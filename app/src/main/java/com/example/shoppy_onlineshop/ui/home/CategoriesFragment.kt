package com.example.shoppy_onlineshop.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppy_onlineshop.api.StoreCategory
import com.example.shoppy_onlineshop.databinding.FragmentCategoriesBinding
import com.example.shoppy_onlineshop.ui.home.adapter.CategoryAdapter

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private var categories: List<StoreCategory>? = null

    companion object {
        fun newInstance() = CategoriesFragment()
    }

    private val viewModel: CategoriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        // Retrieve categories from arguments
        categories = arguments?.getParcelableArrayList("categories")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categories?.let {
            val categoryAdapter = CategoryAdapter(it)
            binding.all.layoutManager = GridLayoutManager(context,2)
            binding.all.adapter = categoryAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}