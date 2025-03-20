package com.example.shoppy_onlineshop.ui.home.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.databinding.FragmentProductDetailsBinding

class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val PRODUCT_ID_KEY = "productId"

        fun newInstance(productId: Int) = ProductDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(PRODUCT_ID_KEY, productId)
            }
        }
    }

    private val viewModel: ProductDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)

        val productID = arguments?.getInt(PRODUCT_ID_KEY)

        if (productID == null || productID <= 0) {
            Toast.makeText(requireContext(), "product_not_found", Toast.LENGTH_SHORT).show()
            return binding.root
        }

        viewModel.loadProductDetails(
            context = requireContext(),
            productID = productID,
            onSuccess = { product ->
                binding.productTitle.text = product.title
                binding.productBrand.text = product.brand
                binding.productDescription.text = product.description
                binding.productPrice.text = "$" + product.price.toString()
                binding.productRating.text = "Rating: " + product.rating.toString()

                Glide.with(requireContext())
                    .load(product.images[0])
                    .placeholder(R.drawable.product_place_holder)
                    .into(binding.productImage)
                //update the rest of the product info in the UI.
            },
            onFailure = {
                Toast.makeText(
                    requireContext(),
                    "failed_to_load_product_details",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}