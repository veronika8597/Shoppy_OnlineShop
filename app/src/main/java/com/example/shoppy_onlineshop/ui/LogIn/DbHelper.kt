package com.example.shoppy_onlineshop.ui.LogIn

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class DbHelper(val context: Context){

    private val database: FirebaseDatabase = Firebase.database
    private val usersRef = database.getReference("users")

    fun addUser(user: User){
        // Generate a unique ID for each user using push()
        //val userId = usersRef.push().key
        val userId = Firebase.auth.currentUser?.uid

        // Get the current user UID from Firebase Auth
        val auth = FirebaseAuth.getInstance()

        if (userId != null) {
            // Store the user data under this unique ID
            usersRef.child(userId).setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("DbHelper", "User added successfully!")
                } else {
                    Log.e("DbHelper", "Error adding user", task.exception)
                }
            }
        }else{
            Log.e("DbHelper", "No Firebase UID found for the user")
        }

    }


    fun getUser(email: String, password: String, authenticatedUserUid: String, callback: (User?) -> Unit) {
        usersRef.child(authenticatedUserUid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = snapshot.getValue(User::class.java)
                if (currentUser != null && currentUser.email == email && currentUser.password == password) {
                    callback(currentUser)
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DbHelper", "Error getting user", error.toException())
                callback(null)
            }
        })
    }

}