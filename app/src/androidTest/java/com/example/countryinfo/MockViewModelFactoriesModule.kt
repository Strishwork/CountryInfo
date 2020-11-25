package com.example.countryinfo

import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock
import javax.inject.Singleton

@Module
class MockViewModelFactoriesModule {

    @Provides
    @Singleton
    fun provideCountriesPreviewViewModelFactory(): CountriesPreviewViewModelFactory =
        mock(CountriesPreviewViewModelFactory::class.java)

    @Provides
    @Singleton
    fun provideCountryDetailsViewModelFactory(): CountryDetailsViewModelFactory =
        mock(CountryDetailsViewModelFactory::class.java)

}