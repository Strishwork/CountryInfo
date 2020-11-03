package com.example.countryinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.exception.ApolloException
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

class CountryDetailsViewModel(private val countryApi: ICountriesApi) : ViewModel() {

    private val countryMutableLiveData = MutableLiveData<CountryDetailsViewState>()
    val pollLiveData: LiveData<CountryDetailsViewState> = countryMutableLiveData
    private lateinit var disposable: Disposable

    fun countryClicked(id: String) {
        getCountryById(id)
    }

    private fun getCountryById(id: String) {
        val countryObservable = countryApi.getCountryById(id)
            .flatMap { response ->
                val countryData = response.data?.country
                when {
                    response.hasErrors() -> Observable.error(response.errors?.get(0)?.message?.let {
                        ApolloException(it)
                    })
                    countryData == null -> Observable.error(ApolloException("Country is not available :("))
                    else -> {
                        val country = countryData.map { country ->
                            CountryDetails(
                                country?.fragments?.countryDetails?.name ?: "",
                                country?.fragments?.countryDetails?.capital ?: "",
                                country?.fragments?.countryDetails?.subregion?.region?.name ?: "",
                                country?.fragments?.countryDetails?.population ?: 0.0,
                                country?.fragments?.countryDetails?.flag?.svgFile ?: "",
                                country?.fragments?.countryDetails?.currencies?.map { currency ->
                                    Currency(
                                        currency?.name ?: "",
                                        currency?.symbol ?: ""
                                    )
                                } ?: emptyList(),
                                country?.fragments?.countryDetails?.officialLanguages?.map { language ->
                                    Language(language?.name ?: "")
                                } ?: emptyList(),
                                country?.fragments?.countryDetails?.timezones?.map { timezone ->
                                    TimeZone(timezone?.name ?: "")
                                } ?: emptyList(),
                                country?.fragments?.countryDetails?.callingCodes?.map { callingCode ->
                                    CallingCode(callingCode?.name ?: "")
                                } ?: emptyList()
                            )
                        }
                        Observable.just(country.first())
                    }
                }
            }

        disposable = countryObservable.subscribe(
            { country -> countryMutableLiveData.postValue(CountryDetailsViewState.Default(country)) },
            { error -> countryMutableLiveData.postValue(CountryDetailsViewState.Error(error)) })
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}