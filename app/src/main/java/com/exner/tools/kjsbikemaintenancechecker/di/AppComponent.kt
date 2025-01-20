package com.exner.tools.kjsbikemaintenancechecker.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.exner.tools.kjsbikemaintenancechecker.database.KJsDAO
import com.exner.tools.kjsbikemaintenancechecker.database.KJsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppComponent {

    @Singleton
    @Provides
    fun provideDAO(kjsDatabase : KJsDatabase) : KJsDAO = kjsDatabase.dataDAO()

    @Singleton
    @Provides
    fun provideDatabase(
       @ApplicationContext context : Context,
       provider: Provider<KJsDAO>
    ) : KJsDatabase = Room.databaseBuilder(
        context = context.applicationContext,
        KJsDatabase::class.java,
        name = "kjs_database"
    ).fallbackToDestructiveMigration().addCallback(ProcessDatabaseCallback(provider)).build()

    class ProcessDatabaseCallback(
        private val provider: Provider<KJsDAO>
    ) : RoomDatabase.Callback() {

        private val applicationScope = CoroutineScope(SupervisorJob())

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            applicationScope.launch(Dispatchers.IO) {
                populateDatabaseWithSampleData()
            }
        }

        private suspend fun populateDatabaseWithSampleData() {
            // add sample data? probably not

        }
    }
}
