package com.example.shoppy_onlineshop.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoreCategory(
    val slug: String,
    val name: String,
    val url: String
) : Parcelable