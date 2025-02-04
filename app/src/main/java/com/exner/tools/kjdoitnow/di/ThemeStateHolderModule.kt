package com.exner.tools.kjdoitnow.di

import com.exner.tools.kjdoitnow.state.ThemeStateHolder
import com.exner.tools.kjdoitnow.state.ThemeStateHolderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ThemeStateHolderModule {

    @Binds
    abstract fun bindThemeStateHolder(
        themeStateHolderImpl: ThemeStateHolderImpl
    ): ThemeStateHolder
}