package com.dimasnoufal.githubuser.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimasnoufal.githubuser.R
import com.dimasnoufal.githubuser.ViewModelFactory.ViewModelFactory
import com.dimasnoufal.githubuser.adapter.UserAdapter
import com.dimasnoufal.githubuser.data.network.handler.NetworkResult
import com.dimasnoufal.githubuser.databinding.ActivityMainBinding
import com.dimasnoufal.githubuser.model.ItemsItem
import com.dimasnoufal.githubuser.utils.AppConstant
import com.dimasnoufal.githubuser.utils.SettingPreferences
import com.dimasnoufal.githubuser.viewmodels.MainViewModel
import com.dimasnoufal.githubuser.viewmodels.SetThemeViewModel

class MainActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var binding: ActivityMainBinding
    private val userAdapter by lazy { UserAdapter() }
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val prefFactory = ViewModelFactory(pref)
        val prevViewModel = ViewModelProvider(this, prefFactory).get(SetThemeViewModel::class.java)

        prevViewModel.getThemeSettings().observe(this) {
            setNightMode(it)
        }

        mainViewModel.userList.observe(this@MainActivity) { res ->
            when (res) {
                is NetworkResult.Loading -> {
                    handleUi(
                        recylerview = false,
                        progressbar = true,
                        tverror = false,
                        iverror = false
                    )
                }
                is NetworkResult.Error -> {
                    binding.tvError.text = res.errorMessage
                    handleUi(
                        recylerview = false,
                        progressbar = false,
                        tverror = true,
                        iverror = true
                    )
                    Toast.makeText(this@MainActivity, res.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Success -> {
                    binding.rvUser.apply {
                        adapter = userAdapter
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        userAdapter.setData(res.data?.items)
                    }
                    userAdapter.setOnItemClickCallback(object : UserAdapter.IOnItemCallBack{
                        override fun onItemClickCallback(data: ItemsItem) {
                            val detail = Intent(this@MainActivity,DetailActivity::class.java)
                            detail.putExtra(AppConstant.EXTRA_USER,data)
                            startActivity(detail)
                        }
                    })
                    handleUi(
                        recylerview = true,
                        progressbar = false,
                        tverror = false,
                        iverror = false
                    )
                }
            }
        }


    }

    fun setNightMode(status: Boolean) {
        if (status) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun handleUi(
        recylerview: Boolean,
        progressbar: Boolean,
        tverror: Boolean,
        iverror: Boolean
    ) {
        binding.apply {
            rvUser.isVisible = recylerview
            progressBar.isVisible = progressbar
            tvError.isVisible = tverror
            ivError.isVisible = iverror
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchView = menu?.findItem(R.id.item_search)?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mainViewModel.getListUser(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.toString().isNotBlank()) mainViewModel.getListUser(newText.toString())
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_favorite -> {
                val favorite = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(favorite)
                return true
            }

            R.id.item_mode -> {
                val mode = Intent(this@MainActivity, SetThemeModeActivity::class.java)
                startActivity(mode)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

}