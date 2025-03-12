package com.example.shoppy_onlineshop.ui.userProfile.ChangePassword

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shoppy_onlineshop.databinding.FragmentChangePasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordFragment : Fragment() {

    private lateinit var resetPassword: TextView
    private lateinit var loadingLayout: RelativeLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var emailSentTextView: TextView
    private lateinit var changePasswordForm: LinearLayout
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        resetPassword = binding.forgotPasswordTextView
        loadingLayout = binding.loadingLayout
        progressBar = binding.progressBar
        emailSentTextView = binding.emailSentTextView
        changePasswordForm = binding.changePasswordForm

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resetPassword.setOnClickListener {
            showLoading()
            sendPasswordResetEmail()
        }

        binding.savePasswordButton.setOnClickListener {
            val currentPassword = binding.currentPasswordTextPassword.text.toString().trim()
            val newPassword = binding.newPasswordTextPassword.text.toString().trim()
            val confirmPassword = binding.confirmPasswordTextPassword.text.toString().trim()

            if (currentPassword.isEmpty()) {
                binding.currentPasswordTextPassword.error = "Please enter your current password"
                Toast.makeText(requireContext(), "Please enter your current password", Toast.LENGTH_SHORT).show()

            } else if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                binding.newPasswordTextPassword.error = "Please enter the new password"
                binding.confirmPasswordTextPassword.error = "Please confirm the new password"

            } else if (newPassword != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                binding.newPasswordTextPassword.error = "Passwords do not match"
                binding.confirmPasswordTextPassword.error = "Passwords do not match"

            } else {
                checkCurrentPassword(currentPassword, newPassword)
            }
        }
    }

    private fun showLoading() {
        changePasswordForm.visibility = View.GONE
        loadingLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        emailSentTextView.visibility = View.GONE
    }


    private fun hideLoading() {
        changePasswordForm.visibility = View.VISIBLE
        loadingLayout.visibility = View.GONE
        progressBar.visibility = View.GONE
        emailSentTextView.visibility = View.GONE
    }


    private fun showEmailSentConfirmation() {
        progressBar.visibility = View.GONE
        emailSentTextView.visibility = View.VISIBLE
    }

    
    private fun sendPasswordResetEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email

        if (email != null) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showEmailSentConfirmation()
                        Handler(Looper.getMainLooper()).postDelayed({
                            findNavController().popBackStack()
                        }, 3000) // 3 seconds delay
                    } else {
                        hideLoading()
                        Toast.makeText(
                            requireContext(),
                            "Failed to send reset email. Try again later.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            hideLoading()
            Toast.makeText(
                requireContext(),
                "User email not found.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateUserPassword(newPassword: String) {
        val user = FirebaseAuth.getInstance().currentUser

        user?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show()

                    binding.currentPasswordTextPassword.text.clear()
                    binding.newPasswordTextPassword.text.clear()
                    binding.confirmPasswordTextPassword.text.clear()
                } else {
                    Toast.makeText(requireContext(), "Failed to change password. Try again later.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkCurrentPassword(currentPassword: String, newPassword: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email ?: return

        val credential = EmailAuthProvider.getCredential(email, currentPassword)
        user.reauthenticate(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserPassword(newPassword)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Incorrect current password",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.currentPasswordTextPassword.error = "Current password is incorrect"
                }
            }
    }

}