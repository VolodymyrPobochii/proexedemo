package com.pobochii.someapp.di

import com.pobochii.someapp.domain.users.GetUserDetailsUseCase
import com.pobochii.someapp.domain.users.GetUsersSortedUseCase
import com.pobochii.someapp.domain.users.GetUsersUseCase
import com.pobochii.someapp.domain.users.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@InstallIn(ViewModelComponent::class)
@Module
object UseCasesModule {

    @ViewModelScoped
    @Provides
    fun provideGetUsersUseCase(repository: UsersRepository): GetUsersUseCase {
        return GetUsersUseCase(repository)
    }

    @ViewModelScoped
    @Provides
    fun provideGetUsersSortedUseCase(getUsers: GetUsersUseCase): GetUsersSortedUseCase {
        return GetUsersSortedUseCase(getUsers)
    }

    @ViewModelScoped
    @Provides
    fun provideGetUserDetailsUseCase(repository: UsersRepository): GetUserDetailsUseCase {
        return GetUserDetailsUseCase(repository)
    }
}