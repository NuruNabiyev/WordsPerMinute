package com.nurunabiyev.wpmapp.features.greetings.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nurunabiyev.wpmapp.core.user.data.userSaver
import com.nurunabiyev.wpmapp.core.user.domain.User
import com.nurunabiyev.wpmapp.features.wpmcounter.presentation.view.TypingScreen
import com.nurunabiyev.wpmapp.ui.theme.WpmAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WpmAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    var user by rememberSaveable(stateSaver = userSaver) { mutableStateOf(User("Test")) }

                    NavHost(
                        navController,
                        startDestination = ScreenNavigation.Greetings.route
                    ) {
                        composable(ScreenNavigation.Greetings.route) {
                            GreetingScreen(onUserRegistered = {
                                user = it
                                navController.navigate(ScreenNavigation.Typing.route)
                            })
                        }
                        composable(ScreenNavigation.Typing.route) {
                            TypingScreen(navController, user)
                        }
                    }
                }
            }
        }
    }

    /**
     * Simple navigation is used in this app, instead of a library
     * @see https://developer.android.com/jetpack/compose/navigation
     */
    private enum class Screen {
        Greetings, Type
    } 
}

