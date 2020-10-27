package com.example.countryinfo

sealed class CountriesPreviewViewState {
    class Default(val countries: List<CountryPreview>) : CountriesPreviewViewState()
    object Error : CountriesPreviewViewState()
}