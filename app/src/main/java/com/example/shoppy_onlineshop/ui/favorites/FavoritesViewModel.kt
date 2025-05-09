package com.example.shoppy_onlineshop.ui.favorites

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppy_onlineshop.api.StoreProduct
import com.example.shoppy_onlineshop.helpers.fetchProductDetails
import com.example.shoppy_onlineshop.helpers.removeFromFavorites
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavoritesViewModel() : ViewModel() {

    private val _favoriteItems = MutableLiveData<List<StoreProduct>>()
    val favoriteItems: LiveData<List<StoreProduct>> = _favoriteItems

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadFavoriteProducts(userId: String?) {

        if (userId != null) {
            fetchProductDetails(
                userId,
                onSuccess = { products ->
                    _favoriteItems.value = products
                    _errorMessage.value = null // Clear previous error
                },
                onFailure = { exception ->
                    _errorMessage.value = "Failed to load favorites: ${exception.message}"
                })
        }

    }

// removes from the favorites list and updates the UI
    fun removeProductFromFavorites(userId: String, product: StoreProduct) {

        val currentFaveList = _favoriteItems.value.orEmpty().toMutableList()

        currentFaveList.remove(product)

        _favoriteItems.value = ArrayList(currentFaveList)

        removeFromFavorites(userId, product,
            onSuccess = {
                Log.d("FavoritesViewModel", "Product removed from favorites")
            },
            onFailure = {
                Log.e("FavoritesViewModel", "Failed to remove product from favorites", it)
            })
    }


}