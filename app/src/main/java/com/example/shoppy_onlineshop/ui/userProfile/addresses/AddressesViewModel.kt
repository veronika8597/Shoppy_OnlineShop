package com.example.shoppy_onlineshop.ui.userProfile.addresses

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel

class AddressesViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("addresses_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _addresses = MutableLiveData<List<AddressItem>>()
    val addresses: MutableLiveData<List<AddressItem>> get() = _addresses

    init {
        loadAddresses()
    }

    fun saveOrUpdateAddress(updatedAddress: AddressItem) {
        val currentList = _addresses.value.orEmpty().toMutableList()
        val index = currentList.indexOfFirst { it.id == updatedAddress.id }
        if (index != -1) {
            currentList[index] = updatedAddress
        } else {
            currentList.add(updatedAddress)
        }
        if (updatedAddress.isDefault) {
            currentList.forEach { it.isDefault = it.id == updatedAddress.id }
        }
        _addresses.value = ArrayList(currentList)
        saveAddresses() // Persist changes
    }

    private fun saveAddresses() {
        val json = gson.toJson(_addresses.value)
        sharedPreferences.edit().putString("addresses_list", json).apply()
    }

    fun loadAddresses() {
        val json = sharedPreferences.getString("addresses_list", "[]")
        val type = object : TypeToken<List<AddressItem>>() {}.type
        val savedAddresses: List<AddressItem> = gson.fromJson(json, type)
        _addresses.value = savedAddresses
    }

    fun deleteAddress(deletedItem: AddressItem) {
        // Get the current list of addresses
        val currentList = _addresses.value.orEmpty().toMutableList()

        // Remove the deleted item from the list
        currentList.remove(deletedItem)

        // Update the LiveData with the new list
        _addresses.value = ArrayList(currentList)

        // Persist the changes in SharedPreferences
        saveAddresses()
    }
}