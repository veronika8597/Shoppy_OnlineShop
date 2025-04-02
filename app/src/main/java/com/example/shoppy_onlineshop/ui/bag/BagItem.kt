package com.example.shoppy_onlineshop.ui.bag

import com.example.shoppy_onlineshop.api.StoreProduct

class BagItem(
    val product: StoreProduct = StoreProduct(),
    var quantity: Int = 1
)

fun StoreProduct.toBagItem(quantity: Int = 1): BagItem {
    return BagItem(product = this, quantity = quantity)
}

