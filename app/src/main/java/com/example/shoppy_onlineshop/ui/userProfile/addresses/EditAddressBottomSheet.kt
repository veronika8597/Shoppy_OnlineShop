package com.example.shoppy_onlineshop.ui.userProfile.addresses

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.databinding.FragmentEditAddressBinding
import com.example.shoppy_onlineshop.ui.LogIn.UserPreferences
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth


class EditAddressBottomSheet : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogRounded

    override fun onStart() {
        super.onStart()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    interface OnAddressSavedListener {
        fun onAddressSaved()
    }

    var listener: OnAddressSavedListener? = null

    private val viewModel: AddressesViewModel by activityViewModels()
    private lateinit var binding: FragmentEditAddressBinding
    private var addressId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditAddressBinding.inflate(inflater, container, false)

        val countries = resources.getStringArray(R.array.Countries)
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countries)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.countrySpinner.adapter = spinnerAdapter

        addressId = arguments?.getInt("addressId")
        if (addressId != null) {
            // Editing existing address
            viewModel.addresses.value?.find { it.id == addressId }?.let { address ->
                binding.fullNameEditText.setText(address.fullName)
                binding.streetEditText.setText(address.streetAddress)
                binding.aptEditText.setText(address.apt ?: "")
                binding.cityEditText.setText(address.city)
                binding.postalCodeEditText.setText(address.zip?.toString() ?: "")
                binding.countrySpinner.setSelection(countries.indexOf(address.country))
                binding.isDefaultAddressCheckBox.isChecked = address.isDefault
            }
        } else {
            // New address — auto-fill user's name
            val storedName = UserPreferences.getName(requireContext())
            val firebaseName = FirebaseAuth.getInstance().currentUser?.displayName

            val defaultName = storedName ?: firebaseName ?: ""
            binding.fullNameEditText.setText(defaultName)

        }

        binding.saveAddressButton.setOnClickListener {
            val newAddress = AddressItem(
                id = addressId ?: ((viewModel.addresses.value?.size ?: 0) + 1),
                fullName = binding.fullNameEditText.text.toString(),
                streetAddress = binding.streetEditText.text.toString(),
                apt = binding.aptEditText.text.toString().takeIf { it.isNotBlank() },
                city = binding.cityEditText.text.toString(),
                country = binding.countrySpinner.selectedItem.toString(),
                zip = binding.postalCodeEditText.text.toString().toIntOrNull(),
                isDefault = binding.isDefaultAddressCheckBox.isChecked
            )

            if (newAddress.fullName.isNotBlank() && newAddress.streetAddress.isNotBlank() && newAddress.city.isNotBlank()) {
                viewModel.saveOrUpdateAddress(newAddress)
                listener?.onAddressSaved() // this triggers UI update in AddressesFragment
                dismiss()

            } else {
                Toast.makeText(context, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}
