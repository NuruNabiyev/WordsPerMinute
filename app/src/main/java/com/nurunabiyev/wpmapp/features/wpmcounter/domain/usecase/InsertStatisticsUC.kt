package com.nurunabiyev.wpmapp.features.wpmcounter.domain.usecase

import com.nurunabiyev.wpmapp.features.wpmcounter.domain.Stats
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.repository.IAnalyticsRepository
import javax.inject.Inject

class InsertStatisticsUC @Inject constructor(
    private val analyticsRepo: IAnalyticsRepository
) {
    suspend operator fun invoke(currentStat: Stats) {
        return analyticsRepo.insertNewStats(currentStat)
    }
}