package com.example.shoppy_onlineshop.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        viewModel.loadFavoriteProducts(currentUserID)
        bagViewModel.loadBagProducts(currentUserID)

        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        favoritesAdapter = FavoritesAdapter(emptyList()) { product ->
            val localBag = bagViewModel.bagItems.value.orEmpty()
            val isInBag = localBag.any { it.product.id == product.id }

            if (isInBag) {
                Toast.makeText(requireContext(), "Product is already in bag", Toast.LENGTH_SHORT).show()
            } else {
                // Fallback check from Firebase if local is null or outdated
                bagViewModel.isProductInBag(currentUserID, product) { result ->
                    if (result) {
                        Toast.makeText(
                            requireContext(),
                            "Product is already in bag",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        bagViewModel.addToBag(
                            currentUserID,
                            product,
                            onSuccess = {
                                Toast.makeText(
                                    requireContext(),
                                    "Product added to bag",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onFailure = {
                                Toast.makeText(
                                    requireContext(),
                                    "Failed to add product to bag",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                    }
                }
            }
        }

        binding.favoritesRecyclerView.adapter = favoritesAdapter

        observeFavoriteProducts()

        setupSwipeToDelete(binding.favoritesRecyclerView) { position ->
            val deletedItem = favoritesAdapter.getItem(position)
            viewModel.removeProductFromFavorites(currentUserID, deletedItem)
        }
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