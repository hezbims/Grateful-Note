package com.example.gratefulnote.common._hilt_module

import com.example.gratefulnote.common.data.SystemTimeProvider
import com.example.gratefulnote.common.domain.ITimeProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TimeProviderModule {
    @Binds
    abstract fun providerTimeProvider(systemTimeProvider: SystemTimeProvider) : ITimeProvider
}