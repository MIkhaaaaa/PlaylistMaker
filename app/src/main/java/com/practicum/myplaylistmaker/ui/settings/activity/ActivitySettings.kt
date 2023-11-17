package com.practicum.myplaylistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.myplaylistmaker.databinding.ActivitySettingsBinding
import com.practicum.myplaylistmaker.ui.settings.view_model.SettingsViewModel


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
            finish()
        }

        binding.switchButton.isChecked = !(settingsViewModel.getThemeLiveData().value ?: false)

        binding.switchButton.setOnClickListener {
            settingsViewModel.appThemeSwitch()
            binding.switchButton.isChecked = !(settingsViewModel.getThemeLiveData().value ?: false)
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