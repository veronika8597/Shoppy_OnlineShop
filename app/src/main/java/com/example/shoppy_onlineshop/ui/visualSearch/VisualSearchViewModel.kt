package com.example.shoppy_onlineshop.ui.visualSearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppy_onlineshop.api.RetroFitInstance
import com.example.shoppy_onlineshop.api.StoreCategory
import com.example.shoppy_onlineshop.api.StoreProduct
import com.example.shoppy_onlineshop.api.StoreProductResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VisualSearchViewModel : ViewModel() {

    private val _products = MutableLiveData<List<StoreProduct>>()
    val products: LiveData<List<StoreProduct>> = _products

    // Fetch all products (fallback option)
    fun fetchProducts(limit: Int = 100) {
        RetroFitInstance.api.getProducts(limit = limit).enqueue(object : Callback<StoreProductResponse> {
            override fun onResponse(
                call: Call<StoreProductResponse>,
                response: Response<StoreProductResponse>
            ) {
                _products.value = response.body()?.products ?: emptyList()
            }

            override fun onFailure(call: Call<StoreProductResponse>, t: Throwable) {
                _products.value = emptyList()
            }
        })
    }

    // Search products by label
    fun searchProducts(query: String) {
        viewModelScope.launch {
            try {
                val result = RetroFitInstance.api.searchProducts(query)
                _products.value = result.products
            } catch (e: Exception) {
                _products.value = emptyList()
            }
        }
    }

    // Check if a label matches a known category
    fun getCategories(onResult: (List<StoreCategory>) -> Unit) {
        RetroFitInstance.api.getCategories().enqueue(object : Callback<List<StoreCategory>> {
            override fun onResponse(
                call: Call<List<StoreCategory>>,
                response: Response<List<StoreCategory>>
            ) {
                onResult(response.body() ?: emptyList())
            }

            override fun onFailure(call: Call<List<StoreCategory>>, t: Throwable) {
                onResult(emptyList())
            }
        })
    }

    // Fetch products by category slug
    fun fetchProductsForCategory(slug: String) {
        RetroFitInstance.api.getProductsForCategory(slug).enqueue(object : Callback<StoreProductResponse> {
            override fun onResponse(
                call: Call<StoreProductResponse>,
                response: Response<StoreProductResponse>
            ) {
                _products.value = response.body()?.products ?: emptyList()
            }

            override fun onFailure(call: Call<StoreProductResponse>, t: Throwable) {
                _products.value = emptyList()
            }
        })
    }

}
