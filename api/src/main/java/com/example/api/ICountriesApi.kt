package com.example.api

import com.apollographql.apollo.api.Response
import com.example.GetCountriesQuery
import com.example.GetCountryByIdQuery

interface ICountriesApi {
    suspend fun getCountries(): Response<GetCountriesQuery.Data>
    suspend fun getCountryById(id: String): Response<GetCountryByIdQuery.Data>
}