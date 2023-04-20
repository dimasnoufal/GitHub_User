package com.dimasnoufal.githubuser.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("SELECT * FROM github_user ORDER BY id ASC")
    fun listUser(): Flow<List<UserEntity>>

    @Delete()
    suspend fun deleteUser(userEntity: UserEntity?)

    @Query("DELETE FROM github_user")
    suspend fun deleteAllUser()
}