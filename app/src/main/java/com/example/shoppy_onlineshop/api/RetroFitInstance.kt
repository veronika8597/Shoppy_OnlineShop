package com.example.shoppy_onlineshop.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitInstance {
    private const val BASE_URL = "https://dummyjson.com/"

    val api: StoreAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StoreAPI::class.java)
    }
}