package com.nurunabiyev.wpmapp.core.user.domain


interface IUserRepository {
    suspend fun registerNewUser(user: User)
    suspend fun getUser(username: String): User?
}