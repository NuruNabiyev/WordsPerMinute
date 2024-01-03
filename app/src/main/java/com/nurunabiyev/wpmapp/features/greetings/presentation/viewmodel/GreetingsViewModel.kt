package com.nurunabiyev.wpmapp.features.greetings.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nurunabiyev.wpmapp.core.user.domain.User
import com.nurunabiyev.wpmapp.features.greetings.domain.RegisterUserUC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class GreetingsViewModel(
    private val registerUserUC: RegisterUserUC
): ViewModel() {

    fun registerUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            registerUserUC(User(Random.nextInt(), username))
        }
    }
}