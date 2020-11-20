package com.example.countryinfo

import com.example.countryinfo.dagger.AppComponent
import com.example.countryinfo.dagger.AppModule

class TestCountriesApplication : CountriesApplication() {

    override fun initDagger(app: CountriesApplication): AppComponent {
        return DaggerTestAppComponent
            .builder()
            .appModule(AppModule(app))
            .build()
    }
}