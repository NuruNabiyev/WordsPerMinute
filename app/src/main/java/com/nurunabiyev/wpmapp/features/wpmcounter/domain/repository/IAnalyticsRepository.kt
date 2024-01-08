package com.nurunabiyev.wpmapp.features.wpmcounter.domain.repository

import com.nurunabiyev.wpmapp.features.wpmcounter.domain.Stats

interface IAnalyticsRepository {
    suspend fun insertNewStats(stats: Stats)
    suspend fun deleteAll()
}