package com.example.shoppy_onlineshop.ui.LogIn

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
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
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var userEmail: EditText
    private lateinit var userPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var login2reg: TextView
    private lateinit var googleLoginButton: ImageView

    private val RC_SIGN_IN = 9001

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContentView(R.layout.activity_log_in)

        auth = FirebaseAuth.getInstance()
        Log.d("LoginFlow", "FirebaseAuth initialized")

        userEmail = findViewById(R.id.email_input)
        userPassword = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)
        login2reg = findViewById(R.id.sign_up_text)
        googleLoginButton = findViewById(R.id.google_login)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        supportActionBar?.hide()

        printAppShaKey()
        setupGoogleSignInClient()
        handleAutoLogin()
        setupClickListeners()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun printAppShaKey() {
        try {
            val info = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
            val signatures = info.signingInfo?.apkContentsSigners
            signatures?.forEach { signature ->
                val md = MessageDigest.getInstance("SHA-1")
                md.update(signature.toByteArray())

                val base64Sha = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                val colonSha = md.digest().joinToString(":") { byte -> "%02X".format(byte) }

                Log.d("REAL_SHA", "🔐 Base64 SHA-1 (Android Studio style): $base64Sha")
                Log.d("REAL_SHA", "📛 Colon-separated SHA-1 (Firebase format): $colonSha")
            }
        } catch (e: Exception) {
            Log.e("REAL_SHA", "Error printing SHA key", e)
        }
    }

    private fun setupGoogleSignInClient() {
        Log.d("LoginFlow", "Setting up GoogleSignInClient with client ID: ${BuildConfig.GOOGLE_CLIENT_ID}")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        Log.d("LoginFlow", "Using CLIENT_ID: 817175636364-i15dot3c0vgr1h21gi29obqi59t9hb92.apps.googleusercontent.com")

    }

    private fun handleAutoLogin() {
        lifecycleScope.launch {
            val (savedEmail, savedPassword) = UserPreferences.getCredentials(this@LogInActivity)
            val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val isLoggedIn = prefs.getBoolean("isLoggedIn", false)
            Log.d("LoginFlow", "Auto-login check: email=$savedEmail, isLoggedIn=$isLoggedIn")
            if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty() && isLoggedIn) {
                autoLogin(savedEmail, savedPassword)
            }
        }
    }

    private fun setupClickListeners() {
        loginButton.setOnClickListener {
            val email = userEmail.text.toString()
            val password = userPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields must be filled in", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("LoginFlow", "Manual login attempt with email: $email")
                loginUser(email, password)
            }
        }

        googleLoginButton.setOnClickListener {
            Log.d("LoginFlow", "Google Sign-In button clicked")
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
            Toast.makeText(this, "Intent Launched", Toast.LENGTH_SHORT).show()
        }

        login2reg.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("LoginFlow", "Login success for: $email")
                getSharedPreferences("user_prefs", Context.MODE_PRIVATE).edit {
                    putBoolean("isLoggedIn", true)
                }
                lifecycleScope.launch {
                    UserPreferences.saveCredentials(this@LogInActivity, email, password)
                }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Log.w("LoginFlow", "Email login failed", task.exception)
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                userEmail.text.clear()
                userPassword.text.clear()
            }
        }
    }

    private fun autoLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("LoginFlow", "Auto-login success for $email")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Log.w("LoginFlow", "Auto-login failed", it.exception)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                Log.d("LoginFlow", "Google SignIn task result: $task")
                Log.d("LoginFlow", "Exception (if any): ${task.exception?.message}")

                val account = task.getResult(ApiException::class.java)

                Log.d("LoginFlow", "ID Token received: ${account.idToken}")
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.e("LoginFlow", "Google sign-in failed (ApiException): ${e.statusCode}", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d("LoginFlow", "Authenticating with Firebase using Google token")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("LoginFlow", "Firebase sign-in with Google successful")
                val user = FirebaseAuth.getInstance().currentUser
                user?.let { firebaseUser ->
                    val db = FirebaseFirestore.getInstance()
                    val userRef = db.collection("users").document(firebaseUser.uid)

                    userRef.get().addOnSuccessListener { doc ->
                        if (!doc.exists()) {
                            Log.d("LoginFlow", "New Google user. Saving to Firestore.")
                            val userData = hashMapOf(
                                "uid" to firebaseUser.uid,
                                "name" to (firebaseUser.displayName ?: ""),
                                "email" to (firebaseUser.email ?: ""),
                                "createdAt" to FieldValue.serverTimestamp()
                            )
                            userRef.set(userData)
                        } else {
                            Log.d("LoginFlow", "Returning Google user. Firestore entry exists.")
                        }
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }.addOnFailureListener {
                        Log.e("LoginFlow", "Firestore user check failed", it)
                    }
                }
            } else {
                Log.e("LoginFlow", "Firebase sign-in with Google failed", task.exception)
                Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }



}
