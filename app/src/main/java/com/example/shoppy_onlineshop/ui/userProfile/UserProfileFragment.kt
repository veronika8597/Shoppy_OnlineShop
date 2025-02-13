package com.example.shoppy_onlineshop.ui.userProfile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.databinding.FragmentUserProfileBinding
import com.example.shoppy_onlineshop.ui.LogIn.LogInActivity
import com.google.firebase.auth.FirebaseAuth

class UserProfileFragment : Fragment() {

    private lateinit var userNameTextView: TextView

    private val sections = listOf(
        AccountSection(R.drawable.orders, "My orders"),
        AccountSection(R.drawable.question, "Need help?"),
        AccountSection(R.drawable.details, "My details"),
        AccountSection(R.drawable.password, "Change password"),
        AccountSection(R.drawable.address, "My addresses"),
        AccountSection(R.drawable.card, "Payment methods"),
        AccountSection(R.drawable.sign_out, "Log out")
    )

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



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.MyAccountRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = MyAccountAdapter(sections) { section ->
            handleSectionClick(section)
        }
    }

    private fun handleSectionClick(section: AccountSection) {
        when (section.title) {
/*            "Profile" -> findNavController().navigate(R.id.action_myAccountFragment_to_profileFragment)
            "My Orders" -> findNavController().navigate(R.id.action_myAccountFragment_to_ordersFragment)
            "Settings" -> findNavController().navigate(R.id.action_myAccountFragment_to_settingsFragment)
            "Help & Support" -> findNavController().navigate(R.id.action_myAccountFragment_to_helpFragment)*/
            "Log out" -> logoutUser()
        }
    }

    private fun logoutUser() {
        // Handle logout logic (Firebase sign out, clear session, navigate to login screen)
        Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), LogInActivity::class.java)
        startActivity(intent)
    }
}

