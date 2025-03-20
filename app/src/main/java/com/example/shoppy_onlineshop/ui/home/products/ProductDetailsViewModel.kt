package com.example.shoppy_onlineshop.ui.home.products

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.shoppy_onlineshop.api.RetroFitInstance
import com.example.shoppy_onlineshop.api.StoreProduct
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
}