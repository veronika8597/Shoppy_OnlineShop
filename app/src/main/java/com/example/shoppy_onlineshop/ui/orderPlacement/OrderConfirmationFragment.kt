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

    private lateinit var currentUserID: String
    private var orderRef = FirebaseDatabase.getInstance().getReference("Orders")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = arguments?.getString("orderId") ?: "N/A"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =  FragmentOrderConfirmationBinding.inflate(inflater, container, false)

        binding.orderIdText.text = "Order ID: $orderId"

         Glide.with(this)
            .asGif()
            .load(R.drawable.success) // Replace with your actual gif drawable
            .into(binding.successIcon)

        binding.goHomeButton.setOnClickListener {
            findNavController().navigate(R.id.action_orderConfirmationFragment_to_navigation_home)
        }
        // ⏳ Auto-return to home after 3 seconds
        binding.root.postDelayed({
            findNavController().navigate(
                R.id.action_orderConfirmationFragment_to_navigation_home
            )
        }, 4000)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}