package com.example.petcare

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class HomeScreen : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: BannerAdapter
    private var currentPage = 0
    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)


        val ImageView = findViewById<ImageView>(R.id.allImg)
        ImageView.setOnClickListener {
            val intent = Intent(this, ShopActivity::class.java)
            startActivity(intent)
        }
        val ImageView1 = findViewById<ImageView>(R.id.dogImg)
        ImageView1.setOnClickListener {
            val intent = Intent(this, ShopActivity::class.java)
            startActivity(intent)
        }
        val ImageView2 = findViewById<ImageView>(R.id.catImg)
        ImageView2.setOnClickListener {
            val intent = Intent(this, ShopActivity::class.java)
            startActivity(intent)
        }
        val botImg = findViewById<ImageView>(R.id.chatBotimage)
        botImg.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        // --- Setup Banner ViewPager ---
        viewPager = findViewById(R.id.viewPagerBanner)
        val images = listOf(
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3
        )

        adapter = BannerAdapter(images)
        viewPager.adapter = adapter

        // Auto-slide every 3 seconds
        timer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (currentPage == images.size) currentPage = 0
                    viewPager.currentItem = currentPage++
                }
            }
        }, 3000, 3000)

        // --- Setup Bottom Navigation ---
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> true
                R.id.nav_shop -> {
                    startActivity(Intent(this, ShopActivity::class.java))
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
    } // <-- onCreate() method ends here

    // --- onDestroy() is now correctly placed outside of onCreate() ---
    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}
