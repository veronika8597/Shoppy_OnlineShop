package com.example.shoppy_onlineshop.ui.home.products

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.example.shoppy_onlineshop.helpers.toggleFavoriteStatus
import com.example.shoppy_onlineshop.helpers.toggleSectionVisibility
import com.example.shoppy_onlineshop.ui.bag.BagViewModel
import com.example.shoppy_onlineshop.ui.favorites.FavoritesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding should not be null")

    private val bagViewModel: BagViewModel by activityViewModels()
    private val favoritesViewModel: FavoritesViewModel by activityViewModels()
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()

    private val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var currentProduct: StoreProduct
    private var isFavorite = false

    companion object {
        private const val PRODUCT_ID_KEY = "productId"

        fun newInstance(productId: Int) = ProductDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(PRODUCT_ID_KEY, productId)
            }
        }
    }

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
        if (productID == null || productID <= 0) {
            Toast.makeText(requireContext(), "Product not found", Toast.LENGTH_SHORT).show()
            return
        }

        favoritesViewModel.loadFavoriteProducts(currentUserID)

        showShimmer()

        productDetailsViewModel.loadProductDetails(
            context = requireContext(),
            productID = productID,
            onSuccess = { product ->
                Handler(Looper.getMainLooper()).postDelayed({
                    hideShimmer()
                    bindProductDetails(product)
                }, 250)
            },
            onFailure = {
                hideShimmer()
                Toast.makeText(requireContext(), "Failed to load product", Toast.LENGTH_SHORT).show()
            }
        )

        setupSectionToggles()
        setupAddToBag()
    }

    private fun showShimmer() {
        binding.shimmerViewContainer.visibility = View.VISIBLE
        binding.shimmerViewContainer.startShimmer()
        binding.productContentLayout.visibility = View.GONE
    }

    private fun hideShimmer() {
        binding.shimmerViewContainer.stopShimmer()
        binding.shimmerViewContainer.visibility = View.GONE
        binding.productContentLayout.visibility = View.VISIBLE
        binding.productCardView.visibility = View.VISIBLE
    }

    private fun bindProductDetails(product: StoreProduct) {
        currentProduct = product
        binding.productTitle.text = product.title
        binding.productBrand.text = product.brand
        binding.productDescription.text = product.description
        binding.productPrice.text = "$${product.price}"
        binding.productRating.text = "Rating: ${product.rating}"
        setTags(product.tags)

        Glide.with(requireContext())
            .load(product.thumbnail)
            .transform(CenterCrop(), RoundedCorners(16))
            .into(binding.productImage)

        //setupReviewsRecyclerView(product.reviews)
        productDetailsViewModel.fetchFirebaseReviews(product.id) { firebaseReviews ->
            val allReviews = product.reviews + firebaseReviews

            // 🧮 Calculate new average
            val totalRating = allReviews.sumOf { it.rating }
            val averageRating = if (allReviews.isNotEmpty()) totalRating.toDouble() / allReviews.size else 0.0

            // Update UI
            binding.productRating.text = "Rating: %.1f".format(averageRating)

            setupReviewsRecyclerView(allReviews)
        }

        binding.submitReviewButton.setOnClickListener {

            val comment = binding.reviewInput.text.toString().trim()
            val rating = binding.ratingBar.rating.toInt()

            if (comment.isEmpty()) {
                Toast.makeText(requireContext(), "Please write a review first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val reviewerName = FirebaseAuth.getInstance().currentUser?.displayName ?: "Anonymous"
            val reviewerEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val review = Review(
                rating = rating,
                comment = comment,
                date = date,
                reviewerName = reviewerName,
                reviewerEmail = reviewerEmail
            )

            val dbRef = FirebaseDatabase.getInstance().getReference("product_reviews")
            val reviewId = dbRef.child(product.id.toString()).push().key ?: UUID.randomUUID().toString()
            productDetailsViewModel.submitReviewToFirebase(
                productId = product.id,
                review = review,
                onSuccess = {
                    Toast.makeText(requireContext(), "Review submitted successfully!", Toast.LENGTH_SHORT).show()
                    binding.reviewInput.text.clear()

                    // 🔁 Re-fetch Firebase reviews and update the list
                    productDetailsViewModel.fetchFirebaseReviews(product.id) { updatedReviews ->
                        val allReviews = product.reviews + updatedReviews
                        // ✅ Recalculate average rating
                        val totalRating = allReviews.sumOf { it.rating }
                        val averageRating = if (allReviews.isNotEmpty()) totalRating.toDouble() / allReviews.size else 0.0

                        // ✅ Update UI
                        binding.productRating.text = "Rating: %.1f".format(averageRating)
                        //binding.productRatingStars.rating = averageRating.toFloat()

                        setupReviewsRecyclerView(allReviews)
                        binding.reviewsRecyclerView.visibility = View.VISIBLE
                    }
                },
                onFailure = {
                    Toast.makeText(requireContext(), "Failed to submit review", Toast.LENGTH_SHORT).show()
                }
            )

        }

        isProductInFavorites(currentUserID, product) { result ->
            isFavorite = result
            updateFavoriteButtonVisibility()

            val favoriteClickListener = View.OnClickListener {
                isFavorite = toggleFavoriteStatus(
                    currentUserID,
                    binding.favoriteButtonEmptyHeart,
                    binding.favoriteButtonFilledHeart,
                    product,
                    isFavorite
                )
                updateFavoriteButtonVisibility()
            }

            binding.favoriteButtonEmptyHeart.setOnClickListener(favoriteClickListener)
            binding.favoriteButtonFilledHeart.setOnClickListener(favoriteClickListener)
        }
    }

    private fun updateFavoriteButtonVisibility() {
        binding.favoriteButtonFilledHeart.visibility = if (isFavorite) View.VISIBLE else View.INVISIBLE
        binding.favoriteButtonEmptyHeart.visibility = if (!isFavorite) View.VISIBLE else View.INVISIBLE
        Log.d("ProductDetailsFragment", "Updated UI: isFavorite = $isFavorite")
    }

    private fun setTags(tags: List<String>) {
        val tagViews = listOf(binding.tag1, binding.tag2, binding.tag3)
        tagViews.forEach { it.visibility = View.GONE }
        tags.forEachIndexed { index, tag ->
            tagViews.getOrNull(index)?.let {
                it.text = tag
                it.visibility = View.VISIBLE
            }
        }
    }

    private fun setupReviewsRecyclerView(reviews: List<Review>) {
        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.reviewsRecyclerView.adapter = ReviewAdapter(reviews)
        binding.reviewsRecyclerView.visibility = View.GONE
    }

    private fun setupSectionToggles() {
        binding.reviewsTitle.setOnClickListener {
            toggleSectionVisibility(binding.reviewsRecyclerView, binding.reviewsTitle, "Reviews -", "Reviews +")
        }
        binding.addReviewToggle.setOnClickListener {
            toggleSectionVisibility(binding.addReviewForm, binding.addReviewToggle, "Add Review -", "Add Review +")
        }
        binding.itemDetailsHeader.setOnClickListener {
            toggleSectionVisibility(binding.itemDetailsSection, binding.itemDetailsHeader, "Item Details -", "Item Details +")
        }
        binding.shippingInformation.setOnClickListener {
            toggleSectionVisibility(binding.shippingInformationSection, binding.shippingInformation, "Shipping Information -", "Shipping Information +")
        }
        binding.returnPolicy.setOnClickListener {
            toggleSectionVisibility(binding.returnPolicySection, binding.returnPolicy, "Return Policy -", "Return Policy +")
        }
    }

    private fun setupAddToBag() {
        binding.addToBagButton.setOnClickListener {
            if (!::currentProduct.isInitialized) {
                Toast.makeText(requireContext(), "Please wait, loading product...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val localBag = bagViewModel.bagItems.value
            val isInBag = localBag?.any { it.product.id == currentProduct.id } == true

            if (isInBag) {
                Toast.makeText(requireContext(), "Product is already in bag", Toast.LENGTH_SHORT).show()
            } else {
                bagViewModel.isProductInBag(currentUserID, currentProduct) { result ->
                    if (result) {
                        Toast.makeText(requireContext(), "Product is already in bag", Toast.LENGTH_SHORT).show()
                    } else {
                        bagViewModel.addToBag(
                            currentUserID,
                            currentProduct,
                            onSuccess = {
                                Toast.makeText(requireContext(), "Product added to bag", Toast.LENGTH_SHORT).show()
                            },
                            onFailure = {
                                Toast.makeText(requireContext(), "Failed to add product to bag", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
