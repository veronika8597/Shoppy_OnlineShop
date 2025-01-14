package com.example.shoppy_onlineshop.ui.deals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DealsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Deals Fragment"
    }
    val text: LiveData<String> = _text
}