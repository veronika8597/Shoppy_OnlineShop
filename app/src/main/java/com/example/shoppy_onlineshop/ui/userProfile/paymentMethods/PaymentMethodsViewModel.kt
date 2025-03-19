package com.example.shoppy_onlineshop.ui.userProfile.paymentMethods

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PaymentMethodsViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("payment_methods_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _paymentMethods = MutableLiveData<List<PaymentMethodItem>>()
    val paymentMethods: MutableLiveData<List<PaymentMethodItem>> get() = _paymentMethods

    init {
        loadPaymentMethods()
    }

    fun saveOrUpdatePaymentMethod(updatedPaymentMethods: PaymentMethodItem) {
        val currentList = _paymentMethods.value.orEmpty().toMutableList()
        val index = currentList.indexOfFirst { it.id == updatedPaymentMethods.id }
        if (index != -1) {
            currentList[index] = updatedPaymentMethods
        } else {
            currentList.add(updatedPaymentMethods)
        }
        if (updatedPaymentMethods.isDefault) {
            currentList.forEach { it.isDefault = it.id == updatedPaymentMethods.id }
        }
        _paymentMethods.value = ArrayList(currentList)
        savePaymentMethods() // Persist changes
    }

    private fun savePaymentMethods() {
        val json = gson.toJson(_paymentMethods.value)
        sharedPreferences.edit() { putString("payment_methods_list", json) }
    }

    fun loadPaymentMethods() {
        val json = sharedPreferences.getString("payment_methods_list", "[]")
        val type = object : TypeToken<List<PaymentMethodItem>>() {}.type
        val savedPaymentMethods: List<PaymentMethodItem> = gson.fromJson(json, type)
        _paymentMethods.value = savedPaymentMethods
    }

        fun deletePaymentMethod(deletedItem: PaymentMethodItem) {
        // Get the current list of payment methods
        val currentList = _paymentMethods.value.orEmpty().toMutableList()

        // Remove the deleted item from the list
        currentList.remove(deletedItem)

        // Update the LiveData with the new list
        _paymentMethods.value = ArrayList(currentList)

        // Persist the changes in SharedPreferences
        savePaymentMethods()
    }
}