package com.example.countryinfo.di

import com.example.api.ICountriesApi
import com.example.countryinfo.CountriesPreviewViewModelFactory
import com.example.countryinfo.CountryDetailsViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object ViewModelFactoriesModule {

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