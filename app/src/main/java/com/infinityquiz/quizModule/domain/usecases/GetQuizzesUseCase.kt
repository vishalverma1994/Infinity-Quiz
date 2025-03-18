package com.infinityquiz.quizModule.domain.usecases

import com.infinityquiz.quizModule.domain.model.Question
import com.infinityquiz.quizModule.domain.repository.QuizRepository
import com.infinityquiz.quizModule.util.Either
import com.infinityquiz.quizModule.util.NetworkError
import javax.inject.Inject

/**
 * `GetQuizzesUseCase` is a use case responsible for retrieving a list of quizzes (represented as a list of `Question` objects).
 * It interacts with the `QuizRepository` to fetch the quiz data.
 *
 * This use case encapsulates the logic for obtaining quizzes, separating it from the UI and data layers.
 * It handles potential network errors and returns the result wrapped in an `Either` data type.
 *
 * @property repository The `QuizRepository` instance responsible for fetching quiz data from the data source.
 *                     Injected via constructor injection.
 */
class GetQuizzesUseCase @Inject constructor(private val repository: QuizRepository) {
    suspend operator fun invoke(): Either<NetworkError, List<Question>> {
        return repository.getQuizzes()
    }
}