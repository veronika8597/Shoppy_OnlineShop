package com.example.shoppy_onlineshop.ui.bag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R

import com.example.shoppy_onlineshop.databinding.FragmentBagBinding
import com.example.shoppy_onlineshop.helpers.setupSwipeToDelete
import com.google.firebase.auth.FirebaseAuth

class BagFragment : Fragment() {

    private val bagViewModel: BagViewModel by activityViewModels()
    private lateinit var bagAdapter: BagAdapter
    private lateinit var currentUserID: String

    private var _binding: FragmentBagBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBagBinding.inflate(inflater, container, false)

        currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        bagViewModel.loadBagProducts(currentUserID)

        // Setup RecyclerView
        binding.bagRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        bagAdapter = BagAdapter(emptyList(),
            onIncreaseQuantity = { productId ->
                bagViewModel.increaseQuantity(currentUserID, productId)
            },
            onDecreaseQuantity = { productId ->
                bagViewModel.decreaseQuantity(currentUserID, productId)
            })
        binding.bagRecyclerView.adapter = bagAdapter

        bagViewModel.bagItems.observe(viewLifecycleOwner) { cartItems ->
            bagAdapter.updateItems(cartItems)
            updateUI(cartItems)
        }

        setupSwipeToDelete(binding.bagRecyclerView) { position ->
            if (position in 0 until bagAdapter.itemCount) {
                val deletedItem = bagAdapter.getItem(position)
                bagViewModel.removeProductFromBag(currentUserID, deletedItem.product.id)
            }
        }

        return binding.root
    }

    private fun updateUI(items: List<BagItem>) {
        if (items.isEmpty()) {
            binding.bagRecyclerView.visibility = View.GONE
            binding.priceSummaryLayout.visibility = View.GONE
            binding.emptyBagTextView.visibility = View.VISIBLE
            binding.bagimageView.visibility = View.VISIBLE
        } else {
            binding.bagRecyclerView.visibility = View.VISIBLE
            binding.priceSummaryLayout.visibility = View.VISIBLE
            binding.emptyBagTextView.visibility = View.GONE
            binding.bagimageView.visibility = View.GONE

            val subtotal = items.sumOf { it.product.price * it.quantity }
            val shipping = if (subtotal >= 100.0) 0.0 else 9.99
            val total = subtotal + shipping

            binding.priceSubtotal.text = "Subtotal: $%.2f".format(subtotal)
            binding.priceShipping.text = "Shipping: $%.2f".format(shipping)
            binding.priceTotal.text = "Total: $%.2f".format(total)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
