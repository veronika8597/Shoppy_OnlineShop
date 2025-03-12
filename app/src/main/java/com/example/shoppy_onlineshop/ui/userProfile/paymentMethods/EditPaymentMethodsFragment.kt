package com.example.shoppy_onlineshop.ui.userProfile.paymentMethods

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shoppy_onlineshop.R

class EditPaymentMethodsFragment : Fragment() {

    companion object {
        fun newInstance() = EditPaymentMethodsFragment()
    }

    private val viewModel: EditPaymentMethodsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_edit_payment_methods, container, false)
    }
}