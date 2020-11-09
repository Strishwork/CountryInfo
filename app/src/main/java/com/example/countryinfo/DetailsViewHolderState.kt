package com.example.countryinfo

data class DetailsViewHolderState(
    val title: String,
    val info: List<String>,
    val isInfoHighlighted: Boolean,
    val detailsSections: DetailsSections
)