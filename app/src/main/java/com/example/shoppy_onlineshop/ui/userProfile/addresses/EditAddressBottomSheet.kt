package com.example.shoppy_onlineshop.ui.userProfile.addresses

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



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
        addressId?.let { id ->
            viewModel.addresses.value?.find { it.id == id }?.let { address ->
                binding.fullNameEditText.setText(address.fullName)
                binding.streetEditText.setText(address.streetAddress)
                binding.aptEditText.setText(address.apt ?: "")
                binding.cityEditText.setText(address.city)
                binding.postalCodeEditText.setText(address.zip?.toString() ?: "")
                binding.countrySpinner.setSelection(countries.indexOf(address.country))
                binding.isDefaultAddressCheckBox.isChecked = address.isDefault
            }
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
                listener?.onAddressSaved()
                dismiss()
            } else {
                Toast.makeText(context, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}

