package com.example.countryinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.exception.ApolloException
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.text.DecimalFormat

class CountryDetailsViewModel(private val countryApi: ICountriesApi) : ViewModel() {

    private val countryMutableLiveData = MutableLiveData<CountryDetailsViewState>()
    val pollLiveData: LiveData<CountryDetailsViewState> = countryMutableLiveData

    private lateinit var disposable: Disposable

    fun countryClicked(id: String) {
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
        val state: MutableList<DetailsViewHolderState> = mutableListOf(DetailsViewHolderState())
        state.clear()
        state.add(
            DetailsViewHolderState(
                DetailsSections.COUNTRY_NAME.title,
                listOf(countryDetails.name),
                false
            )
        )
        state.add(
            DetailsViewHolderState(
                DetailsSections.CAPITAL.title,
                listOf(countryDetails.capital),
                false
            )
        )
        state.add(
            DetailsViewHolderState(
                DetailsSections.REGION.title,
                listOf(countryDetails.region),
                false
            )
        )
        state.add(
            DetailsViewHolderState(
                DetailsSections.POPULATION.title,
                listOf(formatPopulation(countryDetails.population)),
                false
            )
        )
        state.add(
            DetailsViewHolderState(
                DetailsSections.CURRENCIES.title,
                countryDetails.currencyNames,
                true,
                DetailsSections.CURRENCIES
            )
        )
        state.add(
            DetailsViewHolderState(
                DetailsSections.LANGUAGES.title,
                countryDetails.languages,
                true,
                DetailsSections.LANGUAGES
            )
        )
        state.add(
            DetailsViewHolderState(
                DetailsSections.TIMEZONES.title,
                formatTime(countryDetails.timezones),
                true,
                DetailsSections.TIMEZONES
            )
        )
        state.add(
            DetailsViewHolderState(
                DetailsSections.CALLING_CODES.title,
                countryDetails.callingCodes,
                true,
                DetailsSections.CALLING_CODES
            )
        )
        return state.toList()
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
                s = "GMT "
                s += value[i][3] + " "
                s += if (value[i].substring(4, 5).toInt() >= 10) {
                    value[i].substring(4, 5)
                } else {
                    value[i][5]
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
                currencies?.map { currencyName -> currencyName?.name } as List<String>,
                officialLanguages?.map { language -> language?.name } as List<String>,
                timezones?.map { timezone -> timezone?.name } as List<String>,
                callingCodes?.map { callingCode -> "+" + callingCode?.name } as List<String>
            )
        }
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}