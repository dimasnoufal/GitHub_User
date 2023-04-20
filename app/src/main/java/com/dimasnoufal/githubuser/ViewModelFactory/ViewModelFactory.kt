package com.dimasnoufal.githubuser.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dimasnoufal.githubuser.utils.SettingPreferences
import com.dimasnoufal.githubuser.viewmodels.SetThemeViewModel

class ViewModelFactory (private val settingPref: SettingPreferences): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetThemeViewModel::class.java)) {
            return SetThemeViewModel(settingPref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}