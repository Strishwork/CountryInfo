package com.example.countryinfo

import com.example.countryinfo.dagger.AppComponent
import com.example.countryinfo.dagger.AppModule

class TestCountriesApplication : CountriesApplication() {

    lateinit var testCountriesComponent: TestAppComponent

    override fun initDagger(app: CountriesApplication): AppComponent {
        testCountriesComponent = DaggerTestAppComponent
            .builder()
            .appModule(AppModule(app))
            .build()
        return testCountriesComponent
    }
}