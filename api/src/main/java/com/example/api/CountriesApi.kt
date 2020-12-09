package com.example.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.coroutines.toFlow
import com.example.GetCountriesQuery
import com.example.GetCountryByIdQuery

class CountriesApi(private val apolloClient: ApolloClient) :
    ICountriesApi {

    override suspend fun getCountries(): Response<GetCountriesQuery.Data> {
        return apolloClient.query(GetCountriesQuery()).await()
    }

    override suspend fun getCountryById(id: String): Response<GetCountryByIdQuery.Data> {
        return apolloClient.query(GetCountryByIdQuery(id.toInput())).await()
    }
}