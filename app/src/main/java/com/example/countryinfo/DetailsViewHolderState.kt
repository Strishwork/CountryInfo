package com.example.countryinfo

data class DetailsViewHolderState(
    val title: String = "",
    val info: List<String> = emptyList(),
    val isInfoHighlighted: Boolean = false,
    val detailsSections: DetailsSections = DetailsSections.COUNTRY_NAME
)