package com.example.countryinfo.model

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.rx2.rxQuery
import com.example.GetCountriesQuery
import com.example.GetCountryByIdQuery
import com.example.countryinfo.ICountriesApi
import io.reactivex.Observable
import javax.inject.Inject

class CountriesApi @Inject constructor(private val apolloClient: ApolloClient) :
    ICountriesApi {

    override fun getCountries(): Observable<Response<GetCountriesQuery.Data>> {
        return apolloClient.rxQuery(GetCountriesQuery())
    }

    override fun getCountryById(id: String): Observable<Response<GetCountryByIdQuery.Data>> {
        return apolloClient.rxQuery(GetCountryByIdQuery(id.toInput()))
    }
}