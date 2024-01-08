package com.nurunabiyev.wpmapp.core.di

import android.content.Context
import com.nurunabiyev.wpmapp.core.user.data.UserRepositoryImpl
import com.nurunabiyev.wpmapp.core.user.domain.IUserRepository
import com.nurunabiyev.wpmapp.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppDiModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext) as AppDatabase
    }

    @Provides
    @Singleton
    fun providesUserRepository(db: AppDatabase): IUserRepository {
        return UserRepositoryImpl(db.userDao())
    }
}