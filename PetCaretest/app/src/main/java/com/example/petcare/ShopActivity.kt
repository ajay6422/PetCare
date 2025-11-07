package com.example.petcare

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ShopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop)
        val btnCart = findViewById<ImageView>(R.id.btnCart)
        val btnWishlist = findViewById<TextView>(R.id.btnWishlist)

        val petlacCard = findViewById<LinearLayout>(R.id.petlacCard)
        val drClauderCard = findViewById<LinearLayout>(R.id.drClauderCard)
        val beapharCard = findViewById<LinearLayout>(R.id.beapharCard)
        val myBeauCard = findViewById<LinearLayout>(R.id.myBeauCard)

        petlacCard.setOnClickListener {
            // Corrected: Changed product_details_cart to product_details
            val intent = Intent(this, ProductDetails::class.java)
            intent.putExtra("name", "PetLac Milk Powder for Kittens")
            intent.putExtra("price", "₹1,900.00")
            intent.putExtra(
                "description",
                "Kitten milk replacer for newborn and orphaned kittens. Supports digestion, growth, and immune health with balanced nutrition."
            )
            intent.putExtra("image", R.drawable.petlac)
            startActivity(intent)
        }

        drClauderCard.setOnClickListener {
            val intent = Intent(this, ProductDetails::class.java)
            intent.putExtra("name", "Dr Clauders Whelpnmilk 450gm")
            intent.putExtra("price", "₹1,755.00")
            intent.putExtra(
                "description",
                "Premium milk replacer for puppies. Provides essential proteins, calcium, and Omega nutrients for strong bones and healthy growth."
            )
            intent.putExtra("image", R.drawable.dr_clauder)
            startActivity(intent)
        }

        btnCart.setOnClickListener {
            // Open your future CartActivity
        }

        btnWishlist.setOnClickListener {
            // Wishlist screen (if you make one later)
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation3)
        bottomNav.selectedItemId = R.id.nav_shop

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_shop -> true
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeScreen::class.java))
                    true
                }
                R.id.nav_services -> {
                    startActivity(Intent(this, ServicesActivity::class.java))
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
    }
}
