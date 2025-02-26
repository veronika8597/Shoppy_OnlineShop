package com.example.shoppy_onlineshop.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppy_onlineshop.api.StoreProduct

class FavoritesViewModel : ViewModel() {

    private val favoriteProducts = MutableLiveData<List<StoreProduct>>(mutableListOf())
    val favoriteItems: LiveData<List<StoreProduct>> get() = favoriteProducts

    fun addFavoriteProduct(item: StoreProduct) {
        val updatedList = favoriteProducts.value!!.toMutableList()
        updatedList.add(item)
        favoriteProducts.value = updatedList
    }

    fun removeFavoriteProduct(item: StoreProduct) {
        val updatedList = favoriteProducts.value!!.toMutableList()
        updatedList.remove(item)
        favoriteProducts.value = updatedList
    }

    fun clearBag() {
        favoriteProducts.value = mutableListOf()
    }
}