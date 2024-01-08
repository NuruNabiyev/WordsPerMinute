package com.nurunabiyev.wpmapp.features.wpmcounter.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AnalyticsDao {
    @Query("DELETE FROM statistics")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg statsDb: StatsDb)
}