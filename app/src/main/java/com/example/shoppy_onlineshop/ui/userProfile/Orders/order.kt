package com.example.shoppy_onlineshop.ui.userProfile.Orders

import com.example.shoppy_onlineshop.ui.bag.BagItem

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val items: List<BagItem> = emptyList(),
    val status: String = "In Process",
    val timestamp: Long = 0L
)