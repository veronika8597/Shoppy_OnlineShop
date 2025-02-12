package com.example.shoppy_onlineshop.ui.LogIn

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppy_onlineshop.MainActivity
import com.example.shoppy_onlineshop.R
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_log_in)

        val login2reg: TextView = findViewById(R.id.sign_up_text)

        val userEmail: EditText = findViewById(R.id.email_input)
        val userPassword: EditText = findViewById(R.id.password_input)
        val loginButton: Button = findViewById(R.id.login_button)


        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        supportActionBar?.hide()


        loginButton.setOnClickListener {
            val enteredEmail = userEmail.text.toString()
            val enteredPassword = userPassword.text.toString()

            if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(this, "All fields must be filled in", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(enteredEmail, enteredPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    }
                }

        }

        login2reg.setOnClickListener {
            intent = Intent(applicationContext, RegistrationActivity::class.java)
            startActivity(intent)
        }

    }
}