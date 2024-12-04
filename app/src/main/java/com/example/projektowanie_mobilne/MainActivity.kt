package com.example.projektowanie_mobilne

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.projektowanie_mobilne.databinding.ActivityMainBinding
import com.example.projektowanie_mobilne.fragments.GalleryFragment
import com.example.projektowanie_mobilne.fragments.LocationFragment
import com.example.projektowanie_mobilne.fragments.MusicFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Domyślnie ładujemy fragment galerii
        loadFragment(GalleryFragment())

        // Obsługa kliknięć w BottomNavigationView
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_gallery -> {
                    loadFragment(GalleryFragment())
                    true
                }
                R.id.nav_music -> {
                    loadFragment(MusicFragment())
                    true
                }
                R.id.nav_location -> {
                    loadFragment(LocationFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}

