package com.example.shoppy_onlineshop.helpers

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.shoppy_onlineshop.api.StoreProduct
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


private val databaseFavoritesRef = FirebaseDatabase.getInstance().getReference("favorites")

fun toggleFavoriteStatus(userId: String, emptyHeart: ImageView, filledHeart: ImageView, product: StoreProduct, isFavorite: Boolean): Boolean {

    Log.d("FavoritesViewModel", "Current favorite status: $isFavorite")
    val newFavoriteStatus = !isFavorite // Toggle the status
    Log.d("FavoritesViewModel", "New favorite status: $newFavoriteStatus")

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


fun addToFavorites(userId: String, product: StoreProduct, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    val productId = product.id.toString()

    databaseFavoritesRef.child(userId).child(productId).setValue(product)
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { onFailure(it) }
}

fun removeFromFavorites(userId: String, product: StoreProduct, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {

    Log.e("removeFromFavorites", "Product ID: ${product.id}")

    databaseFavoritesRef.child(userId).child(product.id.toString())
        .removeValue()
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure(it) }
}

fun fetchProductDetails(userId: String, onSuccess: (List<StoreProduct>) -> Unit, onFailure: (Exception) -> Unit) {

    databaseFavoritesRef.child(userId).get().addOnSuccessListener { dataSnapshot ->
        val products = mutableListOf<StoreProduct>()
        for (snapshot in dataSnapshot.children) {
            snapshot.getValue(StoreProduct::class.java)?.let { products.add(it) }
        }
        onSuccess(products)
    }.addOnFailureListener { onFailure(it) }
}


fun isProductInFavorites(userId: String, product: StoreProduct, callback: (Boolean) -> Unit) {

    databaseFavoritesRef.child(userId).child(product.id.toString())
        .addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                callback(dataSnapshot.exists())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(false) // Assume not a favorite on error
            }
        })

}