package com.example.shoppy_onlineshop.ui.LogIn

import UserPreferences
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.example.shoppy_onlineshop.BuildConfig
import com.example.shoppy_onlineshop.MainActivity
import com.example.shoppy_onlineshop.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.security.MessageDigest

class LogInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var userEmail: EditText
    private lateinit var userPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var login2reg: TextView
    private lateinit var googleLoginButton: ImageView

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_log_in)

        login2reg= findViewById(R.id.sign_up_text)
        userEmail= findViewById(R.id.email_input)
        userPassword = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)
        googleLoginButton = findViewById(R.id.google_login)


        //window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        supportActionBar?.hide()

        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            val signatures = info.signingInfo?.apkContentsSigners
            if (signatures != null) {
                for (signature in signatures) {
                    val md = MessageDigest.getInstance("SHA-1")
                    md.update(signature.toByteArray())
                    val sha = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                    Log.d("REAL_SHA", sha)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Configure sign-in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

/*        FirebaseAuth.getInstance().signOut()
        googleSignInClient.signOut()*/

        lifecycleScope.launch {
            val (savedEmail, savedPassword) = UserPreferences.getCredentials(this@LogInActivity)
            if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

                if (isLoggedIn) {
                    autoLogin(savedEmail, savedPassword)
                }
            }
        }

        loginButton.setOnClickListener {
            val enteredEmail = userEmail.text.toString()
            val enteredPassword = userPassword.text.toString()

            if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(this, "All fields must be filled in", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            loginUser(enteredEmail, enteredPassword)
        }

        googleLoginButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        login2reg.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    sharedPreferences.edit() { putBoolean("isLoggedIn", true) }

                    // 🔹 Save credentials for future auto-login
                    lifecycleScope.launch {
                        UserPreferences.saveCredentials(this@LogInActivity, email, password)
                    }

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    userEmail.text.clear()
                    userPassword.text.clear()
                }
            }
    }

    private fun autoLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n" +
            "which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n" +
            "contracts for common intents available in\n" +
            "{@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n" +
            "testing, and allow receiving results in separate, testable classes independent from your\n" +
            "activity. Use {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n" +
            "with the appropriate {@link ActivityResultContract} and handling the result in the\n" +
            "{@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("Login", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        val db = FirebaseFirestore.getInstance()
                        val userRef = db.collection("users").document(it.uid)

                        userRef.get().addOnSuccessListener { doc ->
                            if (!doc.exists()) {
                                val userData = hashMapOf(
                                    "uid" to it.uid,
                                    "name" to (it.displayName ?: ""),
                                    "email" to (it.email ?: ""),
                                    "createdAt" to FieldValue.serverTimestamp()
                                )
                                userRef.set(userData)
                            }
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }
                } else {
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

}