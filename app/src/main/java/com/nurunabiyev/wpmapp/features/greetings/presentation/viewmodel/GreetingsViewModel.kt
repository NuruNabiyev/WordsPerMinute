package com.nurunabiyev.wpmapp.features.greetings.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nurunabiyev.wpmapp.core.user.domain.User
import com.nurunabiyev.wpmapp.features.greetings.domain.GetUserUC
import com.nurunabiyev.wpmapp.features.greetings.domain.RegisterUserUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GreetingsViewModel @Inject constructor(
    private val getUserUC: GetUserUC,
    private val registerUC: RegisterUserUC,
) : ViewModel() {
    var usernameText = mutableStateOf(NO_ERROR)
    var error = mutableStateOf(NO_ERROR)
    var registrationCompleted by mutableStateOf<User?>(null)

    fun registerUser() {
        error.value = validate(usernameText.value)
        if (error.value.isNotEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            if (getUserUC(usernameText.value) != null) {
                delay(100) // delay to let user see that error has updated
                error.value = "User exists"
                return@launch
            }
            val user = User(Random.nextInt(), usernameText.value)
            registerUC(user)
            registrationCompleted = user
        }
    }

    private fun validate(username: String): String {
        if (username.isEmpty()) return "Can't be empty"
        if (username.length > 20) return "Too long"
        return NO_ERROR
    }

    fun resetError() {
        error.value = NO_ERROR
    }

    fun reset() = viewModelScope.launch {
        registrationCompleted = null
        resetError()
        usernameText.value = NO_ERROR
    }

    companion object {
        const val NO_ERROR = ""
    }
}