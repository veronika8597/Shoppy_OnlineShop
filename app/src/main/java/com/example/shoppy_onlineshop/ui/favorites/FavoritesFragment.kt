package com.example.shoppy_onlineshop.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppy_onlineshop.databinding.FragmentFavoritesBinding
import com.google.firebase.auth.FirebaseAuth

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by activityViewModels()
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        // Setup RecyclerView
        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        favoritesAdapter = FavoritesAdapter(emptyList())
        binding.favoritesRecyclerView.adapter = favoritesAdapter

        // Observe LiveData
        observeFavoriteProducts()

        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        viewModel.loadFavoriteProducts(currentUserID)

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