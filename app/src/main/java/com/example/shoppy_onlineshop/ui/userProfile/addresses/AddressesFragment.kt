package com.example.shoppy_onlineshop.ui.userProfile.addresses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.helpers.setupSwipeToDelete

class AddressesFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        viewModel.loadAddresses() // Force LiveData to reload from SharedPreferences
    }

    private val viewModel: AddressesViewModel by activityViewModels()
    private lateinit var adapter: AddressesAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_addresses, container, false)
        recyclerView = view.findViewById(R.id.userAddresses_recyclerView)
        val addButton: Button = view.findViewById(R.id.addAddress_button)

        adapter = AddressesAdapter(mutableListOf<AddressItem>()) { addressItem ->
/*            val bundle = Bundle().apply { putInt("addressId", addressItem.id) }
            findNavController().navigate(R.id.action_addressesFragment_to_editAddressFragment, bundle)*/

            val sheet = EditAddressBottomSheet()
            sheet.arguments = bundleOf("addressId" to addressItem.id)
            sheet.show(parentFragmentManager, "EditAddressBottomSheet")
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe LiveData and update adapter
        viewModel.addresses.observe(viewLifecycleOwner) { addresses ->
            adapter.updateData(addresses)
        }

        addButton.setOnClickListener {
            //findNavController().navigate(R.id.action_addressesFragment_to_editAddressFragment)
            EditAddressBottomSheet().show(parentFragmentManager, "EditAddressBottomSheet")

        }

        setupSwipeToDelete(recyclerView){ position ->
            val deletedItem = adapter.getItem(position)
            viewModel.deleteAddress(deletedItem)
        }

        return view
    }

}