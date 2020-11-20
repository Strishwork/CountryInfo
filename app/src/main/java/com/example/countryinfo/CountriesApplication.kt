package com.example.countryinfo

import android.app.Application
import com.example.countryinfo.dagger.AppComponent
import com.example.countryinfo.dagger.AppModule
import com.example.countryinfo.dagger.DaggerAppComponent

open class CountriesApplication : Application() {

    lateinit var countriesComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        countriesComponent = initDagger(this)
    }

    protected open fun initDagger(app: CountriesApplication): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()
}