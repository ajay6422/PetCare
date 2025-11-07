package com.example.petcare

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge



class ProductDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_details)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        val productTitle = findViewById<TextView>(R.id.productTitle)
        val productPrice = findViewById<TextView>(R.id.productPrice)
        val productDescription = findViewById<TextView>(R.id.productDescription)
        val productImage = findViewById<ImageView>(R.id.productImage)

        // --- Handle Back Button ---
        backBtn.setOnClickListener {
            finish()
        }

        // --- Receive Data from Intent (if coming from ShopActivity) ---
        val name = intent.getStringExtra("name")
        val price = intent.getStringExtra("price")
        val desc = intent.getStringExtra("description")
        val imageRes = intent.getIntExtra("image", R.drawable.dr_clauder)

        // --- Set Data to Views ---
        productTitle.text = name ?: "Dr Clauders Whelpnmilk 450gm"
        productPrice.text = price ?: "₹1,755.00"
        productDescription.text = desc
            ?: "Premium milk replacer for newborn and orphaned puppies. Supports growth, immunity, and strength with a formula similar to mother’s milk. Highly digestible and enriched with calcium and omega nutrients."
        productImage.setImageResource(imageRes)


    }
}