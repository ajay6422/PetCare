package com.example.petcare

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ServicesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_services)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation2)
        bottomNav.selectedItemId = R.id.nav_services

        // This listener now correctly returns a Boolean for every case.
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_services -> true
                R.id.nav_shop -> {
                    startActivity(Intent(this, ShopActivity::class.java))
                    true
                }

                R.id.nav_home -> {
                    startActivity(Intent(this, HomeScreen::class.java))
                    true
                }

                R.id.nav_socialise -> {
                    startActivity(Intent(this, KnowYourPetActivity::class.java))
                    true
                }

                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }

                else -> false
            }
        }

        // The ImageView's click listener is moved outside and is now separate.
        val grooming = findViewById<ImageView>(R.id.grooming)
        grooming.setOnClickListener {
            val intent = Intent(this, PetServicesIntent::class.java)
            startActivity(intent)
        }

        val boarding = findViewById<ImageView>(R.id.boarding)
        boarding.setOnClickListener {
            val intent = Intent(this, PetServicesIntent::class.java)
            startActivity(intent)

        }
        val transport = findViewById<ImageView>(R.id.transport)
        transport.setOnClickListener {
            val intent = Intent(this, PetServicesIntent::class.java)
            startActivity(intent)

        }
        val training = findViewById<ImageView>(R.id.training)
        training.setOnClickListener {
            val intent = Intent(this, PetServicesIntent::class.java)
            startActivity(intent)

        }
    }
}
