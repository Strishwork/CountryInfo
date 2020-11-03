package com.example.countryinfo

sealed class CountryDetailsViewState {
    class Default(val countryDetails: CountryDetails) : CountryDetailsViewState()
    class Error(val error: Throwable) : CountryDetailsViewState()
}