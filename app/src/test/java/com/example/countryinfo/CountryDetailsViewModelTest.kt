package com.example.countryinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.apollographql.apollo.api.Response
import com.example.GetCountryByIdQuery
import com.example.fragment.CountryDetails
import io.reactivex.Observable
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Answers
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import java.util.*

class CountryDetailsViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val countriesApiMock = mock(ICountriesApi::class.java)

    private val viewModel by lazy {
        CountryDetailsViewModel(countriesApiMock)
    }

    private val testCountryId = UUID.randomUUID().toString()

    @Test
    fun `when countryClicked invoked then post default CountryDetailsViewState on success`() {
        val mockResponse: Response<GetCountryByIdQuery.Data> =
            mock(Response::class.java) as Response<GetCountryByIdQuery.Data>
        val mockData = mock(GetCountryByIdQuery.Data::class.java, Answers.RETURNS_DEEP_STUBS)
        val mockCountryDetailsFragment =
            mock(CountryDetails::class.java, Answers.RETURNS_DEEP_STUBS)
        mockCountryDetailsFragment.apply {
            `when`(name).thenReturn("Test name")
            `when`(capital).thenReturn("Test capital")
            `when`(subregion?.region?.name).thenReturn("Test subregion name")
            `when`(population).thenReturn(1_000_000.0)
            `when`(flag?.svgFile).thenReturn("https://test.com")
            `when`(currencies).thenReturn(listOf())
            `when`(officialLanguages).thenReturn(listOf())
            `when`(timezones).thenReturn(listOf())
            `when`(callingCodes).thenReturn(listOf())
        }

        `when`(mockResponse.data).thenReturn(mockData)
        `when`(mockData.country!![0]!!.fragments.countryDetails).thenReturn(mockCountryDetailsFragment)
        `when`(countriesApiMock.getCountryById(testCountryId)).thenReturn(Observable.just(mockResponse))

        val mockObserver = mock(Observer::class.java) as Observer<CountryDetailsViewState>
        viewModel.countryLiveData.observeForever(mockObserver)

        viewModel.countryClicked(testCountryId)

        val argumentCaptor = ArgumentCaptor.forClass(CountryDetailsViewState::class.java)
        verify(mockObserver, times(1)).onChanged(argumentCaptor.capture())

        assert(argumentCaptor.allValues.last() is CountryDetailsViewState.Default)

        val actualState = argumentCaptor.allValues.last() as CountryDetailsViewState.Default

        assertThat(actualState.countryDetails.name, `is`("Test name"))
    }

}