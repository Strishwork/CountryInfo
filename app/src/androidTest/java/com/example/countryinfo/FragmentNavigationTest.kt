package com.example.countryinfo

import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickBack
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import javax.inject.Inject

@RunWith(AndroidJUnit4ClassRunner::class)
class FragmentNavigationTest {

    @Inject
    lateinit var countryPreviewViewModelFactoryMock: CountriesPreviewViewModelFactory
    private val previewViewModelMock = Mockito.mock(CountriesPreviewViewModel::class.java)
    private val countriesMutableLiveData = MutableLiveData<CountriesPreviewViewState>()

    @Inject
    lateinit var countryDetailsViewModelFactoryMock: CountryDetailsViewModelFactory
    private val detailsViewModelMock = Mockito.mock(CountryDetailsViewModel::class.java)

    @Before
    fun init() {
        (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestCountriesApplication)
            .testCountriesComponent.inject(this)
        `when`(countryPreviewViewModelFactoryMock.create(CountriesPreviewViewModel::class.java))
            .thenReturn(previewViewModelMock)
        setCountryDetails()
        `when`(previewViewModelMock.countriesLiveData).thenReturn(countriesMutableLiveData)

        `when`(countryDetailsViewModelFactoryMock.create(CountryDetailsViewModel::class.java))
            .thenReturn(detailsViewModelMock)
        `when`(detailsViewModelMock.countryLiveData).thenReturn(MutableLiveData<CountryDetailsViewState>())
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun test_navigationToDetailsFragmentAndBack() {
        clickOn(R.id.countryPreviewCardViewRootLayout)
        assertDisplayed(R.id.headerCardView)
        clickBack()
        assertDisplayed(R.id.countryPreviewCardViewRootLayout)
    }

    private fun setCountryDetails() {
        val country1 = CountryPreview(
            "Test id",
            "Test name",
            "Test capital",
            "https://restcountries.eu/data/afg.svg",
            "Test region"
        )
        countriesMutableLiveData.postValue(
            CountriesPreviewViewState.Default(
                listOf(country1)
            )
        )
    }
}