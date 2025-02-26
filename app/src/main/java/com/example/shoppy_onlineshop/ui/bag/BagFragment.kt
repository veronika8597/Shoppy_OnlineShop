package com.example.shoppy_onlineshop.ui.bag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.api.StoreProduct
import com.example.shoppy_onlineshop.databinding.FragmentBagBinding

class BagFragment : Fragment() {

    private lateinit var bagViewModel: BagViewModel
    private lateinit var adapter: BagAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyBagTextView: TextView
    private lateinit var emptyBagImageView: ImageView

    private var _binding: FragmentBagBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_bag, container, false)

        recyclerView = view.findViewById(R.id.bagRecyclerView)
        emptyBagTextView = view.findViewById(R.id.emptyBagTextView)
        emptyBagImageView = view.findViewById(R.id.bagimageView)

        adapter = BagAdapter(emptyList()) //Empty at first
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        bagViewModel = ViewModelProvider(this).get(BagViewModel::class.java)
        bagViewModel.bagItems.observe(viewLifecycleOwner) { products ->
            adapter.updateProducts(products)
            updateUI(products)
        }

        return view

    }

    private fun updateUI(products: List<StoreProduct>) {
        if (products.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyBagTextView.visibility = View.VISIBLE
            emptyBagImageView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyBagTextView.visibility = View.GONE
            emptyBagImageView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}