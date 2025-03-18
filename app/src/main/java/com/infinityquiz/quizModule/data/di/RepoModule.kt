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
    /**
     * Provides an instance of [QuizRepository] using [QuizRepoImpl] as the concrete implementation.
     *
     * This function is annotated with `@Provides` which tells Hilt how to create an instance of [QuizRepository].
     * It's also annotated with `@ViewModelScoped`, meaning that the created instance will live as long as the ViewModel that depends on it.
     * It takes [QuizApi] and [QuizDao] as dependencies which will be provided by Hilt.
     *
     * @param apiService An instance of [QuizApi] for network requests.
     * @param quizDao An instance of [QuizDao] for database operations.
     * @return An instance of [QuizRepository] which is implemented by [QuizRepoImpl].
     */
    @Provides
    @ViewModelScoped
    fun provideQuizRepoImpl(apiService: QuizApi, quizDao: QuizDao):QuizRepository{
        return QuizRepoImpl(apiService= apiService, quizDao=quizDao)
    }
}