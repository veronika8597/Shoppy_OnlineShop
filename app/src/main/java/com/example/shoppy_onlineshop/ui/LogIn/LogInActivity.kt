package com.example.shoppy_onlineshop.ui.LogIn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppy_onlineshop.MainActivity
import com.example.shoppy_onlineshop.R

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_log_in)

        val login2reg: TextView = findViewById(R.id.sign_up_text)

        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        supportActionBar?.hide()

        val loginButton: Button = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        login2reg.setOnClickListener {
            intent = Intent(applicationContext, RegistrationActivity::class.java)
            startActivity(intent)
        }







    }
}