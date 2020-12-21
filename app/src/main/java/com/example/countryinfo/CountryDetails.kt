package com.example.countryinfo

data class CountryDetails(
    val name: String,
    val capital: String,
    val region: String,
    val population: String,
    val flag: String,
    val currencyNames: List<String>,
    val languages: List<String>,
    val timezones: List<String>,
    val callingCodes: List<String>
)