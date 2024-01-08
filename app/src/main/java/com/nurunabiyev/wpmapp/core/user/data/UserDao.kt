package com.nurunabiyev.wpmapp.core.user.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username LIKE :username LIMIT 1")
    suspend fun findByUsername(username: String): UserDb?

    @Insert
    suspend fun insertAll(vararg users: UserDb)
}