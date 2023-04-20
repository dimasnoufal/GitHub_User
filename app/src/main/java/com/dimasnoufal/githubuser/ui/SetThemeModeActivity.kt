package com.dimasnoufal.githubuser.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dimasnoufal.githubuser.ViewModelFactory.ViewModelFactory
import com.dimasnoufal.githubuser.databinding.ActivitySetThemeModeBinding
import com.dimasnoufal.githubuser.utils.SettingPreferences
import com.dimasnoufal.githubuser.viewmodels.SetThemeViewModel

class SetThemeModeActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var binding: ActivitySetThemeModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetThemeModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Set Theme Mode"

        val pref = SettingPreferences.getInstance(dataStore)
        val factory = ViewModelFactory(pref)
        val prevViewModel = ViewModelProvider(this, factory).get(SetThemeViewModel::class.java)

        prevViewModel.getThemeSettings().observe(this) {
            setNightMode(it)
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            prevViewModel.saveThemeSetting(isChecked)
        }
    }

    fun setNightMode(status: Boolean) {
        if (status) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.switchTheme.isChecked = true
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.switchTheme.isChecked = false
        }
    }
}