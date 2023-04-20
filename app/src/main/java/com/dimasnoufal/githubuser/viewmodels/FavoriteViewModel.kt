package com.dimasnoufal.githubuser.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.dimasnoufal.githubuser.data.LocalDataSource
import com.dimasnoufal.githubuser.data.Repository
import com.dimasnoufal.githubuser.data.database.UserDatabase
import com.dimasnoufal.githubuser.data.database.UserEntity

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    // LOCAL
    private val userDao = UserDatabase.getDatabase(application).userDao()
    private val local = LocalDataSource(userDao)

    private val repository = Repository(local = local)

    val favoriteUserList: LiveData<List<UserEntity>> = repository.local!!.listUser().asLiveData()
}