package com.exner.tools.jkbikemechanicaldisasterprevention.di

import com.exner.tools.jkbikemechanicaldisasterprevention.state.ThemeStateHolder
import com.exner.tools.jkbikemechanicaldisasterprevention.state.ThemeStateHolderImpl
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