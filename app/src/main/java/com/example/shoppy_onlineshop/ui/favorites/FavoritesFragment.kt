package com.example.shoppy_onlineshop.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.api.StoreProduct
import com.example.shoppy_onlineshop.databinding.FragmentFavoritesBinding
import com.example.shoppy_onlineshop.helpers.setupSwipeToDelete
import com.example.shoppy_onlineshop.ui.bag.BagViewModel
import com.google.firebase.auth.FirebaseAuth

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by activityViewModels()

    private val bagViewModel: BagViewModel by activityViewModels()

    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var currentUserID: String
    private lateinit var product: StoreProduct


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        viewModel.loadFavoriteProducts(currentUserID)

        // Setup RecyclerView
        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        favoritesAdapter = FavoritesAdapter(emptyList()){ product ->
            bagViewModel.addToBag(
                currentUserID,
                product,
                onSuccess = {
                    Log.d("FavoritesFragment", "Product added to bag successfully")
                },
                onFailure = { exception ->
                    Log.e("FavoritesFragment", "Failed to add product to bag: ${exception.message}")
                }

            )
        }
        binding.favoritesRecyclerView.adapter = favoritesAdapter

        // Observe LiveData
        observeFavoriteProducts()

        setupSwipeToDelete(binding.favoritesRecyclerView) { position ->
            val deletedItem = favoritesAdapter.getItem(position)
            viewModel.removeProductFromFavorites(currentUserID, deletedItem)
        }

        return binding.root
    }

    private fun observeFavoriteProducts() {
        viewModel.favoriteItems.observe(viewLifecycleOwner) { products ->
            if (products.isNotEmpty()) {
                favoritesAdapter.updateFavoriteProducts(products)
                binding.favoritesRecyclerView.visibility = View.VISIBLE
                binding.emptyFavoritesTextView.visibility = View.GONE
                binding.favoritesimageView.visibility = View.GONE
            } else {
                binding.favoritesRecyclerView.visibility = View.GONE
                binding.emptyFavoritesTextView.visibility = View.VISIBLE
                binding.favoritesimageView.visibility = View.VISIBLE
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                // Handle error message display
                Log.e("FavoritesFragment", "Error: $error")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}