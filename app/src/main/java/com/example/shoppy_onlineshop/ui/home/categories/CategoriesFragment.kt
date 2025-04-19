package com.example.shoppy_onlineshop.ui.home.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.api.StoreCategory
import com.example.shoppy_onlineshop.databinding.FragmentCategoriesBinding
import com.example.shoppy_onlineshop.ui.home.products.ProductAdapter

class CategoriesFragment : Fragment(), CategoryClickListener {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)

        // Show shimmer and hide recycler until loaded
        binding.categoryShimmerContainer.startShimmer()
        binding.categoriesRecyclerView.visibility = View.INVISIBLE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle tapping the search bar
        binding.searchBarCategories.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                findNavController().navigate(R.id.searchFragment)
            }
        }

        // Observe and populate category list
        viewModel.fetchCategories()
        viewModel.categories.observe(viewLifecycleOwner) { categoryList ->
            val adapter = CategoryAdapter(categoryList, this)
            binding.categoriesRecyclerView.layoutManager = GridLayoutManager(context, 2)
            binding.categoriesRecyclerView.adapter = adapter

            binding.categoryShimmerContainer.stopShimmer()
            binding.categoryShimmerContainer.visibility = View.GONE
            binding.categoriesRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun onCategoryClick(category: StoreCategory) {
        val bundle = Bundle().apply {
            putString("categorySlug", category.slug)
        }
        findNavController().navigate(R.id.action_categoriesFragment_to_productsFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}