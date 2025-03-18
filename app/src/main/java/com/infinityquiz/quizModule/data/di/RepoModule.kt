package com.infinityquiz.quizModule.data.di

import com.infinityquiz.quizModule.data.local.QuizDao
import com.infinityquiz.quizModule.data.network.QuizApi
import com.infinityquiz.quizModule.data.repository.QuizRepoImpl
import com.infinityquiz.quizModule.domain.repository.QuizRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
class RepoModule {
    @Provides
    @ViewModelScoped
    fun provideQuizRepoImpl(apiService: QuizApi, quizDao: QuizDao):QuizRepository{
        return QuizRepoImpl(apiService= apiService, quizDao=quizDao)
    }
}