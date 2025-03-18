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
    /**
     * Provides an instance of [BookmarkQuestionUseCase].
     *
     * This function is annotated with `@Provides` and `@ViewModelScoped`,
     * indicating that it's a Dagger Hilt provider for a dependency that
     * should be scoped to the ViewModel lifecycle.
     *
     * @param repo The [QuizRepository] instance that the [BookmarkQuestionUseCase] will depend on.
     *             This repository is responsible for data access related to quiz questions.
     * @return A new instance of [BookmarkQuestionUseCase], which encapsulates the logic for
     *         bookmarking quiz questions.
     */
    @Provides
    @ViewModelScoped
    fun provideBookmarkQuestionUseCase(repo: QuizRepository): BookmarkQuestionUseCase {
        return BookmarkQuestionUseCase(repo)
    }

    /**
     * Provides an instance of [CheckBookmarkStatusUseCase] for use in the ViewModel.
     *
     * This function is annotated with `@Provides` and `@ViewModelScoped`, indicating that it's a
     * dependency provider within a Hilt module and that the provided instance will be scoped to the
     * lifecycle of the ViewModel.
     *
     * The [CheckBookmarkStatusUseCase] is responsible for checking the bookmark status of a particular
     * quiz or question. This might involve querying a database or other data source to see if a specific
     * item has been bookmarked by the user.
     *
     * @param repo The [QuizRepository] instance which is used by the [CheckBookmarkStatusUseCase] to interact
     *             with the underlying data layer (e.g., database or network). This repository is expected
     *             to handle the logic of checking the bookmark status.
     * @return A new instance of [CheckBookmarkStatusUseCase] that can be injected into ViewModels.
     */
    @Provides
    @ViewModelScoped
    fun provideCheckBookmarkStatusUseCase(repo: QuizRepository): CheckBookmarkStatusUseCase {
        return CheckBookmarkStatusUseCase(repo)
    }

    /**
     * Provides an instance of [GetQuizzesUseCase].
     *
     * This function is annotated with `@Provides` and `@ViewModelScoped`, indicating that it's a
     * Dagger provider method and that the provided instance should be scoped to the ViewModel.
     *
     * It is responsible for creating and returning a [GetQuizzesUseCase] object, which is then
     * available for injection into other parts of the application, such as ViewModels.
     *
     * The [GetQuizzesUseCase] is dependent on a [QuizRepository], which is injected as a parameter
     * to this function. This ensures that the UseCase has access to the necessary data source.
     *
     * @param repo The [QuizRepository] instance that the [GetQuizzesUseCase] will use to fetch quizzes.
     * @return An instance of [GetQuizzesUseCase].
     */
    @Provides
    @ViewModelScoped
    fun provideGetQuizzesUseCase(repo: QuizRepository): GetQuizzesUseCase {
        return GetQuizzesUseCase(repo)
    }

    /**
     * Provides an instance of [UnbookmarkQuestionUseCase].
     *
     * This function is a Dagger provider that creates and provides an instance of the
     * `UnbookmarkQuestionUseCase`. It's annotated with `@Provides` and `@ViewModelScoped`,
     * indicating that Dagger should manage the lifecycle of this dependency within the scope
     * of a ViewModel. This means a single instance will be provided for each ViewModel
     * instance that requests it.
     *
     * The `UnbookmarkQuestionUseCase` is responsible for handling the logic of unbookmarking
     * a specific question.  It depends on a `QuizRepository` to perform the underlying
     * data operations.
     *
     * @param repo The [QuizRepository] instance used by the `UnbookmarkQuestionUseCase` to
     *             interact with the data layer. This is injected by Dagger.
     * @return A new instance of [UnbookmarkQuestionUseCase].
     */
    @Provides
    @ViewModelScoped
    fun provideUnbookmarkQuestionUseCase(repo: QuizRepository): UnbookmarkQuestionUseCase {
        return UnbookmarkQuestionUseCase(repo)
    }
}