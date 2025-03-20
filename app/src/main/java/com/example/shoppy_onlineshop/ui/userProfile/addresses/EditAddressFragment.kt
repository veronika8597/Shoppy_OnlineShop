package com.example.shoppy_onlineshop.ui.userProfile.addresses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
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
    private lateinit var spinner: Spinner


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
        spinner = view.findViewById(R.id.country_spinner)
        saveButton = view.findViewById(R.id.saveAddress_button)
        isDefaultAddressCheckbox = view.findViewById(R.id.isDefaultAddress_checkBox)

        val countries = resources.getStringArray(R.array.Countries)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        addressId = arguments?.getInt("addressId")
        addressId?.let { loadAddress(it, countries) }


        saveButton.setOnClickListener {
            saveAddress()
        }

        return view
    }

    private fun loadAddress(id: Int, countries: Array<String>) {
        viewModel.addresses.value?.find { it.id == id }?.let { address ->
            fullNameEditText.setText(address.fullName)
            streetAddressEditText.setText(address.streetAddress)
            aptEditText.setText(address.apt ?: "")
            cityEditText.setText(address.city)
            zipEditText.setText(address.zip?.toString() ?: "")
            spinner.setSelection(countries.indexOf(address.country))
            isDefaultAddressCheckbox.isChecked = address.isDefault
        }
    }


    private fun saveAddress() {
        val fullName = fullNameEditText.text.toString()
        val streetAddress = streetAddressEditText.text.toString()
        val apt = aptEditText.text.toString().takeIf { it.isNotBlank() }
        val city = cityEditText.text.toString()
        val country = spinner.selectedItem.toString()
        val zip = zipEditText.text.toString().toIntOrNull()
        val isDefaultAddress = isDefaultAddressCheckbox.isChecked

        if (fullName.isNotBlank() && streetAddress.isNotBlank() && city.isNotBlank()) {
            val newAddress = AddressItem(
                id = addressId ?: ((viewModel.addresses.value?.size ?: 0) + 1),
                fullName = fullName,
                streetAddress = streetAddress,
                apt = apt,
                city = city,
                country = country,
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