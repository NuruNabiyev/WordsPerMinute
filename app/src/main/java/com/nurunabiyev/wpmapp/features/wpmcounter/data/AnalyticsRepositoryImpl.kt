package com.nurunabiyev.wpmapp.features.wpmcounter.data

import com.nurunabiyev.wpmapp.features.wpmcounter.domain.Stats
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.repository.IAnalyticsRepository

class AnalyticsRepositoryImpl(private val analyticsDao: AnalyticsDao) : IAnalyticsRepository {
    private var previousInserted: Stats? = null

    override suspend fun insertNewStats(stats: Stats) {
        if (previousInserted == stats) {
            return
        }
        analyticsDao.insertAll(stats.toStatsDb())
        previousInserted = stats
    }

    override suspend fun deleteAll() {
        analyticsDao.deleteAll()
    }
}