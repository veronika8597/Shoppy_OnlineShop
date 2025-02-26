package com.example.shoppy_onlineshop.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.api.StoreProduct
import com.example.shoppy_onlineshop.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyFavoritesTextView: TextView
    private lateinit var emptyFavoritesImageView: ImageView

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        recyclerView = view.findViewById(R.id.bagRecyclerView)
        emptyFavoritesTextView = view.findViewById(R.id.emptyFavoritesTextView)
        emptyFavoritesImageView = view.findViewById(R.id.bagimageView)

        favoritesAdapter = FavoritesAdapter(emptyList())//Empty at first
        recyclerView.adapter = favoritesAdapter

        favoritesViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)
        favoritesViewModel.favoriteItems.observe(viewLifecycleOwner) { favoriteProducts ->
            favoritesAdapter.updateFavoriteProducts(favoriteProducts)
            updateUI(favoriteProducts)
        }
        return view
    }

    private fun updateUI(favoriteProducts: List<StoreProduct>) {
        if (favoriteProducts.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyFavoritesTextView.visibility = View.VISIBLE
            emptyFavoritesImageView.visibility = View.VISIBLE
        }
        else {
            recyclerView.visibility = View.VISIBLE
            emptyFavoritesTextView.visibility = View.GONE
            emptyFavoritesImageView.visibility = View.GONE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}