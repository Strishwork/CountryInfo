package com.example.countryinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.ICountriesApi
import com.example.countryinfo.testing.OpenForTesting
import kotlinx.coroutines.launch

@OpenForTesting
class CountriesPreviewViewModel(
    private val countryApi: ICountriesApi
) : ViewModel() {

    private val countriesMutableLiveData = MutableLiveData<CountriesPreviewViewState>()
    val countriesLiveData: LiveData<CountriesPreviewViewState> = countriesMutableLiveData

    init {
        getCountries()
    }

    private fun getCountries() {
        viewModelScope.launch {
            val response = countryApi.getCountries()
            val countriesDto = response.data?.country
            when {
                response.hasErrors() ->
                    countriesMutableLiveData.postValue(CountriesPreviewViewState.Error(
                        response.errors?.get(0)?.message.let { Throwable(it) }
                    ))
                countriesDto == null -> countriesMutableLiveData.postValue(
                    CountriesPreviewViewState.Error(Throwable("Countries are not available :("))
                )
                else -> {
                    val countries = countriesDto.map { country ->
                        CountryPreview(
                            country?.fragments?.countryPreview?._id ?: "0",
                            country?.fragments?.countryPreview?.name ?: "",
                            country?.fragments?.countryPreview?.capital ?: "",
                            country?.fragments?.countryPreview?.flag?.svgFile ?: "",
                            country?.fragments?.countryPreview?.subregion?.region?.name ?: ""
                        )
                    }
                    countriesMutableLiveData.postValue(
                        CountriesPreviewViewState.Default(
                            countries
                        )
                    )
                }
            }

        }
    }
}