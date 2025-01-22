package com.example.shoppy_onlineshop.ui.LogIn

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class DbHelper(val context: Context){

    private val database: FirebaseDatabase = Firebase.database
    private val usersRef = database.getReference("users")

    fun addUser(user: User){
        // Generate a unique ID for each user using push()
        val userId = usersRef.push().key

        if (userId != null) {
            // Store the user data under this unique ID
            usersRef.child(userId).setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("DbHelper", "User added successfully!")
                } else {
                    Log.e("DbHelper", "Error adding user", task.exception)
                }
            }
        }

    }
}