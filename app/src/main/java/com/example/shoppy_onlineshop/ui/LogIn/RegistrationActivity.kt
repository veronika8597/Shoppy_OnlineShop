package com.example.shoppy_onlineshop.ui.LogIn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shoppy_onlineshop.R
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseAppLifecycleListener
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        supportActionBar?.hide()
        setContentView(R.layout.activity_registration)

        val reg2login: TextView = findViewById(R.id.already_have_account_text)

        val userName: EditText = findViewById(R.id.full_name_input)
        val userEmail: EditText = findViewById(R.id.email_input)
        val userPassword: EditText = findViewById(R.id.password_input)
        val userConfirmPassword: EditText = findViewById(R.id.confirm_password_input)
        val registerButton: Button = findViewById(R.id.register_button)

        reg2login.setOnClickListener {
            intent = Intent(applicationContext, LogInActivity::class.java)
            startActivity(intent)
        }
        // Initialize Firebase Auth
        var auth: FirebaseAuth = Firebase.auth


        registerButton.setOnClickListener {

            val name = userName.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val password = userPassword.text.toString().trim()
            val confirmPassword = userConfirmPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields must be filled",Toast.LENGTH_LONG).show()
            }
            else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match",Toast.LENGTH_LONG).show()
            }
            else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Authentication success, now store user details in the database
                            val user = User(name, email, password)

                            // Pass the User object to DbHelper
                            DbHelper(this).addUser(user)

                            Toast.makeText(this, "Registration successful", Toast.LENGTH_LONG)
                                .show()

                            // Clear fields after registration
                            userName.text.clear()
                            userEmail.text.clear()
                            userPassword.text.clear()
                            userConfirmPassword.text.clear()

                            // Navigate to the next screen
                            intent = Intent(applicationContext, LogInActivity::class.java)
                            startActivity(intent)

                        }
                        else {
                            Toast.makeText(this, "Registration failed", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}