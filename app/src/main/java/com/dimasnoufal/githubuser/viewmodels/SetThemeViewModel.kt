package com.dimasnoufal.githubuser.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dimasnoufal.githubuser.utils.SettingPreferences
import kotlinx.coroutines.launch

class SetThemeViewModel (private val settingPref: SettingPreferences) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> = settingPref.getThemeSetting().asLiveData()

    fun saveThemeSetting(status: Boolean) {
        viewModelScope.launch {
            settingPref.saveThemeSetting(status)
        }
    }
}