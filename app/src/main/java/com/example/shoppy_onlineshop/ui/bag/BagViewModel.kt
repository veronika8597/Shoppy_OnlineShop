package com.example.shoppy_onlineshop.ui.bag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppy_onlineshop.api.StoreProduct

class BagViewModel : ViewModel() {

    private val bagProducts = MutableLiveData<List<StoreProduct>>(mutableListOf())
    val bagItems: LiveData<List<StoreProduct>> get() = bagProducts

    fun addProduct(item: StoreProduct) {
        val updatedList = bagProducts.value!!.toMutableList()
        updatedList.add(item)
        bagProducts.value = updatedList
    }

    fun removeProduct(item: StoreProduct) {
        val updatedList = bagProducts.value!!.toMutableList()
        updatedList.remove(item)
        bagProducts.value = updatedList
    }

    fun clearBag() {
        bagProducts.value = mutableListOf()
    }
}