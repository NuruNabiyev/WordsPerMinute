package com.nurunabiyev.wpmapp.core.user.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<UserDb>

    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<UserDb>

    @Query("SELECT * FROM users WHERE username LIKE :username LIMIT 1")
    fun findByUsername(username: String): UserDb

    @Insert
    fun insertAll(vararg users: UserDb)
}