package com.dimasnoufal.githubuser.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(UserTypeConverter::class)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: UserDatabase? = null

        fun getDatabase(ctx: Context): UserDatabase {
            val tempInstance = instance
            if (tempInstance != null) {
                instance = tempInstance
            }

            synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    ctx.applicationContext, UserDatabase::class.java,
                    "user"
                ).fallbackToDestructiveMigration()
                    .build()

                instance = newInstance
                return newInstance
            }
        }
    }
}