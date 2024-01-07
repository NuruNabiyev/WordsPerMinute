package com.nurunabiyev.wpmapp.features.greetings.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nurunabiyev.wpmapp.core.user.domain.User
import com.nurunabiyev.wpmapp.features.greetings.domain.GetUserUC
import com.nurunabiyev.wpmapp.features.greetings.domain.RegisterUserUC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GreetingsViewModel(
    private val registerUserUC: RegisterUserUC,
    private val getUserUC: GetUserUC
): ViewModel() {
    var error by mutableStateOf(NO_ERROR)
        private set
    var registrationCompleted by mutableStateOf<User?>(null)

    fun registerUser(username: String) {
        error = validate(username)
        if (error.isNotEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            if (getUserUC(username) != null) {
                delay(100) // delay to let user see that error has updated
                error = "User exists"
                return@launch
            }
            val user = User(Random.nextInt(), username)
            registerUserUC(user)
            registrationCompleted = user
        }
    }

    private fun validate(username: String): String {
        if (username.isEmpty()) return "Can't be empty"
        if (username.length > 20) return "Too long"
        return NO_ERROR
    }

    fun resetError() {
        error = NO_ERROR
    }

    companion object {
        const val NO_ERROR = ""
    }
}