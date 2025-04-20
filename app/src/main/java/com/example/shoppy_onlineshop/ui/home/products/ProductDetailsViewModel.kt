package com.example.shoppy_onlineshop.ui.home.products

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.shoppy_onlineshop.api.RetroFitInstance
import com.example.shoppy_onlineshop.api.Review
import com.example.shoppy_onlineshop.api.StoreProduct
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsViewModel : ViewModel() {

    fun loadProductDetails(
        context: Context,
        productID: Int,
        onSuccess: (StoreProduct) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (productID <= 0) {
            Toast.makeText(context, "Invalid product ID", Toast.LENGTH_SHORT).show()
            return
        }

        RetroFitInstance.api.getProductDetails(productID)
            .enqueue(object : Callback<StoreProduct> {
                override fun onResponse(
                    call: Call<StoreProduct>,
                    response: Response<StoreProduct>
                ) {
                    if (response.isSuccessful) {
                        val product = response.body()
                        if (product != null) {
                            onSuccess(product)
                        } else {
                            onFailure("No product details found.")
                        }
                    } else {
                        onFailure("Error: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<StoreProduct>, t: Throwable) {
                    onFailure("Error fetching product details: ${t.localizedMessage}")
                    t.printStackTrace()
                }
            })
    }

    // In ProductDetailsViewModel.kt
    fun fetchFirebaseReviews(productId: Int, onResult: (List<Review>) -> Unit) {
        val dbRef = FirebaseDatabase.getInstance().getReference("product_reviews").child(productId.toString())
        dbRef.get().addOnSuccessListener { snapshot ->
            val reviews = snapshot.children.mapNotNull { it.getValue(Review::class.java) }
            onResult(reviews)
        }.addOnFailureListener {
            onResult(emptyList())
        }
    }

    fun submitReviewToFirebase(productId: Int, review: Review, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val dbRef = FirebaseDatabase.getInstance().getReference("product_reviews")
        val reviewId = dbRef.child(productId.toString()).push().key
        if (reviewId != null) {
            dbRef.child(productId.toString()).child(reviewId).setValue(review)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onFailure(it) }
        } else {
            onFailure(Exception("Failed to generate review ID"))
        }
    }

}