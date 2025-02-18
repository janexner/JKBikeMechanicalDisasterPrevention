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
        // rides first
        db.execSQL("CREATE TABLE `TempRide` (`name` TEXT NOT NULL, `level` INTEGER NOT NULL, `created_instant` INTEGER NOT NULL, `uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
        db.execSQL(
            "INSERT INTO `TempRide` (name, level, created_instant, uid) " +
                    "SELECT name, SUBSTR(level, 1, 1), created_instant, uid FROM `Ride`"
        )
        db.execSQL("DROP TABLE `Ride`")
        db.execSQL("ALTER TABLE `TempRide` RENAME TO `Ride`")

        // template activities next
        db.execSQL("CREATE TABLE `TempTemplateActivity` (`ride_level` INTEGER, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `is_ebike_specific` INTEGER NOT NULL, `uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
        db.execSQL(
            "INSERT INTO `TempTemplateActivity` (ride_level, title, description, is_ebike_specific, uid) " +
                    "SELECT SUBSTR(ride_level, 1, 1), title, description, is_ebike_specific, uid FROM TemplateActivity"
        )
        db.execSQL("DROP TABLE `TemplateActivity`")
        db.execSQL("ALTER TABLE `TempTemplateActivity` RENAME TO `TemplateActivity`")

        // activity last but not least
        db.execSQL("CREATE TABLE `Temporary` (`title` TEXT NOT NULL, `description` TEXT NOT NULL, `is_completed` INTEGER NOT NULL, `bike_uid` INTEGER, `is_ebike_specific` INTEGER NOT NULL, `ride_level` INTEGER, `ride_uid` INTEGER, `created_instant` INTEGER NOT NULL, `due_date` TEXT, `done_instant` INTEGER, `uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
        db.execSQL(
            "INSERT INTO Temporary (title, description, is_completed, bike_uid, is_ebike_specific, ride_level, ride_uid, created_instant, due_date, done_instant, uid) " +
                    "SELECT title, description, is_completed, bike_uid, is_ebike_specific, SUBSTR(ride_level, 1, 1), ride_uid, created_instant, due_date, done_instant, uid FROM Activity"
        )
        db.execSQL("DROP TABLE `Activity`")
        db.execSQL("ALTER TABLE `Temporary` RENAME TO `Activity`")

        // and the view!
        db.execSQL("DROP VIEW IF EXISTS `ActivityWithBikeData`")
        db.execSQL("CREATE VIEW `ActivityWithBikeData` AS SELECT b.name as bike_name, b.uid as bike_uid, a.title as activity_title, a.description as activity_description, a.is_completed as activity_is_completed, a.ride_uid as activity_ride_uid, a.created_instant as activity_created_instant, a.due_date as activity_due_date, a.done_instant as activity_done_instant, a.is_ebike_specific as activity_is_ebike_specific, a.ride_level as activity_ride_level, a.uid as activity_uid FROM Activity a LEFT JOIN Bike b ON b.uid = a.bike_uid ORDER BY a.due_date DESC")
    }
}