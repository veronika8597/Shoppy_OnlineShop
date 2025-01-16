package com.example.shoppy_onlineshop.api

import retrofit2.Call
import retrofit2.http.GET

interface StoreAPI {
    @GET("products")
    fun getProducts(): Call<StoreProductResponse>

    @GET("products/categories")
    fun getCategories(): Call<List<StoreCategory>> // Updated return type
}