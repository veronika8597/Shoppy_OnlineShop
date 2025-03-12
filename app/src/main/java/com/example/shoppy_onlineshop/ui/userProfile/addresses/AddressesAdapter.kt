package com.example.shoppy_onlineshop.ui.userProfile.addresses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R

class AddressesAdapter(private var addressList: List<AddressItem>, private val onItemClick: (AddressItem) -> Unit ) : RecyclerView.Adapter<AddressesAdapter.AddressViewHolder>() {

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

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val addressItem = addressList[position]
        holder.fullName.text = addressItem.fullName
        holder.address.text = addressItem.streetAddress
        holder.apt.text = addressItem.apt ?: "N/A"
        holder.city.text = addressItem.city
        holder.zipCode.text = addressItem.zip?.toString() ?: "N/A"

        holder.status.visibility = if (addressItem.isDefault) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener { onItemClick(addressItem) }
    }

    fun updateData(newList: List<AddressItem>) {
        addressList = newList
        notifyDataSetChanged()
    }
}