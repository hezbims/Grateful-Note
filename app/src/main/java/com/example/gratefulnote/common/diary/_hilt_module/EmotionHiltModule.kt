package com.example.gratefulnote.common.diary._hilt_module

import com.example.gratefulnote.common.diary.data.repository.DiaryRepositoryImpl
import com.example.gratefulnote.common.diary.domain.repository.IDiaryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class EmotionHiltModule {
    @Binds
    abstract fun bindEmotionRepository(repo : DiaryRepositoryImpl) : IDiaryRepository
}