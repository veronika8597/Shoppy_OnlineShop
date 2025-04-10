package com.example.shoppy_onlineshop.ui.home.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppy_onlineshop.api.RetroFitInstance
import com.example.shoppy_onlineshop.api.StoreProduct
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _searchResults = MutableLiveData<List<StoreProduct>>()
    val searchResults: LiveData<List<StoreProduct>> = _searchResults

    fun searchProducts(query: String) {
        viewModelScope.launch {
            try {
                val response = RetroFitInstance.api.searchProducts(query)
                _searchResults.postValue(response.products)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error: ${e.message}")
            }
        }
    }
}
