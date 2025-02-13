package com.example.shoppy_onlineshop.ui.LogIn

import UserPreferences
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shoppy_onlineshop.MainActivity
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.ui.userProfile.AccountSection
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LogInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_log_in)

        val login2reg: TextView = findViewById(R.id.sign_up_text)
        val userEmail: EditText = findViewById(R.id.email_input)
        val userPassword: EditText = findViewById(R.id.password_input)
        val loginButton: Button = findViewById(R.id.login_button)


        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        supportActionBar?.hide()


 /*       ----------------------------------------   *NOTE*  ---------------------------------------------
                Should check if the user tried to log out and not automatically log in everytime.
        ----------------------------------------   *NOTE*  ---------------------------------------------*/

        // 🔹 Try to auto-login if credentials exist
        lifecycleScope.launch {
            val (savedEmail, savedPassword) = UserPreferences.getCredentials(this@LogInActivity)
            if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                autoLogin(savedEmail, savedPassword)
            }
        }

        loginButton.setOnClickListener {
            val enteredEmail = userEmail.text.toString()
            val enteredPassword = userPassword.text.toString()

            if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(this, "All fields must be filled in", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            loginUser(enteredEmail, enteredPassword)
        }

        login2reg.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    // 🔹 Save credentials for future auto-login
                    lifecycleScope.launch {
                        UserPreferences.saveCredentials(this@LogInActivity, email, password)
                    }

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun autoLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
    }
}