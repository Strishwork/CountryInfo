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
            { country -> countryMutableLiveData.postValue(CountryDetailsViewState.Default(country)) },
            { error -> countryMutableLiveData.postValue(CountryDetailsViewState.Error(error)) })
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