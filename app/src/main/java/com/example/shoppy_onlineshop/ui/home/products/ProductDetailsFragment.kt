package com.example.shoppy_onlineshop.ui.home.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.api.Review
import com.example.shoppy_onlineshop.api.StoreProduct
import com.example.shoppy_onlineshop.databinding.FragmentProductDetailsBinding
import com.example.shoppy_onlineshop.helpers.isProductInFavorites
import com.example.shoppy_onlineshop.helpers.toggleSectionVisibility
import com.example.shoppy_onlineshop.ui.favorites.FavoritesViewModel
import com.google.firebase.auth.FirebaseAuth

class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding should not be null")

    companion object {
        private const val PRODUCT_ID_KEY = "productId"

        fun newInstance(productId: Int) = ProductDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(PRODUCT_ID_KEY, productId)
            }
        }
    }

    private val favoritesViewModel: FavoritesViewModel by activityViewModels()
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productID = arguments?.getInt(PRODUCT_ID_KEY)
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid

        if (productID == null || productID <= 0) {
            Toast.makeText(requireContext(), "Product not found", Toast.LENGTH_SHORT).show()
            return
        }

        favoritesViewModel.loadFavoriteProducts(currentUserID.toString())
        productDetailsViewModel.loadProductDetails(
            context = requireContext(),
            productID = productID,
            onSuccess = { product ->
                val currentProduct: StoreProduct = product

                if (currentUserID != null) {
                    isProductInFavorites(currentUserID, currentProduct) { isFavorite ->
                        // Handle the favorite button visibility
                        binding.favoriteButtonFilledHeart.visibility = if (isFavorite) View.VISIBLE else View.GONE
                        binding.favoriteButtonEmptyHeart.visibility = if (!isFavorite) View.VISIBLE else View.GONE

                        // Set the onClick listener for the favorite button
                        val favoriteClickListener = View.OnClickListener {
                            val newFavoriteStatus = favoritesViewModel.toggleFavoriteStatus(
                                currentUserID,
                                binding.favoriteButtonEmptyHeart,
                                binding.favoriteButtonFilledHeart,
                                currentProduct,
                                isFavorite
                            )
                        }
                        // Set the click listener for both buttons
                        binding.favoriteButtonEmptyHeart.setOnClickListener(favoriteClickListener)
                        binding.favoriteButtonFilledHeart.setOnClickListener(favoriteClickListener)
                    }
                }

                // Set product data
                binding.productTitle.text = currentProduct.title
                binding.productBrand.text = currentProduct.brand
                binding.productDescription.text = currentProduct.description
                binding.productPrice.text = "$${currentProduct.price}"
                binding.productRating.text = "Rating: ${currentProduct.rating}"
                setTags(currentProduct.tags)
                Glide.with(requireContext())
                    .load(currentProduct.thumbnail)
                    .transform(CenterCrop(), RoundedCorners(16))
                    .into(binding.productImage)
                setupReviewsRecyclerView(currentProduct.reviews)
            },
            onFailure = {
                Toast.makeText(requireContext(), "Failed to load product details", Toast.LENGTH_SHORT).show()
            }
        )

        // Set up section visibility toggles
        binding.reviewsTitle.setOnClickListener {
            toggleSectionVisibility(
                binding.reviewsRecyclerView,
                binding.reviewsTitle,
                "Reviews -",
                "Reviews +"
            )
        }

        binding.itemDetailsHeader.setOnClickListener {
            toggleSectionVisibility(
                binding.itemDetailsSection,
                binding.itemDetailsHeader,
                "Item Details -",
                "Item Details +"
            )
        }

        binding.shippingInformation.setOnClickListener {
            toggleSectionVisibility(
                binding.shippingInformationSection,
                binding.shippingInformation,
                "Shipping Information -",
                "Shipping Information +"
            )
        }

        binding.returnPolicy.setOnClickListener {
            toggleSectionVisibility(
                binding.returnPolicySection,
                binding.returnPolicy,
                "Return Policy -",
                "Return Policy +"
            )
        }
    }

    private fun setTags(tags: List<String>) {
        // List of tag views
        val tagViews = listOf(binding.tag1, binding.tag2, binding.tag3)

        // Hide all tags initially
        tagViews.forEach { it.visibility = View.GONE }

        // Set tags based on availability
        tags.forEachIndexed { index, tag ->
            tagViews.getOrNull(index)?.let {
                it.text = tag
                it.visibility = View.VISIBLE
            }
        }
    }

    private fun setupReviewsRecyclerView(reviews: List<Review>) {
        // Set up RecyclerView to display reviews
        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.reviewsRecyclerView.adapter = ReviewAdapter(reviews)
        binding.reviewsRecyclerView.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks by clearing binding reference
    }
}
