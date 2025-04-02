package com.example.shoppy_onlineshop.ui.userProfile.Orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R

class UserOrdersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewOrders)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val sampleOrders = listOf(
            order("No #12345", "Feb 24, 2025", "Shipped", "$49.99"),
            order("No #67890", "Feb 20, 2025", "Processing", "$19.99"),
            order("No #54321", "Feb 18, 2025", "Delivered", "$89.99")
        )

        orderAdapter = OrderAdapter(sampleOrders)
        recyclerView.adapter = orderAdapter
    }

}