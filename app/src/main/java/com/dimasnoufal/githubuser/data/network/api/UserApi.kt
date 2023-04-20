package com.dimasnoufal.githubuser.data.network.api

import com.dimasnoufal.githubuser.model.DetailUserResponse
import com.dimasnoufal.githubuser.model.GithubResponse
import com.dimasnoufal.githubuser.utils.AppConstant.API_TOKEN
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("search/users")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    suspend fun getUsername(@Query("q") username: String): GithubResponse

    @GET("users/{username}")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    suspend fun getDetailUser(@Path("username") username: String): DetailUserResponse

    @GET("users/{username}/followers")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    suspend fun getFollowersList(@Path("username") username: String): ArrayList<DetailUserResponse>

    @GET("users/{username}/following")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    suspend fun getFollowingList(@Path("username") username: String): ArrayList<DetailUserResponse>
}