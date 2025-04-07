package com.example.shoppy_onlineshop.ui.splashScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.shoppy_onlineshop.MainActivity
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.ui.LogIn.LogInActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Load GIF using Glide
        Glide.with(this)
            .asGif()
            .load(R.drawable.logo_animation)
            .into(findViewById(R.id.storeLogo_splash_image))

        // Delay to show splash screen for a few seconds before transitioning
        android.os.Handler().postDelayed({
            if (currentUser != null && isLoggedIn) {
                startActivity(Intent(this, MainActivity::class.java))
            } else{
            startActivity(Intent(this, LogInActivity::class.java))}

            finish()  // Close the splash activity
        }, 2000) // 2 seconds
    }
}

