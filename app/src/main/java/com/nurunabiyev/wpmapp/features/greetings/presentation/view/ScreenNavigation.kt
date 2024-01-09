package com.nurunabiyev.wpmapp.features.greetings.presentation.view

sealed class ScreenNavigation(val route: String) {
    data object Greetings : ScreenNavigation("greetings")
    data object Typing : ScreenNavigation("typing")
}
