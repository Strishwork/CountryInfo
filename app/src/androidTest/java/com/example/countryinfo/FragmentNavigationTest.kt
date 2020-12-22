package com.example.countryinfo

import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.countryinfo.di.ViewModelFactoriesModule
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickBack
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
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
class FragmentNavigationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var countryPreviewViewModelFactoryMock: CountriesPreviewViewModelFactory
    private val previewViewModelMock = Mockito.mock(CountriesPreviewViewModel::class.java)
    private val countriesMutableLiveData = MutableLiveData<CountriesPreviewViewState>()

    @Inject
    lateinit var countryDetailsViewModelFactoryMock: CountryDetailsViewModelFactory
    private val detailsViewModelMock = Mockito.mock(CountryDetailsViewModel::class.java)

    @Before
    fun init() {
        hiltRule.inject()
        `when`(countryPreviewViewModelFactoryMock.create(CountriesPreviewViewModel::class.java))
            .thenReturn(previewViewModelMock)
        setCountryDetails()
        `when`(previewViewModelMock.countriesLiveData).thenReturn(countriesMutableLiveData)

        `when`(countryDetailsViewModelFactoryMock.create(CountryDetailsViewModel::class.java))
            .thenReturn(detailsViewModelMock)
        `when`(detailsViewModelMock.countryLiveData).thenReturn(MutableLiveData<CountryDetailsViewState>())
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @InstallIn(ApplicationComponent::class)
    @Module
    object MockViewModelsFactoryModule {
        @Provides
        @Singleton
        fun provideCountryDetailsViewModelFactory(): CountryDetailsViewModelFactory =
            Mockito.mock(CountryDetailsViewModelFactory::class.java)

        @Provides
        @Singleton
        fun provideCountryPreviewViewModelFactory(): CountriesPreviewViewModelFactory =
            Mockito.mock(CountriesPreviewViewModelFactory::class.java)
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