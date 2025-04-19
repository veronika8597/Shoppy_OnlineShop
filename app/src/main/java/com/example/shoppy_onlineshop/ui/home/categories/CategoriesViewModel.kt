package com.example.shoppy_onlineshop.ui.home.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppy_onlineshop.api.RetroFitInstance
import com.example.shoppy_onlineshop.api.StoreCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesViewModel : ViewModel() {

    private val _categories = MutableLiveData<List<StoreCategory>>()
    val categories: LiveData<List<StoreCategory>> = _categories

    fun fetchCategories() {
        RetroFitInstance.api.getCategories().enqueue(object : Callback<List<StoreCategory>> {
            override fun onResponse(
                call: Call<List<StoreCategory>>,
                response: Response<List<StoreCategory>>
            ) {
                _categories.value = response.body() ?: emptyList()
            }

            override fun onFailure(call: Call<List<StoreCategory>>, t: Throwable) {
                _categories.value = emptyList()
            }
        })
    }
}
