package com.example.api

import com.apollographql.apollo.ApolloClient
import com.example.GetCountriesQuery
import com.example.GetCountryByIdQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test
import org.mockito.Answers
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.isA
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class CountriesApiTest {

    private val apolloClientMock = mock(ApolloClient::class.java, Answers.RETURNS_DEEP_STUBS)

    private val countriesApi: CountriesApi = CountriesApi(apolloClientMock)

    @Test
    fun test_apolloClientGetCountries() {
        GlobalScope.launch {
            countriesApi.getCountries()
            verify(apolloClientMock).query(isA(GetCountriesQuery::class.java))
        }
    }

    @Test
    fun test_apolloClientGetCountryById() {
        GlobalScope.launch {
            countriesApi.getCountryById("3")

            val captor = ArgumentCaptor.forClass(GetCountryByIdQuery::class.java)
            verify(apolloClientMock).query(captor.capture())
            assert(captor.allValues.last().id.value == "3")
        }
    }

}