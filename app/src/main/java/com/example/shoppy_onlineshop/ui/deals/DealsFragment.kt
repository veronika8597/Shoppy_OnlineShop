package com.example.shoppy_onlineshop.ui.deals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppy_onlineshop.databinding.FragmentDealsBinding

class DealsFragment : Fragment() {

    private var _binding: FragmentDealsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dealsViewModel =
            ViewModelProvider(this).get(DealsViewModel::class.java)

        _binding = FragmentDealsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDeals
        dealsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}