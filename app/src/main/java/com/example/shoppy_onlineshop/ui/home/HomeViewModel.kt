package com.example.shoppy_onlineshop.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _exploreTitle = MutableLiveData<String>().apply {
        value = "Explore these categories"
    }
    val exploreTitle: LiveData<String> = _exploreTitle

    // Placeholder data for featured items (you'll replace with real data)
    private val _featuredItems = MutableLiveData<List<String>>().apply {
        value = listOf("Item 1", "Item 2", "Item 3", "Item 4")
    }
    val featuredItems: LiveData<List<String>> = _featuredItems

    // Placeholder data for recommended items (you'll replace with real data)
    private val _recommendedItems = MutableLiveData<List<String>>().apply {
        value = listOf("Recommended 1", "Recommended 2", "Recommended 3")
    }
    val recommendedItems: LiveData<List<String>> = _recommendedItems

}