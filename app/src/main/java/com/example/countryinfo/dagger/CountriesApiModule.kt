package com.example.countryinfo.dagger

import android.os.Looper
import com.apollographql.apollo.ApolloClient
import com.example.countryinfo.ICountriesApi
import com.example.countryinfo.model.CountriesApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CountriesApiModule {

    companion object {
        private const val API_URL = "https://countries-274616.ew.r.appspot.com/"
    }

    @Provides
    @Singleton
    fun provideCountriesApi(apolloClient: ApolloClient): ICountriesApi = CountriesApi(apolloClient)

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        check(Looper.myLooper() == Looper.getMainLooper()) {
            "Only the main thread can get the apolloClient instance"
        }

        return ApolloClient.builder()
            .serverUrl(API_URL)
            .build()

    }
}