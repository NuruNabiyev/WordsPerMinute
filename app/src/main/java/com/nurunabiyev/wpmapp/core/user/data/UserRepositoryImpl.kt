package com.nurunabiyev.wpmapp.core.user.data

import com.nurunabiyev.wpmapp.core.user.domain.IUserRepository
import com.nurunabiyev.wpmapp.core.user.domain.User

class UserRepositoryImpl(private val userDao: UserDao) : IUserRepository {

    override suspend fun registerNewUser(user: User) {
        userDao.insertAll(user.toUserDb())
    }

    override suspend fun getUser(username: String): User? {
        return userDao.findByUsername(username)?.toUser()
    }
}