package com.practicum.myplaylistmaker.ui.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.RootActivityBinding

class RootActivity : AppCompatActivity() {
    private lateinit var bindingMain: RootActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = RootActivityBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.root_container) as NavHostFragment
        bindingMain.bottomNavigationView.setupWithNavController(navHostFragment.navController)
    }
}