package com.nurunabiyev.wpmapp.features.wpmcounter

import com.nurunabiyev.wpmapp.database.AppDatabase
import com.nurunabiyev.wpmapp.features.wpmcounter.data.AnalyticsRepositoryImpl
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.repository.IAnalyticsRepository
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.usecase.DeleteStatisticsUC
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.usecase.InsertStatisticsUC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object WpmCounterDiModule {
    @Provides
    fun providesAnalyticsRepository(db: AppDatabase): IAnalyticsRepository {
        return AnalyticsRepositoryImpl(db.analyticsDao())
    }

    @Provides
    fun providesInsertStatisticsUseCase(repo: IAnalyticsRepository): InsertStatisticsUC {
        return InsertStatisticsUC(repo)
    }

    @Provides
    fun providesDeleteStatisticsUseCase(repo: IAnalyticsRepository): DeleteStatisticsUC {
        return DeleteStatisticsUC(repo)
    }
}