package com.nurunabiyev.wpmapp.core.user.data

import androidx.compose.runtime.saveable.mapSaver
import com.nurunabiyev.wpmapp.core.user.domain.User

/**
 * Used to retrieve/save user during orientation change for JetpackCompose
 */
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