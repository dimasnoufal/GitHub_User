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

class FollowingViewModel (application: Application) : AndroidViewModel(application) {
    // Api
    private val remoteService = Service.GitHubService
    private val remote = RemoteDataSource(remoteService)

    private val repository = Repository(remote)

    private var _followingList: MutableLiveData<NetworkResult<ArrayList<DetailUserResponse>>> = MutableLiveData()
    val followingList: LiveData<NetworkResult<ArrayList<DetailUserResponse>>> = _followingList

    fun getFollowingList(username: String) {
        viewModelScope.launch {
            repository.remote!!.getFollowingList(username).collect { res ->
                _followingList.value = res
            }
        }
    }
}