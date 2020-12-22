package com.example.countryinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.ICountriesApi
import com.example.countryinfo.testing.OpenForTesting
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@OpenForTesting
class CountryDetailsViewModel(private val countryApi: ICountriesApi) : ViewModel() {

    private val countryMutableLiveData = MutableLiveData<CountryDetailsViewState>()
    val countryLiveData: LiveData<CountryDetailsViewState> = countryMutableLiveData

    fun countryClicked(id: String) {
        getCountryById(id)
    }

    private fun getCountryById(id: String) {
        viewModelScope.launch {
            val response = countryApi.getCountryById(id)
            val countryDetails = response.data?.country?.get(0)?.fragments?.countryDetails
            when {
                response.hasErrors() ->
                    countryMutableLiveData.postValue(CountryDetailsViewState.Error(
                        response.errors?.get(0)?.message.let { Throwable(it) }
                    ))
                countryDetails == null -> countryMutableLiveData.postValue(
                    CountryDetailsViewState.Error(Throwable("Country is not available :("))
                )
                else -> {
                    val country = initCountryDetails(countryDetails)
                    countryMutableLiveData.postValue(
                        CountryDetailsViewState.Default(country, setCountryStates(country))
                    )
                }
            }
        }
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
                countryDetails.timezones,
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

    private fun formatPopulation(value: Double): String {
        val format = DecimalFormat("0.##")
        val res = if (value >= 1_000_000) {
            format.format(value / 1_000_000) + " m"
        } else {
            format.format(value)
        }
        return res.replace(".", ",")
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

    @Suppress("UNCHECKED_CAST")
    private fun initCountryDetails(countryDetailsFragment: com.example.fragment.CountryDetails): CountryDetails {
        return with(countryDetailsFragment) {
            CountryDetails(
                name,
                capital,
                subregion?.region?.name ?: "",
                formatPopulation(population),
                flag?.svgFile ?: "",
                currencies?.mapNotNull { currencyName -> currencyName?.name ?: "" } as List<String>,
                officialLanguages?.mapNotNull { language -> language?.name ?: "" } as List<String>,
                formatTime(timezones?.mapNotNull { timezone ->
                    timezone?.name ?: ""
                } as List<String>),
                callingCodes?.mapNotNull { callingCode ->
                    "+" + callingCode?.name ?: ""
                } as List<String>
            )
        }
    }
}