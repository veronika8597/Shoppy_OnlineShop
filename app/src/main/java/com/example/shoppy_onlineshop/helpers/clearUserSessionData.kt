package com.example.shoppy_onlineshop.helpers

import android.content.Context
import com.example.shoppy_onlineshop.ui.LogIn.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import androidx.core.content.edit

fun clearUserSessionData(context: Context) {
    // Clear addresses
    context.getSharedPreferences("addresses_prefs", Context.MODE_PRIVATE).edit() { clear() }

    // Clear payment methods
    context.getSharedPreferences("payment_methods_prefs", Context.MODE_PRIVATE).edit() { clear() }

    // Clear login credentials (if using DataStore or similar)
    UserPreferences.clearCredentials(context)

    // Firebase sign out
    FirebaseAuth.getInstance().signOut()
}