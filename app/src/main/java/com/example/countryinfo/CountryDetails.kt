package com.example.countryinfo

class CountryDetails(
    val name: String = "",
    val capital: String = "",
    val region: String = "",
    val population: Double = 0.0,
    val flag: String = "",
    val currencyNames: List<String> = emptyList(),
    val languages: List<String> = emptyList(),
    val timezones: List<String> = emptyList(),
    val callingCodes: List<String> = emptyList()
)