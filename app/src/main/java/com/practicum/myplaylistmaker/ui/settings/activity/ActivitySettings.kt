package com.practicum.myplaylistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.myplaylistmaker.databinding.ActivitySettingsBinding
import com.practicum.myplaylistmaker.ui.settings.view_model.SettingsViewModel

const val PRACTICUM_EXAMPLE_PREFERENCES = "practicum_example_preferences"
const val THEME_KEY = "theme_key"

class ActivitySettings : AppCompatActivity() {
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        binding.returnButton.setOnClickListener {
            settingsViewModel.onBackClick()
        }
        settingsViewModel.getOnBackLiveData()
            .observe(this) { onBackLiveData -> onBackClick(onBackLiveData) }


        binding.switchButton.isChecked = !(settingsViewModel.getThemeLiveData().value!!)
        binding.switchButton.setOnClickListener {
            settingsViewModel.themeSwitch()
            binding.switchButton.isChecked = !(settingsViewModel.getThemeLiveData().value!!)
        }

        binding.shareButton.setOnClickListener {
            settingsViewModel.shareApp()
        }

        binding.supportView.setOnClickListener {
            settingsViewModel.writeSupport()
        }

        binding.TermsOfUse.setOnClickListener {
            settingsViewModel.readAgreement ()
        }
    }

    private fun onBackClick(back: Boolean) {
        if (back) {
            finish()
        }
    }
}