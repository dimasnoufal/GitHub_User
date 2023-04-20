package com.dimasnoufal.githubuser.data.network.handler

sealed class NetworkResult<T>(
    val data: T? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(errorMessage:String, data: T? = null) : NetworkResult<T>(data, errorMessage)
    class Loading<T> (isLoading: Boolean): NetworkResult<T>(isLoading = isLoading)
}