package com.example.countryinfo

import android.app.Application
import com.example.countryinfo.dagger.AppComponent
import com.example.countryinfo.dagger.AppModule
import com.example.countryinfo.dagger.DaggerAppComponent

class CountriesApplication : Application() {

    lateinit var countriesComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        countriesComponent = initDagger(this)
    }

    private fun initDagger(app: CountriesApplication): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()
}