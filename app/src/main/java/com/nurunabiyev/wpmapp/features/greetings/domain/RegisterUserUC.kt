package com.nurunabiyev.wpmapp.features.greetings.domain

import com.nurunabiyev.wpmapp.core.user.domain.IUserRepository
import com.nurunabiyev.wpmapp.core.user.domain.User

/**
 * I follow clean architecture,
 *  where use cases are intermediary between viewmodel and repository
 */


class RegisterUserUC(
    private val userRepo: IUserRepository,
) {
    operator fun invoke(user: User) {
        return userRepo.registerNewUser(user)
    }
}