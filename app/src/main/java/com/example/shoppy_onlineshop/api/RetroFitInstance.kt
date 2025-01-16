package com.example.shoppy_onlineshop.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitInstance {
    val api: StoreAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StoreAPI::class.java)
    }
}

