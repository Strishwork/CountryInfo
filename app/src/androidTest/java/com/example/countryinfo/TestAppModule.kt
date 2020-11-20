package com.example.countryinfo

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestAppModule(private val app: Application) {

    @Singleton
    @Provides
    fun provideContext(): Context = app
}