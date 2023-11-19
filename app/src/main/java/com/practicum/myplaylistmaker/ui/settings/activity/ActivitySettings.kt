package com.practicum.myplaylistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.myplaylistmaker.databinding.ActivitySettingsBinding
import com.practicum.myplaylistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ActivitySettings : AppCompatActivity() {
    private val settingsViewModel:SettingsViewModel by viewModel()
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

}