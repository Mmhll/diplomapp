package com.ggg.gggapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ggg.gggapp.R
import com.ggg.gggapp.fragments.auth.AuthFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.containerView, AuthFragment()).commit()
    }
}




