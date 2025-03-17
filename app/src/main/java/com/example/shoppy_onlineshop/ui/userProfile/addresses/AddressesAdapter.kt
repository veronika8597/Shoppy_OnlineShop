package com.example.shoppy_onlineshop.ui.userProfile.addresses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R

class AddressesAdapter(private var addressList: MutableList<AddressItem>, private val onItemClick: (AddressItem) -> Unit ) : RecyclerView.Adapter<AddressesAdapter.AddressViewHolder>() {

    class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val fullName: TextView = view.findViewById(R.id.fullName_editText)
        val address: TextView = view.findViewById(R.id.street_editText)
        val apt: TextView = view.findViewById(R.id.apt_editText)
        val city: TextView = view.findViewById(R.id.city_editText)
        val zipCode: TextView = view.findViewById(R.id.postalCode_editText)
        val status: TextView = view.findViewById(R.id.status_textView)
        //val country: TextView = view.findViewById(R.id.country_editText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.address_row, parent, false)
        return AddressViewHolder(view)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    fun getItem(position: Int): AddressItem {
        return addressList[position]
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val addressItem = getItem(position)

        holder.apply {
            fullName.text = addressItem.fullName
            address.text = addressItem.streetAddress
            apt.text = addressItem.apt ?: "N/A"
            city.text = addressItem.city
            zipCode.text = addressItem.zip?.toString() ?: "N/A"

            status.visibility = if (addressItem.isDefault) View.VISIBLE else View.GONE

            itemView.setOnClickListener { onItemClick(addressItem) }
        }
    }

    fun updateData(newList: List<AddressItem>) {
        addressList.clear()
        addressList.addAll(newList)
        notifyDataSetChanged()
    }
}
