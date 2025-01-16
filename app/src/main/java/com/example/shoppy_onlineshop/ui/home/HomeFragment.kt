package com.example.shoppy_onlineshop.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.databinding.FragmentHomeBinding
import com.example.shoppy_onlineshop.ui.home.adapter.CategoryAdapter
import com.example.shoppy_onlineshop.ui.home.adapter.ProductAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Explore title
        val titleTextView: TextView = binding.ExploreTitleTextView
        homeViewModel.exploreTitle.observe(viewLifecycleOwner) {
            titleTextView.text = it
        }

        //Featured Categories RecyclerView
        val categoriesRecyclerView: RecyclerView = binding.FeaturedItemsHorizontal
        categoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter = CategoryAdapter(emptyList())
        categoriesRecyclerView.adapter = categoryAdapter

        homeViewModel.featuredCategories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.updateData(categories)
        }

        //Recommended Products RecyclerView
        val productsRecyclerView: RecyclerView = binding.RecomendedItemsVerticalRecyclerView
        productsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        productAdapter = ProductAdapter(emptyList())
        productsRecyclerView.adapter = productAdapter

        homeViewModel.allProducts.observe(viewLifecycleOwner) { products ->
            productAdapter.updateData(products)
        }

        //Search Bar
        val searchBar: SearchView = binding.searchBarHome
        searchBar.queryHint = "Search on Shoppy"
        binding.searchBarHome.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                // Handle search query text changes
                return true
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}