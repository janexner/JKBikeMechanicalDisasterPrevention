package com.exner.tools.kjsbikemaintenancechecker.di

import com.exner.tools.kjsbikemaintenancechecker.state.ThemeStateHolder
import com.exner.tools.kjsbikemaintenancechecker.state.ThemeStateHolderImpl
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