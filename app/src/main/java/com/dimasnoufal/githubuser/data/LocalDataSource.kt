package com.dimasnoufal.githubuser.data

import com.dimasnoufal.githubuser.data.database.UserDao
import com.dimasnoufal.githubuser.data.database.UserEntity
import kotlinx.coroutines.flow.Flow

class LocalDataSource (private val dao: UserDao) {
    suspend fun insertUser(userEntity: UserEntity) = dao.insertUser(userEntity)

    fun listUser(): Flow<List<UserEntity>> = dao.listUser()

    suspend fun deleteUser(userEntity: UserEntity?) = dao.deleteUser(userEntity)

}