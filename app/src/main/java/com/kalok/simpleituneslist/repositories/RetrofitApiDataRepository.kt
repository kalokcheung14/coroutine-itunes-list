package com.kalok.simpleituneslist.repositories

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApiDataRepository: ApiDataRepository() {
    private val _baseUrl = "https://itunes.apple.com"

    init {
        // Create API instance using Retrofit and RxJava
        api = Retrofit.Builder()
            .baseUrl(_baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build().create(DataApi::class.java)
    }
}