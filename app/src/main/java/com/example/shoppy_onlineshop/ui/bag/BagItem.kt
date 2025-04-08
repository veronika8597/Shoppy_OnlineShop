package com.example.shoppy_onlineshop.ui.bag

import android.os.Parcelable
import com.example.shoppy_onlineshop.api.StoreProduct
import kotlinx.parcelize.Parcelize

@Parcelize
class BagItem(
    val product: StoreProduct = StoreProduct(),
    var quantity: Int = 1
): Parcelable

fun StoreProduct.toBagItem(quantity: Int = 1): BagItem {
    return BagItem(product = this, quantity = quantity)
}

