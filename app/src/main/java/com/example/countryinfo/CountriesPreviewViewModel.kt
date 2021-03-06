package com.example.countryinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.exception.ApolloException
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

class CountriesPreviewViewModel(private val countryApi: ICountriesApi) : ViewModel() {

    init {
        getCountries()
    }

    private val countriesMutableLiveData = MutableLiveData<CountriesPreviewViewState>()
    val pollLiveData: LiveData<CountriesPreviewViewState> = countriesMutableLiveData
    private lateinit var disposable: Disposable

    private fun getCountries() {
        val countryObservable = countryApi.getCountries()
            .flatMap { response ->
                val countriesDto = response.data?.country
                when {
                    response.hasErrors() -> Observable.error(response.errors?.get(0)?.message?.let {
                        ApolloException(it)
                    })
                    countriesDto == null -> Observable.error(ApolloException("Countries are not available :("))
                    else -> {
                        val countries = countriesDto.map { country ->
                            CountryPreview(
                                country?.fragments?.countryPreview?.name ?: "",
                                country?.fragments?.countryPreview?.capital ?: "",
                                country?.fragments?.countryPreview?.flag?.svgFile ?: "",
                                country?.fragments?.countryPreview?.subregion?.region?.name ?: ""
                            )
                        }
                        Observable.just(countries)
                    }
                }
            }

        disposable = countryObservable.subscribe(
            { countries -> countriesMutableLiveData.postValue(CountriesPreviewViewState.Default(countries)) },
            { error -> countriesMutableLiveData.postValue(CountriesPreviewViewState.Error(error)) })
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}