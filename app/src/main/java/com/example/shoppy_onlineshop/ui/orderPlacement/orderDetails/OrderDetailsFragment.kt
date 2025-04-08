package com.example.shoppy_onlineshop.ui.orderPlacement.orderDetails

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppy_onlineshop.databinding.FragmentOrderDetailsBinding
import com.example.shoppy_onlineshop.ui.userProfile.Orders.Order
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailsFragment : Fragment() {

    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var order: Order
    private var statusListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        order = arguments?.getParcelable("order") ?: return
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderRef = FirebaseDatabase.getInstance().getReference("orders").child(order.orderId)

        // UI setup
        binding.orderIdTextView.text = "Order No: ${order.orderId}"
        binding.orderDateTextView.text = "Date: ${formatDate(order.timestamp)}"
        binding.orderTotalTextView.text =
            "Total: $%.2f".format(order.items.sumOf { it.product.price * it.quantity })

        binding.orderItemsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = OrderDetailsAdapter(order.items)
        }

        // Real-time status updates
        statusListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newStatus = snapshot.getValue(String::class.java)
                _binding?.orderStatusTextView?.text = "Status: $newStatus"
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        orderRef.child("status").addValueEventListener(statusListener!!)

        // Status simulation only if still "In Process"
        orderRef.child("status").get().addOnSuccessListener {
            if (it.value == "In Process") {
                Handler(Looper.getMainLooper()).postDelayed({
                    orderRef.child("status").setValue("Shipped")
                }, 5000)

                Handler(Looper.getMainLooper()).postDelayed({
                    orderRef.child("status").setValue("Delivered")
                }, 10000)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val orderRef = FirebaseDatabase.getInstance().getReference("orders").child(order.orderId)
        statusListener?.let { orderRef.child("status").removeEventListener(it) }
        _binding = null
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
