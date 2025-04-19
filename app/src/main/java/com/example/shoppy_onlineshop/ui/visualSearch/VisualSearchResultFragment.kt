package com.example.shoppy_onlineshop.ui.visualSearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.databinding.FragmentVisualSearchResultBinding

class VisualSearchResultFragment : Fragment() {
    private var _binding: FragmentVisualSearchResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VisualSearchAdapter
    private lateinit var viewModel: VisualSearchViewModel

    private val labels: Array<String> by lazy {
        arguments?.getStringArray("labels") ?: emptyArray()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisualSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("RESULT_DEBUG", "Received labels: ${labels.joinToString()}")

        adapter = VisualSearchAdapter { selectedProduct ->
            Log.d("PRODUCT_CLICK", "Clicked: ${selectedProduct.title}")

            val bundle = Bundle().apply {
                putInt("productId", selectedProduct.id)
            }
            findNavController().navigate(
                R.id.action_visualSearchResultFragment_to_productDetailsFragment,
                bundle
            )
        }

        binding.recyclerViewResults.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewResults.adapter = adapter

        viewModel = ViewModelProvider(this)[VisualSearchViewModel::class.java]

        val topLabel = labels.firstOrNull()
        if (topLabel != null) {
            viewModel.getCategories { categories ->
                val matchedCategory = categories.firstOrNull { category ->
                    category.name.equals(topLabel, ignoreCase = true)
                }

                if (matchedCategory != null) {
                    Log.d("CATEGORY_MATCH", "Found category match: ${matchedCategory.name}")
                    viewModel.fetchProductsForCategory(matchedCategory.slug)
                } else {
                    Log.d("SEARCH_DEBUG", "No category match, using label for search: $topLabel")
                    viewModel.searchProducts(topLabel)
                }
            }
        } else {
            Log.d("SEARCH_DEBUG", "No labels detected, fallback to full product list")
            viewModel.fetchProducts()
        }

        viewModel.products.observe(viewLifecycleOwner) { results ->
            if (results.isEmpty()) {
                binding.emptyMessage.visibility = View.VISIBLE
            } else {
                binding.emptyMessage.visibility = View.GONE
            }
            adapter.updateData(results)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

