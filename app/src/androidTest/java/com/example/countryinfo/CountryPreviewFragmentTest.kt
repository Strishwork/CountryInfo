package com.example.countryinfo

import androidx.lifecycle.MutableLiveData
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.countryinfo.di.ViewModelFactoriesModule
import com.schibsted.spain.barista.assertion.BaristaImageViewAssertions.assertHasDrawable
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidTest
@UninstallModules(ViewModelFactoriesModule::class)
@RunWith(AndroidJUnit4ClassRunner::class)
class CountryPreviewFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var countryPreviewViewModelFactoryMock: CountriesPreviewViewModelFactory
    private val viewModelMock = Mockito.mock(CountriesPreviewViewModel::class.java)
    private val countriesMutableLiveData = MutableLiveData<CountriesPreviewViewState>()

    @ExperimentalCoroutinesApi
    @Before
    fun init() {
        hiltRule.inject()
        `when`(countryPreviewViewModelFactoryMock.create(CountriesPreviewViewModel::class.java))
            .thenReturn(viewModelMock)
        setCountryDetails()
        `when`(viewModelMock.countriesLiveData).thenReturn(countriesMutableLiveData)
        val scenario = launchFragmentInHiltContainer<CountryPreviewFragment>()
    }

    @InstallIn(ApplicationComponent::class)
    @Module
    object MockCountryPreviewViewModelFactoryModule {
        @Provides
        @Singleton
        fun provideCountryPreviewViewModelFactory(): CountriesPreviewViewModelFactory =
            Mockito.mock(CountriesPreviewViewModelFactory::class.java)
    }

    @Test
    fun test_areHeaderViewsAreVisibleAndCorrect() {
        assertHasDrawable(R.id.smileImageView, R.drawable.smile)
        assertDisplayed(R.string.choose_card_label)
    }

    @Test
    fun test_arePreviewsValuesCorrectAndVisible() {

        assertRecyclerViewItemCount(R.id.detailsRecyclerView, 3);
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 0, R.id.country_name, "Test name1")
        assertDisplayedAtPosition(
            R.id.detailsRecyclerView,
            0,
            R.id.country_capital,
            "Test capital1"
        )
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 0, R.id.country_region, "Test region1")
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 1, R.id.country_name, "Test name2")
        assertDisplayedAtPosition(
            R.id.detailsRecyclerView,
            1,
            R.id.country_capital,
            "Test capital2"
        )
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 1, R.id.country_region, "Test region2")
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 2, R.id.country_name, "Test name3")
        assertDisplayedAtPosition(
            R.id.detailsRecyclerView,
            2,
            R.id.country_capital,
            "Test capital3"
        )
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 2, R.id.country_region, "Test region3")
    }

    private fun setCountryDetails() {
        val country1 = CountryPreview(
            "Test id1",
            "Test name1",
            "Test capital1",
            "https://restcountries.eu/data/afg.svg",
            "Test region1"
        )
        val country2 = CountryPreview(
            "Test id2",
            "Test name2",
            "Test capital2",
            "https://restcountries.eu/data/afg.svg",
            "Test region2"
        )
        val country3 = CountryPreview(
            "Test id3",
            "Test name3",
            "Test capital3",
            "https://restcountries.eu/data/afg.svg",
            "Test region3"
        )
        countriesMutableLiveData.postValue(
            CountriesPreviewViewState.Default(
                listOf(country1, country2, country3)
            )
        )
    }

}