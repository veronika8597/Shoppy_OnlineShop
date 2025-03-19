package com.example.shoppy_onlineshop.ui.userProfile.paymentMethods

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R

class PaymentMethodsAdapter(private var paymentMethodsList: MutableList<PaymentMethodItem>, private val onItemClick: (PaymentMethodItem) -> Unit ) : RecyclerView.Adapter<PaymentMethodsAdapter.PaymentMethodViewHolder>() {

    class PaymentMethodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardNumber: TextView = view.findViewById(R.id.masked_card_number)
        val expiryDate: TextView = view.findViewById(R.id.expiry_date)
        val status: TextView = view.findViewById(R.id.default_payment_badge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.payment_method_row, parent, false)
        return PaymentMethodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return paymentMethodsList.size
    }

    fun getItem(position: Int): PaymentMethodItem {
        return paymentMethodsList[position]
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        val paymentMethodItem = getItem(position)

        holder.apply {
            cardNumber.text = maskCardNumber(paymentMethodItem.cardNumber)
            expiryDate.text = paymentMethodItem.expiryDate
            status.visibility = if (paymentMethodItem.isDefault) View.VISIBLE else View.GONE

            itemView.setOnClickListener { onItemClick(paymentMethodItem) }
        }
    }

    fun updateData(newList: List<PaymentMethodItem>) {
        paymentMethodsList.clear()
        paymentMethodsList.addAll(newList)
        notifyDataSetChanged()
    }

    fun maskCardNumber(cardNumber: String): String {
        return if(!cardNumber.isNullOrEmpty() && cardNumber.length >= 15){
            "**** **** **** " + cardNumber.takeLast(4)
        }
        else {
            "Invalid Card Number"
        }

    }

}