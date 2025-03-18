package com.infinityquiz.quizModule.domain.di

import com.infinityquiz.quizModule.domain.repository.QuizRepository
import com.infinityquiz.quizModule.domain.usecases.BookmarkQuestionUseCase
import com.infinityquiz.quizModule.domain.usecases.CheckBookmarkStatusUseCase
import com.infinityquiz.quizModule.domain.usecases.GetQuizzesUseCase
import com.infinityquiz.quizModule.domain.usecases.UnbookmarkQuestionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideBookmarkQuestionUseCase(repo: QuizRepository): BookmarkQuestionUseCase {
        return BookmarkQuestionUseCase(repo)
    }

    @Provides
    @ViewModelScoped
    fun provideCheckBookmarkStatusUseCase(repo: QuizRepository): CheckBookmarkStatusUseCase {
        return CheckBookmarkStatusUseCase(repo)
    }

    @Provides
    @ViewModelScoped
    fun provideGetQuizzesUseCase(repo: QuizRepository): GetQuizzesUseCase {
        return GetQuizzesUseCase(repo)
    }

    @Provides
    @ViewModelScoped
    fun provideUnbookmarkQuestionUseCase(repo: QuizRepository): UnbookmarkQuestionUseCase {
        return UnbookmarkQuestionUseCase(repo)
    }
}