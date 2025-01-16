package com.example.shoppy_onlineshop.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppy_onlineshop.api.RetroFitInstance
import com.example.shoppy_onlineshop.api.StoreProduct
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _exploreTitle = MutableLiveData<String>().apply {
        value = "Explore these categories"
    }
    val exploreTitle: LiveData<String> = _exploreTitle

    private val _allProducts = MutableLiveData<List<StoreProduct>>()
    val allProducts: LiveData<List<StoreProduct>> = _allProducts

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        RetroFitInstance.api.getProducts().enqueue(object : Callback<List<StoreProduct>> {
            override fun onResponse(call: Call<List<StoreProduct>>, response: Response<List<StoreProduct>>) {
                if (response.isSuccessful) {
                    val products = response.body() ?: emptyList()
                    _allProducts.value = products
                } else {
                    onFailure(call, Throwable("API request failed"))
                }
            }

            override fun onFailure(call: Call<List<StoreProduct>>, t: Throwable) {
                Log.e("API Error", t.message ?: "Unknown error")
            }
        })
    }
}