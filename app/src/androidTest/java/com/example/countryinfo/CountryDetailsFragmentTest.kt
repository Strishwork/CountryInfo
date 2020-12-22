package com.example.countryinfo

import androidx.lifecycle.MutableLiveData
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.countryinfo.di.ViewModelFactoriesModule
import com.schibsted.spain.barista.assertion.BaristaImageViewAssertions.assertHasDrawable
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.schibsted.spain.barista.interaction.BaristaListInteractions.scrollListToPosition
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
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidTest
@UninstallModules(ViewModelFactoriesModule::class)
@RunWith(AndroidJUnit4ClassRunner::class)
class CountryDetailsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var countryDetailsViewModelFactoryMock: CountryDetailsViewModelFactory
    private val viewModelMock = mock(CountryDetailsViewModel::class.java)
    private val countryMutableLiveData = MutableLiveData<CountryDetailsViewState>()

    @ExperimentalCoroutinesApi
    @Before
    fun init() {
        hiltRule.inject()
        `when`(countryDetailsViewModelFactoryMock.create(CountryDetailsViewModel::class.java))
            .thenReturn(viewModelMock)
        setCountryDetails()
        `when`(viewModelMock.countryLiveData).thenReturn(countryMutableLiveData)
        val scenario = launchFragmentInHiltContainer<CountryDetailsFragment>(
            themeResId =
            R.style.Theme_AppCompat_DayNight
        )
    }

    @InstallIn(ApplicationComponent::class)
    @Module
    object MockCountryDetailsViewModelFactoryModule {
        @Provides
        @Singleton
        fun provideCountryDetailsViewModelFactory(): CountryDetailsViewModelFactory =
            mock(CountryDetailsViewModelFactory::class.java)
    }

    @Test
    fun test_areSmileAndGarlandVisible() {
        assertHasDrawable(R.id.smileImageView, R.drawable.smile)
        assertHasDrawable(R.id.garlandImageView, R.drawable.garland)
    }

    @Test
    fun test_areDetailsValuesCorrect() {
        assertRecyclerViewItemCount(R.id.detailsRecyclerView, 8);
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 0, R.id.titleText, "Test name")
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 1, R.id.titleText, "Test capital")
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 2, R.id.titleText, "Test region")
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 3, R.id.titleText, "100")
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 4, R.id.titleText, "Test currency1")
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 4, R.id.titleText2, "Test currency2")
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 5, R.id.titleText, "Test language1")
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 5, R.id.titleText2, "Test language2")
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 6, R.id.titleText, "GMT + 0:00")
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 6, R.id.titleText2, "GMT + 4:30")
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 7, R.id.titleText, "+000")
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 7, R.id.titleText2, "+111")
    }

    @Test
    fun test_isDialogShownAndClosed() {
        assertRecyclerViewItemCount(R.id.detailsRecyclerView, 8);
        scrollListToPosition(R.id.detailsRecyclerView, 5);
        clickListItem(R.id.detailsRecyclerView, 5)
        assertDisplayed("Ok")
        clickOn("Ok")
        assertNotExist("Ok")
    }

    private fun setCountryDetails() {
        val country = CountryDetails(
            "Test name",
            "Test capital",
            "Test region",
            "100",
            "https://restcountries.eu/data/afg.svg",
            listOf("Test currency1", "Test currency2"),
            listOf("Test language1", "Test language2"),
            listOf("UTC+00:00", "UTC+04:30"),
            listOf("+000", "+111")
        )
        countryMutableLiveData.postValue(
            CountryDetailsViewState.Default(
                country,
                setCountryStates(country)
            )
        )
    }

    private fun setCountryStates(countryDetails: CountryDetails): List<DetailsViewHolderState> {
        return listOf(
            DetailsViewHolderState(
                DetailsSections.COUNTRY_NAME.title,
                listOf(countryDetails.name),
                false,
                DetailsSections.DEFAULT
            ),
            DetailsViewHolderState(
                DetailsSections.CAPITAL.title,
                listOf(countryDetails.capital),
                false,
                DetailsSections.DEFAULT
            ),
            DetailsViewHolderState(
                DetailsSections.REGION.title,
                listOf(countryDetails.region),
                false,
                DetailsSections.DEFAULT
            ),
            DetailsViewHolderState(
                DetailsSections.POPULATION.title,
                listOf(countryDetails.population),
                false,
                DetailsSections.DEFAULT
            ),
            DetailsViewHolderState(
                DetailsSections.CURRENCIES.title,
                countryDetails.currencyNames,
                true,
                DetailsSections.CURRENCIES
            ),
            DetailsViewHolderState(
                DetailsSections.LANGUAGES.title,
                countryDetails.languages,
                true,
                DetailsSections.LANGUAGES
            ),
            DetailsViewHolderState(
                DetailsSections.TIMEZONES.title,
                formatTime(countryDetails.timezones),
                true,
                DetailsSections.TIMEZONES
            ),
            DetailsViewHolderState(
                DetailsSections.CALLING_CODES.title,
                countryDetails.callingCodes,
                true,
                DetailsSections.CALLING_CODES
            )
        )
    }

    private fun formatTime(value: List<String>): List<String> {
        val state: MutableList<String> = mutableListOf(String())
        state.clear()
        var s = ""
        for (i in value.indices) {
            if (value[i].length > 3) {
                s = if (value[i].substring(4, 5).toInt() >= 10) {
                    "GMT ${value[i][3]} ${value[i].substring(4, 5)}"
                } else {
                    "GMT ${value[i][3]} ${value[i][5]}"
                }
            }
            s += value[i].takeLast(3)
            state.add(s)
        }
        return state
    }

}