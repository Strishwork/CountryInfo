package com.example.countryinfo.dagger

import com.example.countryinfo.CountriesPreviewViewModelFactory
import com.example.countryinfo.CountryDetailsViewModelFactory
import com.example.countryinfo.ICountriesApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelFactoriesModule {

    @Provides
    @Singleton
    fun provideCountriesPreviewViewModelFactory(countriesApi: ICountriesApi): CountriesPreviewViewModelFactory {
        return CountriesPreviewViewModelFactory(countriesApi)
    }

    @Provides
    @Singleton
    fun provideCountryDetailsViewModelFactory(countriesApi: ICountriesApi): CountryDetailsViewModelFactory =
        CountryDetailsViewModelFactory(countriesApi)
}