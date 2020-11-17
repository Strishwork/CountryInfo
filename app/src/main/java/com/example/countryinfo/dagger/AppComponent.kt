package com.example.countryinfo.dagger

import com.example.countryinfo.CountryDetailsFragment
import com.example.countryinfo.CountryPreviewFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelFactoriesModule::class, CountriesApiModule::class])
interface AppComponent {

    fun inject(target: CountryPreviewFragment)
    fun inject(target: CountryDetailsFragment)

}