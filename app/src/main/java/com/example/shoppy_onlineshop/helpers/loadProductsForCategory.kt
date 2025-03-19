package com.example.shoppy_onlineshop.helpers

// ProductLoader.kt
import android.content.Context
import android.widget.Toast
import com.example.shoppy_onlineshop.api.RetroFitInstance
import com.example.shoppy_onlineshop.api.StoreProduct
import com.example.shoppy_onlineshop.api.StoreProductResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object ProductLoader {

    fun loadProductsForCategory(
        context: Context?,
        categorySlug: String,
        onSuccess: (List<StoreProduct>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        // Ensure the categorySlug is not empty
        if (categorySlug.isBlank()) {
            Toast.makeText(context, "Invalid category", Toast.LENGTH_SHORT).show()
            return
        }

        // Make your API call to get products based on the categorySlug
        RetroFitInstance.api.getProductsForCategory(categorySlug)
            .enqueue(object : Callback<StoreProductResponse> {
                override fun onResponse(
                    call: Call<StoreProductResponse>,
                    response: Response<StoreProductResponse>
                ) {
                    if (response.isSuccessful) {
                        // Get the list of products from the response
                        val products = response.body()?.products ?: emptyList()
                        onSuccess(products)
                    } else {
                        // Handle error
                        onFailure("Error: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<StoreProductResponse>, t: Throwable) {
                    // Provide more details about the failure
                    onFailure("Error fetching products: ${t.localizedMessage}")
                    t.printStackTrace() // Log the stack trace for debugging
                }
            })
    }
}
