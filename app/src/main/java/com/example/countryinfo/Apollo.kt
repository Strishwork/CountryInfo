package com.example.countryinfo

import android.content.Context
import android.os.Looper
import com.apollographql.apollo.ApolloClient

private var instance: ApolloClient? = null

fun apolloClient(context: Context): ApolloClient {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Only the main thread can get the apolloClient instance"
    }

    if (instance != null) {
        return instance!!
    }

    instance = ApolloClient.builder()
        .serverUrl("https://countries-274616.ew.r.appspot.com/")
        .build()

    return instance!!
}