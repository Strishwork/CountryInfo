package com.example.countryinfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.toJson
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
                val data = t.data?.toJson().toString()
                countriesMutableLiveData.postValue(
                    CountriesPreviewViewState.Default(deserializeResponse(data))
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

    private fun deserializeResponse(data: String): List<CountryPreview> {
        val list: MutableList<CountryPreview> = ArrayList()
        val root = JSONObject(data).getJSONObject("data").getJSONArray("Country")
        for (i in 0 until root.length()) {
            val obj = root.getJSONObject(i)
            val countryPreview = Gson().fromJson(obj.toString(), CountryPreview::class.java)
            if (obj.has("subregion")) {
                countryPreview.region =
                    obj.getJSONObject("subregion").getJSONObject("region").getString("name")
            }
            if (obj.has("flag")) {
                countryPreview.flagUrl = obj.getJSONObject("flag").getString("svgFile")
            }
            list.add(countryPreview)
        }
        return list
    }

}