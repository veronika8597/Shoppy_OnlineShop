package com.example.shoppy_onlineshop.ui.orderPlacement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.databinding.FragmentOrderConfirmationBinding
import com.google.firebase.database.FirebaseDatabase

class OrderConfirmationFragment : Fragment() {

    private lateinit var _binding: FragmentOrderConfirmationBinding
    private val binding get() = _binding

    private lateinit var orderId: String
    private var returnToHomeRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = arguments?.getString("orderId") ?: "N/A"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderConfirmationBinding.inflate(inflater, container, false)

        binding.orderIdText.text = "Order ID: $orderId"

        Glide.with(this)
            .asGif()
            .load(R.drawable.success_animation)
            .into(binding.successIcon)

        val navController = findNavController()

        binding.goHomeButton.setOnClickListener {
            returnToHomeRunnable?.let { binding.root.removeCallbacks(it) }
            navController.navigate(R.id.navigation_home)
        }

        returnToHomeRunnable = Runnable {
            if (isAdded) {
                navController.navigate(R.id.navigation_home)
            }
        }

        binding.root.postDelayed(returnToHomeRunnable!!, 4000)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        returnToHomeRunnable?.let { binding.root.removeCallbacks(it) }
    }
}
