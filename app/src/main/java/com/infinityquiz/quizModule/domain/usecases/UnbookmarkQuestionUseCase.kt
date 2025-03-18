package com.infinityquiz.quizModule.domain.usecases

import com.infinityquiz.quizModule.domain.model.Question
import com.infinityquiz.quizModule.domain.repository.QuizRepository
import javax.inject.Inject

/**
 * Use case to remove a bookmarked question from the repository.
 *
 * This use case encapsulates the logic for unbookmarking a specific [Question].
 * It interacts with the [QuizRepository] to perform the underlying data manipulation.
 *
 * @property repository The repository responsible for managing bookmarked questions.
 */
class UnbookmarkQuestionUseCase @Inject constructor(private val repository: QuizRepository) {
    suspend operator fun invoke(question: Question) {
        repository.unbookmarkQuestion(question)
    }
}