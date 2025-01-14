package com.example.shoppy_onlineshop.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppy_onlineshop.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Explore title
        val titleTextView: TextView = binding.ExploreTitleTextView
        homeViewModel.exploreTitle.observe(viewLifecycleOwner) {
            titleTextView.text = it
        }

        //Search Bar
        val searchBar: SearchView = binding.searchBarHome
        searchBar.queryHint = "Search on Shoppy"
        binding.searchBarHome.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(queary: String?): Boolean {
                // Handle search query submission
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
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