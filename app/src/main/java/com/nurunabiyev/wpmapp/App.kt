package com.nurunabiyev.wpmapp

import android.app.Application
import androidx.room.Room
import com.nurunabiyev.wpmapp.database.AppDatabase

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
            private set
    }
}