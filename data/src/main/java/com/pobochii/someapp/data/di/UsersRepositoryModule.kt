package com.pobochii.someapp.data.di

import com.pobochii.someapp.data.UsersDataRepository
import com.pobochii.someapp.domain.users.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UsersRepositoryModule {

    @Singleton
    @Binds
    abstract fun provideUsersRepository(repository: UsersDataRepository): UsersRepository
}