package com.example.shoppy_onlineshop.ui.userProfile.Orders

import android.os.Parcelable
import com.example.shoppy_onlineshop.ui.bag.BagItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val orderId: String = "",
    val userId: String = "",
    val items: List<BagItem> = emptyList(),
    val status: String = "In Process",
    val timestamp: Long = 0L
): Parcelable