package com.practicum.myplaylistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.myplaylistmaker.databinding.FragmentSettingsBinding
import com.practicum.myplaylistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private val settingsViewModel: SettingsViewModel by viewModel()
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            switchButton.isChecked = !(settingsViewModel.getThemeLiveData().value ?: false)

            switchButton.setOnClickListener {
                settingsViewModel.appThemeSwitch()
                binding.switchButton.isChecked = !(settingsViewModel.getThemeLiveData().value ?: false)
            }

            shareButton.setOnClickListener {
                settingsViewModel.shareApp()
            }

            supportView.setOnClickListener {
                settingsViewModel.writeSupport()
            }

            TermsOfUse.setOnClickListener {
                settingsViewModel.readAgreement()
            }

        }

    }
}