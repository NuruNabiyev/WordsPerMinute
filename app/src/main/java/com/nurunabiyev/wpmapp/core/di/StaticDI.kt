package com.nurunabiyev.wpmapp.core.di

import com.nurunabiyev.wpmapp.App
import com.nurunabiyev.wpmapp.core.user.data.UserRepositoryImpl
import com.nurunabiyev.wpmapp.database.AppDatabase

/**
 * TODO use dagger/hilt, static for now
 */

val db by lazy {
    AppDatabase.getDatabase(App.instance) as AppDatabase
}

val userRepo by lazy {
    UserRepositoryImpl(db.userDao())
}