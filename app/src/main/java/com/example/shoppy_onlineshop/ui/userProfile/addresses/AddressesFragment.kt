package com.example.shoppy_onlineshop.ui.userProfile.addresses

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R

class    AddressesFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        viewModel.loadAddresses() // Force LiveData to reload from SharedPreferences
    }

    private val viewModel: AddressesViewModel by viewModels()
    private lateinit var adapter: AddressesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_addresses, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.userAddresses_recyclerView)
        val addButton: Button = view.findViewById(R.id.addAddress_button)

        adapter = AddressesAdapter(emptyList()) { addressItem ->
            val bundle = Bundle().apply { putInt("addressId", addressItem.id) }
            findNavController().navigate(R.id.action_addressesFragment_to_editAddressFragment, bundle)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe LiveData and update adapter
        viewModel.addresses.observe(viewLifecycleOwner) { addresses ->
            adapter.updateData(addresses)
            adapter.notifyDataSetChanged()
        }

        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_addressesFragment_to_editAddressFragment)
        }

        return view
    }
}