package com.example.countryinfo

sealed class CountryDetailsViewState {
    class Default(
        val countryDetails: CountryDetails,
        val detailsViewHolderStateList: List<DetailsViewHolderState> = emptyList()
    ) : CountryDetailsViewState()

    class Error(val error: Throwable) : CountryDetailsViewState()
}