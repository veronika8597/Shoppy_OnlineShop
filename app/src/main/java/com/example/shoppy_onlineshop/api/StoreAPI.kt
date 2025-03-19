package com.example.shoppy_onlineshop.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StoreAPI {
    @GET("products")
    fun getProducts(): Call<StoreProductResponse>

    @GET("products/categories")
    fun getCategories(): Call<List<StoreCategory>>

    @GET("products/search")
    fun searchProducts(@Query("q") query: String) : Response<List<StoreProduct>>

    @GET("products/category/{categorySlug}")
    fun getProductsForCategory(@Path("categorySlug") category: String): Call<StoreProductResponse>
}