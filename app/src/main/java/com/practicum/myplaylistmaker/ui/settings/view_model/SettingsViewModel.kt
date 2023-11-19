package com.practicum.myplaylistmaker.ui.settings.view_model

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.myplaylistmaker.domain.settings.SettingsInteractor
import com.practicum.myplaylistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private var sharingInteractor: SharingInteractor,
    private var settingsInteractor: SettingsInteractor
) : ViewModel() {
    private  var themeLiveData : MutableLiveData<Boolean>
    init {
        themeLiveData = MutableLiveData(settingsInteractor.isDayOrNight())
    }



    fun getThemeLiveData(): LiveData<Boolean> {
        Log.d("themeLiveData",themeLiveData.value.toString())
        return themeLiveData
    }


    fun appThemeSwitch() {
        themeLiveData.value = settingsInteractor.appThemeSwitch()
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

}