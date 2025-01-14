package com.example.shoppy_onlineshop.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.shoppy_onlineshop.MainActivity
import com.example.shoppy_onlineshop.R

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

        // Load GIF using Glide
        Glide.with(this)
            .asGif()
            .load(R.drawable.store_logo_animation)
            .into(findViewById(R.id.storeLogo_splash_image))

        // Delay to show splash screen for a few seconds before transitioning
        android.os.Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()  // Close the splash activity
        }, 2000) // 2 seconds
    }
}