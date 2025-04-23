package com.example.shoppy_onlineshop.ui.userProfile.paymentMethods

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.databinding.FragmentEditPaymentMethodsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditPaymentMethodBottomSheet : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogRounded

    override fun onStart() {
        super.onStart()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    private val viewModel: PaymentMethodsViewModel by activityViewModels()
    private lateinit var binding: FragmentEditPaymentMethodsBinding
    private var paymentMethodId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPaymentMethodsBinding.inflate(inflater, container, false)

        paymentMethodId = arguments?.getInt("paymentMethodId")
        paymentMethodId?.let { id ->
            viewModel.paymentMethods.value?.find { it.id == id }?.let { paymentMethod ->
                binding.cardNumberEditText.setText(paymentMethod.cardNumber)
                binding.expiryDateEditText.setText(paymentMethod.expiryDate)
                binding.cvvEditText.setText(paymentMethod.cvv)
                binding.cardholderNameEditText.setText(paymentMethod.cardholderName)
                binding.defaultPaymentCheckbox.isChecked = paymentMethod.isDefault
            }
        }

        binding.savePaymentButton.setOnClickListener {
            val cardNumber = binding.cardNumberEditText.text.toString()
            val expiryDate = binding.expiryDateEditText.text.toString()
            val cvv = binding.cvvEditText.text.toString()
            val cardholderName = binding.cardholderNameEditText.text.toString()
            val isDefault = binding.defaultPaymentCheckbox.isChecked

            if (cardNumber.isNotBlank() && expiryDate.isNotBlank() && cvv.isNotBlank() && cardholderName.isNotBlank()) {
                val newPaymentMethod = PaymentMethodItem(
                    id = paymentMethodId ?: ((viewModel.paymentMethods.value?.size ?: 0) + 1),
                    cardNumber = cardNumber,
                    expiryDate = expiryDate,
                    cvv = cvv,
                    cardholderName = cardholderName,
                    isDefault = isDefault
                )
                viewModel.saveOrUpdatePaymentMethod(newPaymentMethod)
                dismiss()
            } else {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}



/*
class EditPaymentMethodBottomSheet : BottomSheetDialogFragment() {

    val viewModel: PaymentMethodsViewModel by viewModels()
    private lateinit var adapter: PaymentMethodsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardNumberEditText: EditText
    private lateinit var expiryDateEditText: EditText
    private lateinit var cvvEditText: EditText
    private lateinit var cardholderNameEditText: EditText
    private lateinit var savePaymentButton: Button
    private lateinit var isDefaultPaymentMethodCheckBox: CheckBox
    private var paymentMethodId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  inflater.inflate(R.layout.fragment_edit_payment_methods, container, false)

        cardNumberEditText = view.findViewById(R.id.card_number_editText)
        expiryDateEditText = view.findViewById(R.id.expiry_date_editText)
        cvvEditText = view.findViewById(R.id.cvv_editText)
        cardholderNameEditText = view.findViewById(R.id.cardholder_name_editText)
        isDefaultPaymentMethodCheckBox = view.findViewById(R.id.default_payment_checkbox)
        savePaymentButton = view.findViewById(R.id.save_payment_button)

        paymentMethodId = arguments?.getInt("paymentMethodId")
        paymentMethodId?.let { loadPaymentMethod(it) }


        savePaymentButton.setOnClickListener {
            savePaymentMethod()
        }

        return view
    }

    private fun loadPaymentMethod(id: Int) {
        viewModel.paymentMethods.value?.find { it.id == id }?.let { paymentMethod ->
            cardNumberEditText.setText(paymentMethod.cardNumber)
            expiryDateEditText.setText(paymentMethod.expiryDate)
            cvvEditText.setText(paymentMethod.cvv)
            cardholderNameEditText.setText(paymentMethod.cardholderName)
            isDefaultPaymentMethodCheckBox.isChecked = paymentMethod.isDefault
        }
    }

    private fun savePaymentMethod() {
        val cardNumber = cardNumberEditText.text.toString()
        val expiryDate = expiryDateEditText.text.toString()
        val cvv = cvvEditText.text.toString()
        val cardholderName = cardholderNameEditText.text.toString()
        val isDefault = isDefaultPaymentMethodCheckBox.isChecked

        if (cardNumber.isNotBlank() && expiryDate.isNotBlank() && cvv.isNotBlank() && cardholderName.isNotBlank()) {
            val newPaymentMethod = PaymentMethodItem(
                id = paymentMethodId ?: ((viewModel.paymentMethods.value?.size ?: 0) + 1),
                cardNumber = cardNumber,
                expiryDate = expiryDate,
                cvv = cvv,
                cardholderName = cardholderName,
                isDefault = isDefault
            )
            viewModel.saveOrUpdatePaymentMethod(newPaymentMethod)
            findNavController().popBackStack()
        }
        else{
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }
}*/
