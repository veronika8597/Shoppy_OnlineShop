package com.example.shoppy_onlineshop.ui.notifications

import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppy_onlineshop.R

class NotificationsViewModel : ViewModel() {
    // MutableLiveData to update notification data
    private val _notificationLiveData = MutableLiveData<String>()
    val notificationLiveData: LiveData<String> get() = _notificationLiveData

    // Function to update the notification data
    fun sendNotificationData(message: String) {
        _notificationLiveData.value = message
    }
}