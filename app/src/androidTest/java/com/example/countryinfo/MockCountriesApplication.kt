package com.example.countryinfo

import com.example.countryinfo.dagger.AppComponent

class MockCountriesApplication : CountriesApplication() {

    lateinit var mockCountriesComponent: AppComponent

    override fun initDagger(app: CountriesApplication): AppComponent {
        return super.initDagger(app)
    }
}