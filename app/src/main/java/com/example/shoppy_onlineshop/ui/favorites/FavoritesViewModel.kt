package com.example.shoppy_onlineshop.ui.favorites

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppy_onlineshop.api.StoreProduct
import com.example.shoppy_onlineshop.helpers.addToFavorites
import com.example.shoppy_onlineshop.helpers.fetchProductDetails
import com.example.shoppy_onlineshop.helpers.removeFromFavorites
import com.google.firebase.database.FirebaseDatabase

class FavoritesViewModel() : ViewModel() {

    private val _favoriteItems = MutableLiveData<List<StoreProduct>>()
    val favoriteItems: LiveData<List<StoreProduct>> = _favoriteItems

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadFavoriteProducts(userId: String?) {

        if (userId != null) {
            fetchProductDetails(userId,
                onSuccess = { products ->
                    _favoriteItems.value = products
                    _errorMessage.value = null // Clear previous error
                },
                onFailure = { exception ->
                    _errorMessage.value = "Failed to load favorites: ${exception.message}"
                })
        }

    }

    fun toggleFavoriteStatus(userId: String, emptyHeart: ImageView, filledHeart: ImageView, product: StoreProduct, isFavorite: Boolean): Boolean {

        val newFavoriteStatus = !isFavorite // Toggle the status

        if (newFavoriteStatus) {
            addToFavorites(userId, product,
                onSuccess = {
                    Log.d("FavoritesViewModel", "Product added to favorites")
                            },
                onFailure = {
                    Log.e("FavoritesViewModel", "Failed to add product to favorites", it)
                })
            filledHeart.visibility = View.VISIBLE
            emptyHeart.visibility = View.GONE

        } else {
            removeFromFavorites(userId, product,
                onSuccess = {
                    Log.d("FavoritesViewModel", "Product removed from favorites")
                },
                onFailure = {
                    Log.e("FavoritesViewModel", "Failed to remove product from favorites", it)
                })

            filledHeart.visibility = View.GONE
            emptyHeart.visibility = View.VISIBLE
        }

        return newFavoriteStatus
    }


// removes from the favorites list and updates the UI
    fun removeProductFromFavorites(userId: String, product: StoreProduct) {

    }

    fun addProductToFavorites(userId: String, product: StoreProduct) {

    }
}