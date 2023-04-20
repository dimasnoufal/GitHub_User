package com.dimasnoufal.githubuser.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.dimasnoufal.githubuser.data.LocalDataSource
import com.dimasnoufal.githubuser.data.RemoteDataSource
import com.dimasnoufal.githubuser.data.Repository
import com.dimasnoufal.githubuser.data.database.UserDatabase
import com.dimasnoufal.githubuser.data.database.UserEntity
import com.dimasnoufal.githubuser.data.network.Service
import com.dimasnoufal.githubuser.data.network.handler.NetworkResult
import com.dimasnoufal.githubuser.model.DetailUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailUserViewModel (application: Application) : AndroidViewModel(application) {
    // Api
    private val remoteService = Service.GitHubService
    private val remote = RemoteDataSource(remoteService)

    // LOCAL
    private val userDao = UserDatabase.getDatabase(application).userDao()
    private val local = LocalDataSource(userDao)

    private val repository = Repository(remote,local)

    private var _userDetail: MutableLiveData<NetworkResult<DetailUserResponse>> = MutableLiveData()
    val userDetail: LiveData<NetworkResult<DetailUserResponse>> = _userDetail

    fun getDetailUser(username: String) {
        viewModelScope.launch {
            repository.remote!!.getDetailUser(username).collect { res ->
                _userDetail.value = res
            }
        }
    }

    val favoriteUserList:LiveData<List<UserEntity>> = repository.local!!.listUser().asLiveData()

    fun insertFavoriteUser(userEntity: UserEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local!!.insertUser(userEntity)
        }
    }

    fun deleteFavoriteUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local!!.deleteUser(userEntity)
        }
    }

}