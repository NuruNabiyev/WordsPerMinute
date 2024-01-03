package com.nurunabiyev.wpmapp.core.user.domain


interface IUserRepository {
    fun registerNewUser(user: User)
}