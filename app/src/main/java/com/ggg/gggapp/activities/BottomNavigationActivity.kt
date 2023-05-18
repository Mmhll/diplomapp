package com.ggg.gggapp.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ggg.gggapp.R
import com.ggg.gggapp.databinding.ActivityBottomNavigationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavigationBinding
    private var navView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_navigation)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_chat -> {
                    showBottomNav()
                }
                R.id.navigation_profile -> {
                    showBottomNav()
                }
                R.id.navigation_news -> {
                    showBottomNav()
                }
                R.id.navigation_services -> {
                    showBottomNav()
                }
                else -> {
                    hideBottomNav()
                }
            }
        }

        navView?.setupWithNavController(navController)
    }

    private fun showBottomNav() {
        navView?.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        navView?.visibility = View.GONE
    }

}