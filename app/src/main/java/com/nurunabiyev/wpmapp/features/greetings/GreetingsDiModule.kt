package com.nurunabiyev.wpmapp.features.greetings

import com.nurunabiyev.wpmapp.core.user.domain.IUserRepository
import com.nurunabiyev.wpmapp.features.greetings.domain.GetUserUC
import com.nurunabiyev.wpmapp.features.greetings.domain.RegisterUserUC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object GreetingsDiModule {

    @Provides
    fun providesRegisterUserUseCase(repo: IUserRepository): RegisterUserUC {
        return RegisterUserUC(repo)
    }

    @Provides
    fun providesGetUserUserUseCase(repo: IUserRepository): GetUserUC {
        return GetUserUC(repo)
    }
}