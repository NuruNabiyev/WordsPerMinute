package com.nurunabiyev.wpmapp.features.wpmcounter.domain.usecase

import com.nurunabiyev.wpmapp.features.wpmcounter.domain.repository.IAnalyticsRepository
import javax.inject.Inject

class DeleteStatisticsUC @Inject constructor(
    private val analyticsRepo: IAnalyticsRepository
) {
    suspend operator fun invoke() {
        return analyticsRepo.deleteAll()
    }
}