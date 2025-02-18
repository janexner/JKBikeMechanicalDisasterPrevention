package com.exner.tools.jkbikemechanicaldisasterprevention.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsDAO
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsDatabase
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsDatabaseCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppComponent {

    @Singleton
    @Provides
    fun provideDAO(kjsDatabase: KJsDatabase): KJsDAO = kjsDatabase.dataDAO()

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
        provider: Provider<KJsDAO>
    ): KJsDatabase = Room.databaseBuilder(
        context = context.applicationContext,
        KJsDatabase::class.java,
        name = "kjs_database"
    )
        .addMigrations(MIGRATION_1_2)
        .fallbackToDestructiveMigration()
        .addCallback(KJsDatabaseCallback(provider))
        .build()
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // ride and template can safely be deleted and rebuilt
        db.execSQL("DELETE TABLE `Ride`")
        db.execSQL("DELETE TABLE `TemplateActivity`")
        db.execSQL("CREATE TABLE IF NOT EXISTS `Ride` (`name` TEXT NOT NULL, `level` INTEGER NOT NULL, `created_instant` INTEGER NOT NULL, `uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
        db.execSQL("CREATE TABLE IF NOT EXISTS `TemplateActivity` (`ride_level` INTEGER, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `is_ebike_specific` INTEGER NOT NULL, `uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
        // activity is more complicated, there may be some with rideLevels
        db.execSQL("CREATE TABLE `Temporary` (`title` TEXT NOT NULL, `description` TEXT NOT NULL, `is_completed` INTEGER NOT NULL, `bike_uid` INTEGER, `is_ebike_specific` INTEGER NOT NULL, `ride_level` INTEGER, `ride_uid` INTEGER, `created_instant` INTEGER NOT NULL, `due_date` TEXT, `done_instant` INTEGER, `uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
        db.execSQL(
            "INSERT INTO Temporary (title, description, is_completed, bike_uid, is_ebike_specific, ride_level, ride_uid, created_instant, due_date, done_instant, uid) " +
                    "SELECT title, description, is_completed, bike_uid, is_ebike_specific, SUBSTR(ride_level, 1, 1), ride_uid, created_instant, due_date, done_instant, uid) " +
                    "FROM Activity"
        )
        db.execSQL("DELETE TABLE `Activity`")
        db.execSQL("ALTER TABLE `Temporary` RENAME TO `Activity`")
    }
}