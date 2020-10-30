package com.example.countryinfo

sealed class CountriesPreviewViewState {
    class Default(val countries: List<CountryPreview>) : CountriesPreviewViewState()
    class Error(val error: Throwable) : CountriesPreviewViewState()
}