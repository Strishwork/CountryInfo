package com.example.countryinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.exception.ApolloException
import com.example.api.ICountriesApi
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.text.DecimalFormat

open class CountryDetailsViewModel(private val countryApi: ICountriesApi) : ViewModel() {

    private val countryMutableLiveData = MutableLiveData<CountryDetailsViewState>()
    open val countryLiveData: LiveData<CountryDetailsViewState> = countryMutableLiveData

    private lateinit var disposable: Disposable

    open fun countryClicked(id: String) {
        getCountryById(id)
    }

    private fun getCountryById(id: String) {
        val countryObservable = countryApi.getCountryById(id)
            .flatMap<CountryDetails> { response ->
                val countryDetails = response.data?.country?.get(0)?.fragments?.countryDetails
                when {
                    response.hasErrors() -> Observable.error(response.errors?.get(0)?.message?.let {
                        ApolloException(it)
                    })
                    countryDetails == null -> Observable.error(ApolloException("Country is not available :("))
                    else -> {
                        val country = initCountryDetails(countryDetails)
                        Observable.just(country)
                    }
                }
            }

        disposable = countryObservable.subscribe(
            { country ->
                countryMutableLiveData.postValue(
                    CountryDetailsViewState.Default(
                        country,
                        setCountryStates(country)
                    )
                )
            },
            { error -> countryMutableLiveData.postValue(CountryDetailsViewState.Error(error)) })
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
                listOf(formatPopulation(countryDetails.population)),
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
                population,
                flag?.svgFile ?: "",
                currencies?.mapNotNull { currencyName -> currencyName?.name ?: "" } as List<String>,
                officialLanguages?.mapNotNull { language -> language?.name ?: "" } as List<String>,
                timezones?.mapNotNull { timezone -> timezone?.name ?: "" } as List<String>,
                callingCodes?.mapNotNull { callingCode ->
                    "+" + callingCode?.name ?: ""
                } as List<String>
            )
        }
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}