package com.example.shoppy_onlineshop.ui.bag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.shoppy_onlineshop.databinding.FragmentBagBinding

class BagFragment : Fragment() {

    private var _binding: FragmentBagBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bagViewModel =
            ViewModelProvider(this).get(BagViewModel::class.java)

        _binding = FragmentBagBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textBag
        bagViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}