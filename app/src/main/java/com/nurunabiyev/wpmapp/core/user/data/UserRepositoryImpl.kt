package com.nurunabiyev.wpmapp.core.user.data

import com.nurunabiyev.wpmapp.core.user.domain.IUserRepository
import com.nurunabiyev.wpmapp.core.user.domain.User

class UserRepositoryImpl(val userDao: UserDao) : IUserRepository {

    override fun registerNewUser(user: User) {
        userDao.insertAll(user.toUserDb())
    }
}