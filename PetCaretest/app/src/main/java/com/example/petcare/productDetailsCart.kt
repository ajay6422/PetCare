package com.example.petcare

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge



class productDetailsCart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_details_cart)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener { finish() }

        val productTitle = findViewById<TextView>(R.id.productTitle)
        val productPrice = findViewById<TextView>(R.id.productPrice)
        val productDescription = findViewById<TextView>(R.id.productDescription)
        val productImage = findViewById<ImageView>(R.id.productImage)
        val name = intent.getStringExtra("name")
        val price = intent.getStringExtra("price")
        val desc = intent.getStringExtra("description")
        val imageRes = intent.getIntExtra("image", R.drawable.petlac)

        productTitle.text = name
        productPrice.text = price
        productDescription.text = desc
        productImage.setImageResource(imageRes)


    }
}