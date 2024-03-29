package com.nurunabiyev.wpmapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nurunabiyev.wpmapp.core.user.data.UserDao
import com.nurunabiyev.wpmapp.core.user.data.UserDb
import com.nurunabiyev.wpmapp.features.wpmcounter.data.AnalyticsDao
import com.nurunabiyev.wpmapp.features.wpmcounter.data.StatsDb

@Database(entities = [UserDb::class, StatsDb::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun analyticsDao(): AnalyticsDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDatabase? = null

        fun getDatabase(context: Context): RoomDatabase {
            val temp = INSTANCE
            if (temp != null) {
                return temp
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "room-database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}