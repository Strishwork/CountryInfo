package com.example.countryinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.GetCountriesQuery
import com.example.api.ICountriesApi
import io.reactivex.Observable
import org.hamcrest.core.Is.`is`
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Answers
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class CountriesPreviewViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val countriesApiMock = mock(ICountriesApi::class.java)

    private val viewModel by lazy {
        CountriesPreviewViewModel(countriesApiMock)
    }

    @Test
    fun `when getCountries invoked then post default CountryDetailsViewState on success`() {
        val mockResponse: Response<GetCountriesQuery.Data> =
            mock(Response::class.java) as Response<GetCountriesQuery.Data>
        val mockData =
            mock(GetCountriesQuery.Data::class.java, Answers.RETURNS_DEEP_STUBS)
        val mockCountryPreviewFragments = mockCountriesQueryList()

        `when`(mockResponse.data).thenReturn(mockData)
        `when`(mockData.country).thenReturn(
            mockCountryPreviewFragments
        )
        `when`(countriesApiMock.getCountries()).thenReturn(
            Observable.just(
                mockResponse
            )
        )

        val mockObserver = mock(Observer::class.java) as Observer<CountriesPreviewViewState>
        viewModel.countriesLiveData.observeForever(mockObserver)

        val argumentCaptor = ArgumentCaptor.forClass(CountriesPreviewViewState::class.java)
        Mockito.verify(mockObserver, Mockito.times(1)).onChanged(argumentCaptor.capture())

        assert(argumentCaptor.allValues.last() is CountriesPreviewViewState.Default)

        val actualState = argumentCaptor.allValues.last() as CountriesPreviewViewState.Default

        Assert.assertThat(actualState.countries[0].countryName, `is`("Test name"))
        Assert.assertThat(actualState.countries[0].capital, `is`("Test capital"))
        Assert.assertThat(actualState.countries[0].region, `is`("Test subregion name"))
        Assert.assertThat(actualState.countries[0].flagUrl, `is`("https://test.com"))
    }

    @Test
    fun `when countryClicked invoked and CountryPreview is null then post error CountryDetailsViewState on success`() {
        val mockResponse: Response<GetCountriesQuery.Data> =
            mock(Response::class.java) as Response<GetCountriesQuery.Data>
        val mockData =
            mock(GetCountriesQuery.Data::class.java, Answers.RETURNS_DEEP_STUBS)

        `when`(mockResponse.data).thenReturn(mockData)
        `when`(mockData.country).thenReturn(
            null
        )
        `when`(countriesApiMock.getCountries()).thenReturn(
            Observable.just(
                mockResponse
            )
        )

        val mockObserver = mock(Observer::class.java) as Observer<CountriesPreviewViewState>
        viewModel.countriesLiveData.observeForever(mockObserver)

        val argumentCaptor = ArgumentCaptor.forClass(CountriesPreviewViewState::class.java)
        Mockito.verify(mockObserver, Mockito.times(1)).onChanged(argumentCaptor.capture())

        assert(argumentCaptor.allValues.last() is CountriesPreviewViewState.Error)

        val actualState = argumentCaptor.allValues.last() as CountriesPreviewViewState.Error

        Assert.assertThat(actualState.error.message, `is`("Countries are not available :("))
    }

    @Test
    fun `when countryClicked invoked and response has errors then post error CountryDetailsViewState on success`() {
        `when`(countriesApiMock.getCountries()).thenReturn(
            Observable.error(
                ApolloException("Test error")
            )
        )

        val mockObserver = mock(Observer::class.java) as Observer<CountriesPreviewViewState>
        viewModel.countriesLiveData.observeForever(mockObserver)

        val argumentCaptor = ArgumentCaptor.forClass(CountriesPreviewViewState::class.java)
        Mockito.verify(mockObserver, Mockito.times(1)).onChanged(argumentCaptor.capture())

        assert(argumentCaptor.allValues.last() is CountriesPreviewViewState.Error)

        val actualState = argumentCaptor.allValues.last() as CountriesPreviewViewState.Error

        Assert.assertThat(actualState.error.message, `is`("Test error"))
    }

    private fun mockCountriesQueryList(): List<GetCountriesQuery.Country> {
        val mockCountryPreview =
            mock(GetCountriesQuery.Country::class.java, Answers.RETURNS_DEEP_STUBS)
        mockCountryPreview.apply {
            `when`(mockCountryPreview.fragments.countryPreview.name).thenReturn("Test name")
            `when`(mockCountryPreview.fragments.countryPreview.capital).thenReturn("Test capital")
            `when`(mockCountryPreview.fragments.countryPreview.subregion?.region?.name).thenReturn("Test subregion name")
            `when`(mockCountryPreview.fragments.countryPreview.flag?.svgFile).thenReturn("https://test.com")

        }
        return listOf(mockCountryPreview)
    }

}