package com.nurunabiyev.wpmapp.core.user.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nurunabiyev.wpmapp.core.user.domain.User

@Entity(tableName = "users")
data class UserDb(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "username") val username: String
)

fun UserDb.toUser() = User(uid, username)
fun User.toUserDb() = UserDb(id, username)