package com.practicum.myplaylistmaker.ui.settings.view_model

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.myplaylistmaker.App.App
import com.practicum.myplaylistmaker.domain.settings.SettingsInteractor
import com.practicum.myplaylistmaker.domain.sharing.SharingInteractor
import com.practicum.myplaylistmaker.util.Creator

class SettingsViewModel(
    private var sharingInteractor: SharingInteractor,
    private var settingsInteractor: SettingsInteractor
) : ViewModel() {
    init {
        sharingInteractor = Creator.provideSharingIneractor()
        settingsInteractor = Creator.provideSettingInteractor()
    }

    private var onBackLiveData = MutableLiveData(false)
    fun onBackClick() {
        onBackLiveData.value = true
    }

    fun getOnBackLiveData(): LiveData<Boolean> = onBackLiveData

    private var themeLiveData = MutableLiveData(settingsInteractor.themeSwitch())
    fun getThemeLiveData(): LiveData<Boolean> {
        val getting = if (themeLiveData.value!!) "day" else "night"
        return themeLiveData
    }

    fun themeSwitch() {
        themeLiveData.value = settingsInteractor.changeTheme()
        val getting = if (themeLiveData.value!!) "day" else "night"
        makeTheme(themeLiveData.value!!)
    }

    private fun makeTheme(theme: Boolean) {
        if (theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun writeSupport() {
        sharingInteractor.openSupport()
    }


    fun readAgreement() {
        sharingInteractor.openTerms()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val app = App()
                    return SettingsViewModel(
                        Creator.provideSharingIneractor(),
                        Creator.provideSettingInteractor()
                    ) as T
                }
            }
    }
}