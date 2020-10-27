package com.example.countryinfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.api.Response
import com.example.GetCountriesQuery
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONObject

class CountriesPreviewViewModel(private val countryApi: ICountriesApi) : ViewModel() {
    private val TAG = "CountriesPreviewViewMod"

    init {
        getCountries()
    }

    private val countriesMutableLiveData = MutableLiveData<CountriesPreviewViewState>()
    val pollLiveData: LiveData<CountriesPreviewViewState> = countriesMutableLiveData

    private fun getCountries() {
        val myObservable = countryApi.getCountries()
        val myObserver = object : Observer<Response<GetCountriesQuery.Data>> {
            override fun onComplete() {
                Log.d(TAG, "onComplete: ")
            }

            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe: ")
            }

            override fun onNext(t: Response<GetCountriesQuery.Data>) {
                val data = t.data?.country?.map {
                    country -> CountryPreview(
                    country?.fragments?.countryPreview?.name?:"",
                    country?.fragments?.countryPreview?.capital?:"",
                    country?.fragments?.countryPreview?.flag?.svgFile?:"",
                    country?.fragments?.countryPreview?.subregion?.region?.name?:""
                )
                }
                countriesMutableLiveData.postValue(
                    data?.let { CountriesPreviewViewState.Default(it) }
                )
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "onError: ", e)
                countriesMutableLiveData.postValue(
                    CountriesPreviewViewState.Error
                )
            }
        }
        myObservable.subscribe(myObserver)
    }

}