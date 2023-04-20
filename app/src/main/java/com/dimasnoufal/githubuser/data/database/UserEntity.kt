package com.dimasnoufal.githubuser.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dimasnoufal.githubuser.model.ItemsItem
import com.dimasnoufal.githubuser.utils.AppConstant.USER_TABLE_NAME
import kotlinx.parcelize.Parcelize

@Entity(tableName = USER_TABLE_NAME)
@Parcelize
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val user: ItemsItem
): Parcelable