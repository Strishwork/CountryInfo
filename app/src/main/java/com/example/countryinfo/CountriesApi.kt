package com.example.countryinfo

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.rx2.rxQuery
import com.example.GetCountriesQuery
import com.example.GetCountryByIdQuery
import io.reactivex.Observable

class CountriesApi(private val apolloClient: ApolloClient) : ICountriesApi {

    override fun getCountries(): Observable<Response<GetCountriesQuery.Data>> {
        return apolloClient.rxQuery(GetCountriesQuery())
    }

    override fun getCountryById(id: String): Observable<Response<GetCountryByIdQuery.Data>> {
        return apolloClient.rxQuery(GetCountryByIdQuery(id.toInput()))
    }
}