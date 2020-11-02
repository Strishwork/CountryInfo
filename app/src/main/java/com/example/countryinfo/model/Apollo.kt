package com.example.countryinfo.model

import android.content.Context
import android.os.Looper
import com.apollographql.apollo.ApolloClient

private const val API_URL = "https://countries-274616.ew.r.appspot.com/"
private var instance: ApolloClient? = null

fun apolloClient(context: Context): ApolloClient {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Only the main thread can get the apolloClient instance"
    }

    if (instance != null) {
        return instance!!
    }

    instance = ApolloClient.builder()
        .serverUrl(API_URL)
        .build()

    return instance!!
}