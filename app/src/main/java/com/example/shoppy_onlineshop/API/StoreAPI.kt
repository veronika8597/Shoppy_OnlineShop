package com.example.shoppy_onlineshop.API

import retrofit2.Call
import retrofit2.http.GET

interface StoreAPI {
    @GET("products")
    fun getProducts(): Call<List<StoreProduct>>
}