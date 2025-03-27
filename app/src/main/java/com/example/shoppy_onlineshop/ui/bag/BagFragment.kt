package com.example.shoppy_onlineshop.ui.bag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R

import com.example.shoppy_onlineshop.databinding.FragmentBagBinding

class BagFragment() : Fragment() {

    private lateinit var bagViewModel: BagViewModel
    private lateinit var adapter: BagAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyBagTextView: TextView
    private lateinit var emptyBagImageView: ImageView
    private lateinit var subtotalTextView: TextView
    private lateinit var shippingTextView: TextView
    private lateinit var totalTextView: TextView
    private lateinit var priceSummaryLayout: View


    private var _binding: FragmentBagBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_bag, container, false)

        recyclerView = view.findViewById(R.id.bagRecyclerView)
        emptyBagTextView = view.findViewById(R.id.emptyBagTextView)
        emptyBagImageView = view.findViewById(R.id.bagimageView)
        subtotalTextView = view.findViewById(R.id.priceSubtotal)
        shippingTextView = view.findViewById(R.id.priceShipping)
        totalTextView = view.findViewById(R.id.priceTotal)
        priceSummaryLayout = view.findViewById(R.id.priceSummaryLayout)

        adapter = BagAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        bagViewModel = ViewModelProvider(this)[BagViewModel::class.java]
        bagViewModel.bagItems.observe(viewLifecycleOwner) { cartItems ->
            adapter.updateItems(cartItems)
            updateUI(cartItems)
        }

        return view
    }

    private fun updateUI(items: List<CartItem>) {
        if (items.isEmpty()) {
            recyclerView.visibility = View.GONE
            priceSummaryLayout.visibility = View.GONE
            emptyBagTextView.visibility = View.VISIBLE
            emptyBagImageView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            priceSummaryLayout.visibility = View.VISIBLE
            emptyBagTextView.visibility = View.GONE
            emptyBagImageView.visibility = View.GONE

            val subtotal = items.sumOf { it.product.price * it.quantity }
            val shipping = if (subtotal >= 100.0) 0.0 else 9.99
            val total = subtotal + shipping

            subtotalTextView.text = "Subtotal: $%.2f".format(subtotal)
            shippingTextView.text = "Shipping: $%.2f".format(shipping)
            totalTextView.text = "Total: $%.2f".format(total)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
