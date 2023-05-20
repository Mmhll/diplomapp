package com.ggg.gggapp.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.removeItemAt
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ggg.gggapp.R
import com.ggg.gggapp.databinding.ActivityBottomNavigationBinding
import com.ggg.gggapp.utils.JWTParser
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
                R.id.navigation_chat, R.id.navigation_profile, R.id.navigation_task, R.id.navigation_services -> {
                    showBottomNav()
                }
                else -> {
                    hideBottomNav()
                }
            }
        }
        val sharedPrefs = getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("token", "")!!
        val jwtParser = JWTParser(token)

        when(jwtParser.getClaimString("permission")){
            "ROLE_USER", "ROlE_EDITUSER" -> {
                navView?.menu?.removeItemAt(0)
            }
            else ->{

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