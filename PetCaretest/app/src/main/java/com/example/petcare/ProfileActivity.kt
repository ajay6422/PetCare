package com.example.petcare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge

class ProfileActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageView
    private lateinit var btnEditProfile: Button
    private lateinit var btnShare: ImageView
    private lateinit var btnEditPic: ImageView
    private lateinit var txtUserName: TextView
    private lateinit var txtUserEmail: TextView
    private lateinit var txtFollowers: TextView
    private lateinit var txtFollowing: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        // Initialize views
        btnBack = findViewById(R.id.btnBack)
        btnEditProfile = findViewById(R.id.btnEditProfile)
        btnShare = findViewById(R.id.btnShare)
        btnEditPic = findViewById(R.id.btnEditPic)
        txtUserName = findViewById(R.id.txtUserName)
        txtUserEmail = findViewById(R.id.txtUserEmail)
        txtFollowers = findViewById(R.id.txtFollowers)
        txtFollowing = findViewById(R.id.txtFollowing)

        // Set text data
        txtUserName.text = "User xyz"
        txtUserEmail.text = "user123@gmail.com"
        txtFollowers.text = "250"
        txtFollowing.text = "180"

        // Click listeners
        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnShare.setOnClickListener {
            shareProfile()
        }

        // ✅ Important: Call bottom navigation setup
        setupBottomNavigation()
    }

    // 📤 Share profile info
    private fun shareProfile() {
        val shareText = "Check out ${txtUserName.text}'s profile on the PetCare App! 🐾"
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(shareIntent, "Share Profile via"))
    }

    // ✅ Setup bottom navigation
    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation1)
        bottomNav.selectedItemId = R.id.nav_profile

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> true
                R.id.nav_shop -> {
                    startActivity(Intent(this, ShopActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_services -> {
                    startActivity(Intent(this, ServicesActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeScreen::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_socialise -> {
                    startActivity(Intent(this, KnowYourPetActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }
}
