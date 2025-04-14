package com.example.shoppy_onlineshop.ui.home.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppy_onlineshop.api.RetroFitInstance
import com.example.shoppy_onlineshop.api.StoreProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {

    private val _searchResults = MutableLiveData<List<StoreProduct>>()
    val searchResults: LiveData<List<StoreProduct>> = _searchResults

    fun searchProducts(query: String) {
        viewModelScope.launch {
            try {
                val response = RetroFitInstance.api.searchProducts(query)
                val rawResults = response.products

                // Filter results that contain the query in the title only (not brand, description, etc.)
                val filtered = rawResults.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.brand.contains(query, ignoreCase = true) ||
                            it.category.contains(query, ignoreCase = true)
                }

                if (filtered.isNotEmpty()) {
                    _searchResults.postValue(filtered)
                } else {
                    // Try as category
                    val categorySlug = query.trim().lowercase().replace(" ", "-")
                    val categoryCall = RetroFitInstance.api.getProductsForCategory(categorySlug)
                    withContext(Dispatchers.IO) {
                        val categoryRes = categoryCall.execute()
                        if (categoryRes.isSuccessful) {
                            _searchResults.postValue(categoryRes.body()?.products)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Search Error: ${e.message}")
            }
        }
    }
}
