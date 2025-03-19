package com.example.shoppy_onlineshop.ui.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppy_onlineshop.api.RetroFitInstance
import com.example.shoppy_onlineshop.api.StoreCategory
import com.example.shoppy_onlineshop.api.StoreProduct
import com.example.shoppy_onlineshop.api.StoreProductResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _exploreTitle = MutableLiveData<String>().apply {
        value = "Explore these categories"
    }
    val exploreTitle: LiveData<String> = _exploreTitle

    //products
    private val _allProducts = MutableLiveData<List<StoreProduct>>()
    val allProducts: LiveData<List<StoreProduct>> = _allProducts

    //Categories
    private val _featuredCategories = MutableLiveData<List<StoreCategory>>()
    val featuredCategories: LiveData<List<StoreCategory>> = _featuredCategories

    private val _allCategories = MutableLiveData<List<StoreCategory>>()
    val allCategories: LiveData<List<StoreCategory>> = _allCategories

    init {
        fetchProducts()
        fetchCategories()
    }

    private fun fetchProducts() {
        RetroFitInstance.api.getProducts().enqueue(object : Callback<StoreProductResponse> {
            override fun onResponse(call: Call<StoreProductResponse>, response: Response<StoreProductResponse>) {
                if (response.isSuccessful) {
                    val products = response.body()?.products ?: emptyList()
                    Log.d("HomeViewModel", "Products fetched successfully: $products")
                    _allProducts.value = products
                } else {
                    Log.e("HomeViewModel", "Products API request failed: ${response.message()}")
                    onFailure(call, Throwable("API request failed"))
                }
            }

            override fun onFailure(call: Call<StoreProductResponse>, t: Throwable) {
                Log.e("HomeViewModel", "Products API request failed: ${t.message}")
            }
        })
    }

    private fun fetchCategories(){
        RetroFitInstance.api.getCategories().enqueue(object : Callback<List<StoreCategory>> {
            override fun onResponse(call: Call<List<StoreCategory>>, response: Response<List<StoreCategory>>) {
                if (response.isSuccessful) {
                    val categories = response.body() ?: emptyList()
                    Log.d("HomeViewModel", "Categories fetched successfully: $categories")
                    val featured = categories.shuffled().take(5) // Select first 5 categories
                    _featuredCategories.value = featured

                    _allCategories.value = categories // Store all categories
                } else {
                    Log.e("HomeViewModel", "Categories API request failed: ${response.message()}")
                    onFailure(call, Throwable("API request failed"))
                }
            }

            override fun onFailure(call: Call<List<StoreCategory>>, t: Throwable) {
                Log.e("HomeViewModel", "Categories API request failed: ${t.message}")
            }
        })
    }

}