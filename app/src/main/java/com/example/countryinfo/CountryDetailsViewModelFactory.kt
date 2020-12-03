package com.example.countryinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.api.ICountriesApi
import com.example.countryinfo.testing.OpenForTesting

@OpenForTesting
class CountryDetailsViewModelFactory(
    private val countryApi: ICountriesApi
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CountryDetailsViewModel(countryApi) as T
    }

}