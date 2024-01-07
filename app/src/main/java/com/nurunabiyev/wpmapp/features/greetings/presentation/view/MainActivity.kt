package com.nurunabiyev.wpmapp.features.greetings.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nurunabiyev.wpmapp.core.user.domain.User
import com.nurunabiyev.wpmapp.features.wpmcounter.presentation.view.TypingScreen
import com.nurunabiyev.wpmapp.ui.theme.WpmAppTheme

val userSaver = run {
    val id = "id"
    val username = "username"
    mapSaver(
        save = { mapOf(id to it.id, username to it.username) },
        restore = {
            User(
                id = it[id] as Int,
                username = it[username] as String
            )
        }
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WpmAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var screen by rememberSaveable { mutableStateOf(Screen.Greetings) }
                    var user by rememberSaveable(stateSaver = userSaver) { mutableStateOf(User(-1, "")) }
                    when (screen) {
                        Screen.Greetings -> GreetingScreen(onUserRegistered = {
                            user = it
                            screen = Screen.Type
                        })
                        Screen.Type -> TypingScreen(user)
                    }
                }
            }
        }
    }

    /**
     * Simple navigation is used in this app, instead of a library
     * @see https://developer.android.com/jetpack/compose/navigation
     */
    enum class Screen {
        Greetings, Type
    } 
}

