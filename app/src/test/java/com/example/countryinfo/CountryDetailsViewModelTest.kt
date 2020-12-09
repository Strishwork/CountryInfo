package com.example.countryinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.apollographql.apollo.api.Error
import com.apollographql.apollo.api.Response
import com.example.GetCountryByIdQuery
import com.example.api.ICountriesApi
import com.example.fragment.CountryDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Answers
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import java.util.*

@ExperimentalCoroutinesApi
class CountryDetailsViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val countriesApiMock = mock(ICountriesApi::class.java)

    private val viewModel by lazy {
        CountryDetailsViewModel(countriesApiMock)
    }

    private val testCountryId = UUID.randomUUID().toString()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `when countryClicked invoked then post default CountryDetailsViewState on success`() =
        runBlockingTest {
            val mockResponse: Response<GetCountryByIdQuery.Data> =
                mock(Response::class.java) as Response<GetCountryByIdQuery.Data>
            val mockData = mock(GetCountryByIdQuery.Data::class.java, Answers.RETURNS_DEEP_STUBS)
            val mockCountryDetailsFragment = mockCountryDetailsFragment()

            `when`(mockResponse.data).thenReturn(mockData)
            `when`(mockData.country!![0]!!.fragments.countryDetails).thenReturn(
                mockCountryDetailsFragment
            )
            `when`(countriesApiMock.getCountryById(testCountryId)).thenReturn(mockResponse)

            val mockObserver = mock(Observer::class.java) as Observer<CountryDetailsViewState>
            viewModel.countryLiveData.observeForever(mockObserver)

            viewModel.countryClicked(testCountryId)

            val argumentCaptor = ArgumentCaptor.forClass(CountryDetailsViewState::class.java)
            verify(mockObserver, times(1)).onChanged(argumentCaptor.capture())

            assert(argumentCaptor.allValues.last() is CountryDetailsViewState.Default)

            val actualState = argumentCaptor.allValues.last() as CountryDetailsViewState.Default

            assertThat(actualState.countryDetails.name, `is`("Test name"))
            assertThat(actualState.countryDetails.capital, `is`("Test capital"))
            assertThat(actualState.countryDetails.region, `is`("Test subregion name"))
            assertThat(actualState.countryDetails.population, `is`(1_000_000.0))
            assertThat(actualState.countryDetails.flag, `is`("https://test.com"))
            assertThat(actualState.countryDetails.currencyNames[0], `is`("Test currency"))
            assertThat(actualState.countryDetails.languages[0], `is`("Test language"))
            assertThat(actualState.countryDetails.timezones[0], `is`("UTC+03:00"))
            assertThat(actualState.countryDetails.callingCodes[0], `is`("+380"))
        }

    @Test
    fun `when countryClicked invoked and countryDetails is null then post error CountryDetailsViewState on success`() =
        runBlockingTest {
            val mockResponse: Response<GetCountryByIdQuery.Data> =
                mock(Response::class.java) as Response<GetCountryByIdQuery.Data>
            val mockData = mock(GetCountryByIdQuery.Data::class.java, Answers.RETURNS_DEEP_STUBS)

            `when`(mockResponse.data).thenReturn(mockData)
            `when`(mockData.country!![0]!!.fragments.countryDetails).thenReturn(null)
            `when`(countriesApiMock.getCountryById(testCountryId)).thenReturn(mockResponse)

            val mockObserver = mock(Observer::class.java) as Observer<CountryDetailsViewState>
            viewModel.countryLiveData.observeForever(mockObserver)

            viewModel.countryClicked(testCountryId)

            val argumentCaptor = ArgumentCaptor.forClass(CountryDetailsViewState::class.java)
            verify(mockObserver, times(1)).onChanged(argumentCaptor.capture())

            assert(argumentCaptor.allValues.last() is CountryDetailsViewState.Error)

            val actualState = argumentCaptor.allValues.last() as CountryDetailsViewState.Error

            assertThat(actualState.error.message, `is`("Country is not available :("))

        }

    @Test
    fun `when countryClicked invoked and response has errors then post error CountryDetailsViewState on success`() =
        runBlockingTest {
            val mockResponse: Response<GetCountryByIdQuery.Data> =
                mock(Response::class.java) as Response<GetCountryByIdQuery.Data>
            `when`(countriesApiMock.getCountryById(testCountryId)).thenReturn(mockResponse)
            `when`(mockResponse.hasErrors()).thenReturn(true)
            `when`(mockResponse.errors).thenReturn(listOf(Error("Test error")))

            val mockObserver = mock(Observer::class.java) as Observer<CountryDetailsViewState>
            viewModel.countryLiveData.observeForever(mockObserver)

            viewModel.countryClicked(testCountryId)

            val argumentCaptor = ArgumentCaptor.forClass(CountryDetailsViewState::class.java)
            verify(mockObserver, times(1)).onChanged(argumentCaptor.capture())

            assert(argumentCaptor.allValues.last() is CountryDetailsViewState.Error)

            val actualState = argumentCaptor.allValues.last() as CountryDetailsViewState.Error

            assertThat(actualState.error.message, `is`("Test error"))

        }

    private fun mockCountryDetailsFragment(): CountryDetails {
        val mockCountryDetailsFragment =
            mock(CountryDetails::class.java, Answers.RETURNS_DEEP_STUBS)
        mockCountryDetailsFragment.apply {
            `when`(name).thenReturn("Test name")
            `when`(capital).thenReturn("Test capital")
            `when`(subregion?.region?.name).thenReturn("Test subregion name")
            `when`(population).thenReturn(1_000_000.0)
            `when`(flag?.svgFile).thenReturn("https://test.com")
            `when`(currencies).thenReturn(listOf(CountryDetails.Currency("", "Test currency", "")))
            `when`(officialLanguages).thenReturn(
                listOf(
                    CountryDetails.OfficialLanguage(
                        "",
                        "Test language"
                    )
                )
            )
            `when`(timezones).thenReturn(listOf(CountryDetails.Timezone("", "UTC+03:00")))
            `when`(callingCodes).thenReturn(listOf(CountryDetails.CallingCode("", "380")))
        }
        return mockCountryDetailsFragment
    }

}