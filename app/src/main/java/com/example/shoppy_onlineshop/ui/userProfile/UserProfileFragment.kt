package com.example.shoppy_onlineshop.ui.userProfile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.shoppy_onlineshop.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth

class UserProfileFragment : Fragment() {

    private lateinit var userNameTextView: TextView

    companion object {
        fun newInstance() = UserProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for the fragment
        val binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        userNameTextView = binding.userNameText

        // Retrieve the current user's name directly from Firebase
        val userName = FirebaseAuth.getInstance().currentUser?.displayName ?: "Guest" // Default to "Guest" if no name is found

        // Log the user name for debugging purposes
        Log.d("UserProfileFragment", "User name: $userName")

        // Set the user's name in the TextView
        userNameTextView.text = "Hello $userName"

        // Return the root view
        return binding.root
    }
}