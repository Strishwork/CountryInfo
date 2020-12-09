package com.example.api

import com.apollographql.apollo.ApolloClient
import com.example.GetCountriesQuery
import com.example.GetCountryByIdQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Answers
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class CountriesApiTest {

    private val apolloClientMock = mock(ApolloClient::class.java, Answers.RETURNS_DEEP_STUBS)

    private val countriesApi: CountriesApi = CountriesApi(apolloClientMock)

    private val myScope = GlobalScope

    @Test
    fun test_apolloClientGetCountries() {
        runBlocking {
            myScope.launch {
                countriesApi.getCountries()
                verify(apolloClientMock).query(isA(GetCountriesQuery::class.java))
            }
        }
    }

    @Test
    fun test_apolloClientGetCountryById() {
        runBlocking {
            myScope.launch {
                countriesApi.getCountryById("3")

                val captor = ArgumentCaptor.forClass(GetCountryByIdQuery::class.java)
                verify(apolloClientMock).query(captor.capture())
                assert(captor.allValues.last().id.value == "3")
            }
        }
    }

}