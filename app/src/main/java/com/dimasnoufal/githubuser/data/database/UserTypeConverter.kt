package com.dimasnoufal.githubuser.data.database

import androidx.room.TypeConverter
import com.dimasnoufal.githubuser.model.ItemsItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun userDataToString(user: ItemsItem): String {
        return gson.toJson(user)
    }

    @TypeConverter
    fun userStringToData(string: String): ItemsItem {
        val listType = object : TypeToken<ItemsItem>() {}.type
        return gson.fromJson(string, listType)
    }
}