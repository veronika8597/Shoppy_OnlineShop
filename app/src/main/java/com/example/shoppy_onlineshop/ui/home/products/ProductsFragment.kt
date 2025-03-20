package com.example.shoppy_onlineshop.ui.home.products

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
import com.example.shoppy_onlineshop.api.StoreProduct
import com.example.shoppy_onlineshop.databinding.FragmentProductsBinding
import com.example.shoppy_onlineshop.helpers.ProductLoader.loadProductsForCategory

class ProductsFragment : Fragment(), ProductClickListener {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private var products: List<StoreProduct>? = null

    companion object {
        fun newInstance() = ProductsFragment()
    }

    private val viewModel: ProductsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)

        // Retrieve categorySlug passed from CategoriesFragment
        //val categorySlug = arguments?.getString("categorySlug") ?: return binding.root
        val categorySlug = arguments?.getString("categorySlug")
        if (categorySlug == null) {
            Toast.makeText(requireContext(), "Category not found", Toast.LENGTH_SHORT).show()
            return binding.root
        }
        Log.d("ProductsFragment", "Category Slug: $categorySlug")

        // Load products based on the categorySlug
        loadProductsForCategory(
            context = requireContext(),
            categorySlug = categorySlug,
            onSuccess = { productList ->
                products = productList
                setUpRecyclerView()  // Update the RecyclerView with the fetched products
            },
            onFailure = { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecyclerView() {
        // Ensure the RecyclerView is updated with products
        if (products.isNullOrEmpty()) {
            Toast.makeText(context, "No products available", Toast.LENGTH_SHORT).show()
        }

        val productAdapter = ProductAdapter(products ?: emptyList(), this)
        binding.productsRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.productsRecyclerView.adapter = productAdapter
    }

    override fun onProductClick(product: StoreProduct) {
        //Pass the category data to the ProductFragment
        val bundle = Bundle().apply{
            putInt("productId", product.id) //Pass the product ID
        }
        //Navigate to the ProductsFragment with the category's data
        findNavController().navigate(R.id.action_productsFragment_to_productDetailsFragment, bundle)

    }
}
