package com.example.shoppy_onlineshop.ui.userProfile.Orders

import OrdersViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppy_onlineshop.R
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
        orderAdapter = OrdersAdapter(mutableListOf()){ order ->
            val bundle = Bundle().apply {
                putParcelable("order", order)
            }
            findNavController().navigate(R.id.action_userOrdersFragment_to_orderDetailsFragment, bundle)

        } // Initialize with a mutable list
        binding.ordersRecyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        binding.ordersRecyclerView.adapter = orderAdapter
    }

    private fun observeOrders() {
        ordersViewModel.orders.observe(viewLifecycleOwner) { orders ->
            orderAdapter.updateOrders(orders) // Update the adapter with the new list
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}