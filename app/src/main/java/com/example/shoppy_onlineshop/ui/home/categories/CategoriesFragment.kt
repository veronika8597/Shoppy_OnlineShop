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
    private var categories: List<StoreCategory>? = null
    private val viewModel: CategoriesViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter
    private var categorySlug: String? = null
    private var categoryName: String? = null


    companion object {
        fun newInstance() = CategoriesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)

        // Retrieve categories from arguments
        categories = arguments?.getParcelableArrayList("categories")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categories?.let {
            val categoryAdapter = CategoryAdapter(it, this)
            binding.all.layoutManager = GridLayoutManager(context,2)
            binding.all.adapter = categoryAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCategoryClick(category: StoreCategory) {
        //Pass the category data to the ProductFragment
        val bundle = Bundle().apply{
            putString("categorySlug", category.slug) //Pass the slug
        }
        //Navigate to the ProductsFragment wiith the category's data
        findNavController().navigate(R.id.action_categoriesFragment_to_productsFragment, bundle)

    }
}