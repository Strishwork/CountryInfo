package com.example.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.rx2.rxQuery
import com.example.GetCountriesQuery
import com.example.GetCountryByIdQuery
import org.junit.Rule
import org.junit.Test
import org.mockito.Answers
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CountriesApiTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val apolloClientMock = mock(ApolloClient::class.java, Answers.RETURNS_DEEP_STUBS)

    @Test
    fun test_apolloClientGetCountries() {
        val countriesApi = spy(CountriesApi(apolloClientMock))

        val getCountriesQuery = GetCountriesQuery()

        doReturn(apolloClientMock.rxQuery(getCountriesQuery)).`when`(countriesApi).getCountries()
        countriesApi.getCountries()

        verify(countriesApi).getCountries()
        verify(apolloClientMock).query(getCountriesQuery)
    }

    @Test
    fun test_apolloClientGetCountryById() {
        val countriesApi = spy(CountriesApi(apolloClientMock))

        val getCountryByIdQuery = GetCountryByIdQuery("3".toInput())

        doReturn(apolloClientMock.rxQuery(getCountryByIdQuery)).`when`(countriesApi).getCountryById("3")
        countriesApi.getCountryById("3")

        val captor = ArgumentCaptor.forClass(GetCountryByIdQuery::class.java)
        verify(countriesApi).getCountryById("3")
        verify(apolloClientMock).query(captor.capture())
        assert(captor.allValues.last().id == "3".toInput())
    }
}