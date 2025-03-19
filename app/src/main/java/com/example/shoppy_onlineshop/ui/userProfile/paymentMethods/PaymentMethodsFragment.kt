package com.example.shoppy_onlineshop.ui.userProfile.paymentMethods

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.helpers.setupSwipeToDelete

class PaymentMethodsFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        viewModel.loadPaymentMethods() // Force LiveData to reload from SharedPreferences
    }

    private val viewModel: PaymentMethodsViewModel by viewModels()
    private lateinit var adapter: PaymentMethodsAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_payment_methods, container, false)
        recyclerView = view.findViewById(R.id.userPaymentMethods_recyclerView)
        val addCardButton: Button = view.findViewById(R.id.addPaymentMethod_button)

        adapter = PaymentMethodsAdapter(mutableListOf<PaymentMethodItem>()) { paymentMethodItem ->
            val bundle = Bundle().apply { putInt("paymentMethodId", paymentMethodItem.id) }
            findNavController().navigate(R.id.action_paymentMethodsFragment_to_editPaymentMethodFragment, bundle)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe LiveData and update adapter
        viewModel.paymentMethods.observe(viewLifecycleOwner) { paymentMethods ->
            adapter.updateData(paymentMethods)
        }

        addCardButton.setOnClickListener {
            findNavController().navigate(R.id.action_paymentMethodsFragment_to_editPaymentMethodFragment)
        }

        setupSwipeToDelete(recyclerView) { position ->
            val deletedItem = adapter.getItem(position)
            viewModel.deletePaymentMethod(deletedItem)
        }

        return view

    }

}