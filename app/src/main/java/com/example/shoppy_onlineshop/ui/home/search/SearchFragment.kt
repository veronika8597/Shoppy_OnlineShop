package com.example.shoppy_onlineshop.ui.home.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.api.StoreProduct
import com.example.shoppy_onlineshop.databinding.FragmentSearchBinding
import com.example.shoppy_onlineshop.ui.home.products.ProductAdapter
import com.example.shoppy_onlineshop.ui.home.products.ProductClickListener
import com.google.firebase.auth.FirebaseAuth

class SearchFragment : Fragment(), ProductClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private lateinit var viewModel: SearchViewModel
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        // Set up recycler
        productAdapter = ProductAdapter(emptyList(), this, currentUserId)
        binding.searchResultsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.searchResultsRecyclerView.adapter = productAdapter

        // Observe API result
        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            binding.searchShimmerContainer.stopShimmer()
            binding.searchShimmerContainer.visibility = View.GONE
            binding.searchResultsRecyclerView.visibility = View.VISIBLE
            productAdapter.updateData(results)
        }

        // Setup search logic
        binding.searchInput.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let {
                    if (it.length >= 2) {
                        binding.searchShimmerContainer.visibility = View.VISIBLE
                        binding.searchResultsRecyclerView.visibility = View.GONE
                        binding.searchShimmerContainer.startShimmer()
                        viewModel.searchProducts(it)
                    }
                }
                return true
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onProductClick(product: StoreProduct) {
        val bundle = Bundle().apply {
            putInt("productId", product.id)
        }
        findNavController().navigate(R.id.action_searchFragment_to_productDetailsFragment, bundle)
    }
}
