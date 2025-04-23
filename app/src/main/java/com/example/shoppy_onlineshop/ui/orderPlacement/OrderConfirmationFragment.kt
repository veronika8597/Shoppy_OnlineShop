package com.example.shoppy_onlineshop.ui.orderPlacement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.databinding.FragmentOrderConfirmationBinding
import com.example.shoppy_onlineshop.ui.bag.BagViewModel
import com.example.shoppy_onlineshop.ui.userProfile.Orders.Order
import com.example.shoppy_onlineshop.ui.userProfile.Orders.OrdersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class OrderConfirmationFragment : Fragment() {

    private lateinit var _binding: FragmentOrderConfirmationBinding
    private val binding get() = _binding

    private lateinit var orderId: String
    private var returnToHomeRunnable: Runnable? = null
    private val ordersViewModel = OrdersViewModel()
    private val bagViewModel = BagViewModel()
    private val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var order: Order



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        order = arguments?.getParcelable("order")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderConfirmationBinding.inflate(inflater, container, false)

        binding.orderIdText.text = "${order.orderId}"

        Glide.with(this)
            .asGif()
            .load(R.drawable.success_animation)
            .into(binding.successIcon)

        bagViewModel.clearBag(currentUserID)
        ordersViewModel.saveOrderToFirebase(currentUserID, order,
            onSuccess = {
                Toast.makeText(requireContext(), "Order ID: ${order.orderId} placed!", Toast.LENGTH_LONG).show()
            },
            onFailure = {
                Toast.makeText(requireContext(), "Failed to place order: ${it.message}", Toast.LENGTH_LONG).show()
            }
        )

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
