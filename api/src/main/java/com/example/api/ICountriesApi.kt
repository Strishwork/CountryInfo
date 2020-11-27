package com.example.api

import com.apollographql.apollo.api.Response
import com.example.GetCountriesQuery
import com.example.GetCountryByIdQuery
import io.reactivex.Observable

interface ICountriesApi {
    fun getCountries(): Observable<Response<GetCountriesQuery.Data>>
    fun getCountryById(id: String): Observable<Response<GetCountryByIdQuery.Data>>
}