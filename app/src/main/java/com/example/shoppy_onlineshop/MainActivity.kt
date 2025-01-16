package com.example.shoppy_onlineshop

import android.os.Bundle
import android.util.Log

import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shoppy_onlineshop.api.StoreProduct
import com.example.shoppy_onlineshop.api.RetroFitInstance
import com.example.shoppy_onlineshop.databinding.ActivityMainBinding
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_favorites,
                R.id.navigation_notifications,
                R.id.navigation_bag,
                R.id.navigation_deals
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        RetroFitInstance.api.getProducts().enqueue(object : Callback<List<StoreProduct>> {
            override fun onResponse(call: Call<List<StoreProduct>>, response: Response<List<StoreProduct>>) {
                if(response.isSuccessful){
                    val products = response.body()
                    if (products != null) {
                        Log.d("Products", products.toString())}
                }
            }

            override fun onFailure(call: Call<List<StoreProduct>>, t: Throwable) {
                Log.d("Products", t.message.toString())
            }

        })
    }
}