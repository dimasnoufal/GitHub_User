package com.dimasnoufal.githubuser.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasnoufal.githubuser.data.RemoteDataSource
import com.dimasnoufal.githubuser.data.Repository
import com.dimasnoufal.githubuser.data.network.Service
import com.dimasnoufal.githubuser.data.network.handler.NetworkResult
import com.dimasnoufal.githubuser.model.GithubResponse
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    // Api
    private val remoteService = Service.GitHubService
    private val remote = RemoteDataSource(remoteService)

    private val repository = Repository(remote)

    private var _userList: MutableLiveData<NetworkResult<GithubResponse>> = MutableLiveData()
    val userList: LiveData<NetworkResult<GithubResponse>> = _userList

    init {
        getListUser("arif")
    }

    fun getListUser(username: String) {
        viewModelScope.launch {
            repository.remote!!.getUser(username).collect { res ->
                _userList.value = res
            }
        }
    }
}