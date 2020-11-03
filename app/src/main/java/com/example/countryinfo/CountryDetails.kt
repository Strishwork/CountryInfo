package com.example.countryinfo

class CountryDetails(
    val name: String,
    val capital: String,
    val region: String,
    val population: Double,
    val flag: String,
    val currencies: List<Currency>,
    val languages: List<Language>,
    val timezones: List<TimeZone>,
    val callingCodes: List<CallingCode>
)

class Currency(
    val name: String,
    val symbol: String
)

class Language(
    val name: String
)

class TimeZone(
    val name: String
)

class CallingCode(
    val name: String
)