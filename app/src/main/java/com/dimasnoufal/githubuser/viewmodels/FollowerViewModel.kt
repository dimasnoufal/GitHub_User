package com.dimasnoufal.githubuser.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dimasnoufal.githubuser.data.RemoteDataSource
import com.dimasnoufal.githubuser.data.Repository
import com.dimasnoufal.githubuser.data.network.Service
import com.dimasnoufal.githubuser.data.network.handler.NetworkResult
import com.dimasnoufal.githubuser.model.DetailUserResponse
import kotlinx.coroutines.launch

class FollowerViewModel (application: Application) : AndroidViewModel(application) {
    // Api
    private val remoteService = Service.GitHubService
    private val remote = RemoteDataSource(remoteService)

    private val repository = Repository(remote)

    private var _followerList: MutableLiveData<NetworkResult<ArrayList<DetailUserResponse>>> = MutableLiveData()
    val followerList: LiveData<NetworkResult<ArrayList<DetailUserResponse>>> = _followerList

    fun getFollowerList(username: String) {
        viewModelScope.launch {
            repository.remote!!.getFollowersList(username).collect { res ->
                _followerList.value = res
            }
        }
    }
}