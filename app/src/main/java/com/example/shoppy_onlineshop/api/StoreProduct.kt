package com.example.shoppy_onlineshop.api

import com.google.gson.annotations.SerializedName

class StoreProduct(val id: Int, val title: String, val price: Double, val category: String, val description: String, @SerializedName("image") val image: String) {

    override fun toString(): String {
        return "StoreProduct(id=$id, title='$title', price=$price, category='$category', description='$description', image='$image')"
    }
}