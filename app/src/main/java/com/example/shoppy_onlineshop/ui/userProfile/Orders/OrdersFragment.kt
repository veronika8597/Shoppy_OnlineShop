package com.example.shoppy_onlineshop.ui.userProfile.Orders

import OrdersViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppy_onlineshop.databinding.FragmentUserOrdersBinding
import com.google.firebase.auth.FirebaseAuth

class OrdersFragment : Fragment() {
    private lateinit var _binding: FragmentUserOrdersBinding
    private val binding get() = _binding
    private val ordersViewModel: OrdersViewModel by activityViewModels()
    private lateinit var orderAdapter: OrdersAdapter
    private lateinit var currentUserId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        setupRecyclerView()
        observeOrders()
        ordersViewModel.loadOrders(currentUserId) // Load orders for the current user
    }

    private fun setupRecyclerView() {
        orderAdapter = OrdersAdapter(mutableListOf()) // Initialize with a mutable list
        binding.ordersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.ordersRecyclerView.adapter = orderAdapter
    }

    private fun observeOrders() {
        ordersViewModel.orders.observe(viewLifecycleOwner) { orders ->
            orderAdapter.updateOrders(orders) // Update the adapter with the new list
        }
    }
    // Function to update the order list in the adapter
    fun OrdersAdapter.updateOrders(newOrders: List<Order>) {
        // Assuming you have a method to update the data in your adapter
        (this.orders as MutableList<Order>).clear()
        (this.orders as MutableList<Order>).addAll(newOrders)
        this.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}