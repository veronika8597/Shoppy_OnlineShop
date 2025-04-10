package com.example.shoppy_onlineshop.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.api.StoreCategory
import com.example.shoppy_onlineshop.api.StoreProduct
import com.example.shoppy_onlineshop.databinding.FragmentHomeBinding
import com.example.shoppy_onlineshop.ui.home.categories.CategoryAdapter
import com.example.shoppy_onlineshop.ui.home.categories.CategoryClickListener
import com.example.shoppy_onlineshop.ui.home.products.ProductAdapter
import com.example.shoppy_onlineshop.ui.home.products.ProductClickListener
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(), CategoryClickListener, ProductClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private var categories: List<StoreCategory> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]



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

        val shimmerContainer = binding.homeShimmerContainer1
        categoriesRecyclerView.visibility = View.INVISIBLE
        shimmerContainer.startShimmer()


        // Initialize your categories list here (e.g., from an API call)
        categories = listOf(
            StoreCategory("beauty", "Beauty", "https://dummyjson.com/products/category/beauty"),
            StoreCategory("fragrances", "Fragrances", "https://dummyjson.com/products/category/fragrances"),
            StoreCategory("skin-care", "Skin Care", "https://dummyjson.com/products/category/skin-care"),
            StoreCategory("groceries", "Groceries", "https://dummyjson.com/products/category/groceries"),
            StoreCategory("home-decoration", "Home Decoration", "https://dummyjson.com/products/category/home-decoration"),
            StoreCategory("furniture", "Furniture", "https://dummyjson.com/products/category/furniture"),
            StoreCategory("tops", "Tops", "https://dummyjson.com/products/category/tops"),
            StoreCategory("womens-dresses", "Womens Dresses", "https://dummyjson.com/products/category/womens-dresses"),
            StoreCategory("womens-shoes", "Womens Shoes", "https://dummyjson.com/products/category/womens-shoes"),
            StoreCategory("mens-shirts", "Mens Shirts", "https://dummyjson.com/products/category/mens-shirts"),
            StoreCategory("mens-shoes", "Mens Shoes", "https://dummyjson.com/products/category/mens-shoes"),
            StoreCategory("mens-watches", "Mens Watches", "https://dummyjson.com/products/category/mens-watches"),
            StoreCategory("womens-watches", "Womens Watches", "https://dummyjson.com/products/category/womens-watches"),
            StoreCategory("womens-bags", "Womens Bags", "https://dummyjson.com/products/category/womens-bags"),
            StoreCategory("womens-jewellery", "Womens Jewellery", "https://dummyjson.com/products/category/womens-jewellery"),
            StoreCategory("sunglasses", "Sunglasses", "https://dummyjson.com/products/category/sunglasses"),
            StoreCategory("vehicle", "Vehicle", "https://dummyjson.com/products/category/vehicle"),
            StoreCategory("motorcycle", "Motorcycle", "https://dummyjson.com/products/category/motorcycle"),
            StoreCategory("lighting", "Lighting", "https://dummyjson.com/products/category/lighting"),
            StoreCategory("kitchen-accessories", "Kitchen Accessories", "https://dummyjson.com/products/category/kitchen-accessories"),
            StoreCategory("laptops", "Laptops", "https://dummyjson.com/products/category/laptops"),
            StoreCategory("mobile-accessories", "Mobile Accessories", "https://dummyjson.com/products/category/mobile-accessories"),
            StoreCategory("smartphones", "Smartphones", "https://dummyjson.com/products/category/smartphones"),
            StoreCategory("sports-accessories", "Sports Accessories", "https://dummyjson.com/products/category/sports-accessories"),
            StoreCategory("tablets", "Tablets", "https://dummyjson.com/products/category/tablets")
        )

        categoryAdapter = CategoryAdapter(emptyList(), this)
        categoriesRecyclerView.adapter = categoryAdapter

        homeViewModel.featuredCategories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.updateData(categories)

            // Delay just enough to allow Glide to begin loading (but not fully finish if slow)
            binding.FeaturedItemsHorizontal.postDelayed({
                binding.homeShimmerContainer1.stopShimmer()
                binding.homeShimmerContainer1.visibility = View.GONE
                binding.FeaturedItemsHorizontal.visibility = View.VISIBLE
            }, 400)
        }

        //Recommended Products RecyclerView
        val productsRecyclerView: RecyclerView = binding.RecommendedItemsVerticalRecyclerView
        productsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2) // 2 columns

        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        productAdapter = ProductAdapter(emptyList(), this, currentUserID)
        productsRecyclerView.adapter = productAdapter

        homeViewModel.allProducts.observe(viewLifecycleOwner) { products ->
            productAdapter.updateData(products)

            // Delay just enough to allow Glide to begin loading (but not fully finish if slow)
            binding.RecommendedItemsVerticalRecyclerView.postDelayed({
                binding.homeShimmerContainer2.stopShimmer()
                binding.homeShimmerContainer2.visibility = View.GONE
                binding.RecommendedItemsVerticalRecyclerView.visibility = View.VISIBLE
            },400)
        }


        //All categories button
        val allCategoriesButton: Button = binding.categoriesButton

        allCategoriesButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_categories)

        }

        //Profile button
        val profileButton: ImageButton = binding.profileButton
        profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_user_profile)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //All categories button setup
        val allCategoriesButton: Button = binding.categoriesButton
        allCategoriesButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelableArrayList(
                "categories",
                homeViewModel.allCategories.value?.let { it1 -> ArrayList(it1) }
            )
            findNavController().navigate(R.id.action_home_to_categories, bundle)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCategoryClick(category: StoreCategory) {
        val bundle = Bundle().apply {
            putString("categorySlug", category.slug)
        }
        findNavController().navigate(R.id.action_navigation_home_to_productsFragment, bundle)
    }



    override fun onProductClick(product: StoreProduct) {
        //Pass the category data to the ProductFragment
        val bundle = Bundle().apply{
            putInt("productId", product.id) //Pass the slug
        }

        //Navigate to the ProductsFragment with the category's data
        findNavController().navigate(R.id.action_navigation_home_to_productDetailsFragment, bundle)
    }
}