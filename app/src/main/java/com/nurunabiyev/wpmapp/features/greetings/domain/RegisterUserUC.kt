package com.nurunabiyev.wpmapp.features.greetings.domain

import com.nurunabiyev.wpmapp.core.user.domain.IUserRepository
import com.nurunabiyev.wpmapp.core.user.domain.User
import javax.inject.Inject

class RegisterUserUC @Inject constructor(
    private val userRepo: IUserRepository,
) {
    suspend operator fun invoke(user: User) {
        return userRepo.registerNewUser(user)
    }
}