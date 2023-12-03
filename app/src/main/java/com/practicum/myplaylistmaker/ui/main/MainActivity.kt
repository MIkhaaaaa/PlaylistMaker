package com.practicum.myplaylistmaker.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindingMain.root)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootContainer) as NavHostFragment
        val navController = navHostFragment.navController

        //val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bindingMain.bottomNavigationView.setupWithNavController(navController)
    }
}