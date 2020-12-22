package com.example.countryinfo.di

import android.os.Looper
import com.apollographql.apollo.ApolloClient
import com.example.api.CountriesApi
import com.example.api.ICountriesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object CountriesApiModule {

    private const val API_URL = "https://countries-274616.ew.r.appspot.com/"

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