package com.dimasnoufal.githubuser.data

import android.util.Log
import com.dimasnoufal.githubuser.data.network.api.UserApi
import com.dimasnoufal.githubuser.data.network.handler.NetworkResult
import com.dimasnoufal.githubuser.model.DetailUserResponse
import com.dimasnoufal.githubuser.model.GithubResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource (private val userApi: UserApi){
    suspend fun getUser(username: String): Flow<NetworkResult<GithubResponse>> = flow {
        emit(NetworkResult.Loading(true))
        try {
            val user = userApi.getUsername(username)
            if (user.items!!.isEmpty()) {
                emit(NetworkResult.Error("User list not found"))
            } else {
                emit(NetworkResult.Success(user))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("remoteError", "${e.message}")
            emit(NetworkResult.Error("Something went wrong. Please check log."))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getDetailUser(username: String): Flow<NetworkResult<DetailUserResponse>> = flow {
        emit(NetworkResult.Loading(true))
        try {
            val detailuser = userApi.getDetailUser(username)
            if (detailuser.login == null) {
                emit(NetworkResult.Error("Detail User list not found"))
            } else {
                emit(NetworkResult.Success(detailuser))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("remoteError", "${e.message}")
            emit(NetworkResult.Error("Something went wrong. Please check log."))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getFollowersList(username: String): Flow<NetworkResult<ArrayList<DetailUserResponse>>> = flow {
        emit(NetworkResult.Loading(true))
        try {
            val follower = userApi.getFollowersList(username)
            if (follower.isEmpty()) {
                emit(NetworkResult.Error("Detail User list not found"))
            } else {
                emit(NetworkResult.Success(follower))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("remoteError", "${e.message}")
            emit(NetworkResult.Error("Something went wrong. Please check log."))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getFollowingList(username: String): Flow<NetworkResult<ArrayList<DetailUserResponse>>> = flow {
        emit(NetworkResult.Loading(true))
        try {
            val following = userApi.getFollowingList(username)
            if (following.isEmpty()) {
                emit(NetworkResult.Error("Detail User list not found"))
            } else {
                emit(NetworkResult.Success(following))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("remoteError", "${e.message}")
            emit(NetworkResult.Error("Something went wrong. Please check log."))
        }
    }.flowOn(Dispatchers.IO)
}