package com.nurunabiyev.wpmapp.core.user.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nurunabiyev.wpmapp.core.user.domain.User

@Entity(tableName = "users")
data class UserDb(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "username") val username: String
)

fun UserDb.toUser() = User(username)
fun User.toUserDb() = UserDb(username = username)