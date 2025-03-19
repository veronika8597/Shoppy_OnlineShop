package com.example.shoppy_onlineshop.ui.userProfile.addresses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.shoppy_onlineshop.R

class EditAddressFragment : Fragment() {

    private val viewModel: AddressesViewModel by activityViewModels()
    private lateinit var fullNameEditText: EditText
    private lateinit var streetAddressEditText: EditText
    private lateinit var aptEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var zipEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var isDefaultAddressCheckbox: CheckBox
    private var addressId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_edit_address, container, false)

        fullNameEditText = view.findViewById(R.id.fullName_editText)
        streetAddressEditText = view.findViewById(R.id.street_editText)
        aptEditText = view.findViewById(R.id.apt_editText)
        cityEditText = view.findViewById(R.id.city_editText)
        zipEditText = view.findViewById(R.id.postalCode_editText)
        saveButton = view.findViewById(R.id.saveAddress_button)
        isDefaultAddressCheckbox = view.findViewById(R.id.isDefaultAddress_checkBox)


        addressId = arguments?.getInt("addressId")
        addressId?.let { loadAddress(it) }


        saveButton.setOnClickListener {
            saveAddress()
        }

        return view
    }

    private fun loadAddress(id: Int) {
        viewModel.addresses.value?.find { it.id == id }?.let { address ->
            fullNameEditText.setText(address.fullName)
            streetAddressEditText.setText(address.streetAddress)
            aptEditText.setText(address.apt ?: "")
            cityEditText.setText(address.city)
            zipEditText.setText(address.zip?.toString() ?: "")
            isDefaultAddressCheckbox.isChecked = address.isDefault
        }
    }


    private fun saveAddress() {
        val fullName = fullNameEditText.text.toString()
        val streetAddress = streetAddressEditText.text.toString()
        val apt = aptEditText.text.toString().takeIf { it.isNotBlank() }
        val city = cityEditText.text.toString()
        val zip = zipEditText.text.toString().toIntOrNull()
        val isDefaultAddress = isDefaultAddressCheckbox.isChecked

        if (fullName.isNotBlank() && streetAddress.isNotBlank() && city.isNotBlank()) {
            val newAddress = AddressItem(
                id = addressId ?: ((viewModel.addresses.value?.size ?: 0) + 1),
                fullName = fullName,
                streetAddress = streetAddress,
                apt = apt,
                city = city,
                zip = zip,
                isDefault = isDefaultAddress
            )
            viewModel.saveOrUpdateAddress(newAddress)
            findNavController().popBackStack()
        }
        else{
            Toast.makeText(context, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
        }
    }
}