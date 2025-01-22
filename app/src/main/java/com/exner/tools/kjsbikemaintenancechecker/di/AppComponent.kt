package com.exner.tools.kjsbikemaintenancechecker.di

import android.content.Context
import androidx.room.Room
import com.exner.tools.kjsbikemaintenancechecker.database.KJsDAO
import com.exner.tools.kjsbikemaintenancechecker.database.KJsDatabase
import com.exner.tools.kjsbikemaintenancechecker.database.KJsDatabaseCallback
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
    ).fallbackToDestructiveMigration().addCallback(KJsDatabaseCallback(provider)).build()
}
