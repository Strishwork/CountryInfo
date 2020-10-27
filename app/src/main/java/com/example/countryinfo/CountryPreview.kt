package com.example.countryinfo

import com.google.gson.annotations.SerializedName

class CountryPreview(
    @SerializedName("name") val countryName: String,
    val capital: String,
    @SerializedName("svgFile")var flagUrl: String,
    var region: String
)