package com.nurunabiyev.wpmapp.core.di

import com.nurunabiyev.wpmapp.App
import com.nurunabiyev.wpmapp.core.user.data.UserRepositoryImpl
import com.nurunabiyev.wpmapp.database.AppDatabase
import com.nurunabiyev.wpmapp.features.greetings.domain.GetUserUC
import com.nurunabiyev.wpmapp.features.greetings.domain.RegisterUserUC

/**
 * TODO use dagger/hilt, static for now
 */

val db by lazy {
    AppDatabase.getDatabase(App.instance) as AppDatabase
}

val userRepo by lazy {
    UserRepositoryImpl(db.userDao())
}

val registerUC by lazy { RegisterUserUC(userRepo) }
val getUserUC by lazy { GetUserUC(userRepo) }