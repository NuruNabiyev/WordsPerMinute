package com.nurunabiyev.wpmapp.core.user.data

import androidx.compose.runtime.saveable.mapSaver
import com.nurunabiyev.wpmapp.core.user.domain.User

/**
 * Used to retrieve/save user during orientation change for JetpackCompose
 */
val userSaver = run {
    val username = "username"
    mapSaver(
        save = { mapOf(username to it.username) },
        restore = { User(username = it[username] as String) }
    )
}