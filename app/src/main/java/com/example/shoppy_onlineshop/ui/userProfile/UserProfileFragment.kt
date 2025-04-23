package com.example.shoppy_onlineshop.ui.userProfile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.databinding.FragmentUserProfileBinding
import com.example.shoppy_onlineshop.ui.LogIn.LogInActivity
import com.example.shoppy_onlineshop.ui.LogIn.UserPreferences
import com.example.shoppy_onlineshop.ui.userProfile.accountSection.AccountSection
import com.example.shoppy_onlineshop.ui.userProfile.accountSection.MyAccountAdapter
import com.google.firebase.auth.FirebaseAuth
import androidx.core.content.edit

class UserProfileFragment : Fragment() {

    private lateinit var userNameTextView: TextView

    private val sections = listOf(
        AccountSection(R.drawable.orders, "My orders"),
        AccountSection(R.drawable.question, "Need help?"),
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
        userNameTextView.text = "$userName"

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
            "My orders" -> findNavController().navigate(R.id.action_user_profileFragment_to_userOrdersFragment)
            "Need help?" -> findNavController().navigate(R.id.action_user_profileFragment_to_faqFragment)
            "Change password" -> findNavController().navigate(R.id.action_user_profileFragment_to_changePasswordFragment)
            "My addresses" -> findNavController().navigate(R.id.action_user_profileFragment_to_addressesFragment)
            "Payment methods" -> findNavController().navigate(R.id.action_user_profileFragment_to_paymentMethodsFragment)
            "Log out" -> logoutUser()
        }
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut() // Sign out from Firebase

        // Clear all persisted data
        UserPreferences.clearCredentials(requireContext())
        requireContext().getSharedPreferences("addresses_prefs", Context.MODE_PRIVATE).edit() { clear() }
        requireContext().getSharedPreferences("payment_methods_prefs", Context.MODE_PRIVATE).edit() { clear() }
        requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE).edit() { clear() }

        Toast.makeText(this.context, "Logged out", Toast.LENGTH_SHORT).show()

        // Navigate back to login screen and clear activity stack
        val intent = Intent(this.context, LogInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish() // Finish the current activity
    }
}

