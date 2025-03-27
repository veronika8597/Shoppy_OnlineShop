package com.example.shoppy_onlineshop.ui.bag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppy_onlineshop.api.StoreProduct

class BagViewModel : ViewModel() {

    private val _bagItems = MutableLiveData<List<CartItem>>(mutableListOf())
        val bagItems: LiveData<List<CartItem>> get() = _bagItems

    fun addProduct(product: StoreProduct) {
        val currentList = _bagItems.value?.toMutableList() ?: mutableListOf()

        val existingItem = currentList.find { it.product.id == product.id }

        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
            currentList.add(CartItem(product = product, quantity = 1))
        }

        _bagItems.value = currentList
    }

    fun removeProduct(productId: Int) {
        val currentList = _bagItems.value?.toMutableList() ?: return
        val updatedList = currentList.filterNot { it.product.id == productId }
        _bagItems.value = updatedList
    }

    fun clearBag() {
        _bagItems.value = emptyList()
    }
}