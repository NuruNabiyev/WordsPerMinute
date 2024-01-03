package com.nurunabiyev.wpmapp.features.greetings.domain

import com.nurunabiyev.wpmapp.core.user.domain.IUserRepository
import com.nurunabiyev.wpmapp.core.user.domain.User

class GetUserUC(
    private val userRepo: IUserRepository,
) {
    suspend operator fun invoke(username: String): User? {
        return userRepo.getUser(username)
    }
}