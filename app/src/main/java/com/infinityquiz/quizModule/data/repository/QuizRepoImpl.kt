package com.infinityquiz.quizModule.data.repository

import com.infinityquiz.quizModule.data.dto.toQuestion
import com.infinityquiz.quizModule.data.local.QuizDao
import com.infinityquiz.quizModule.data.local.toQuestion
import com.infinityquiz.quizModule.data.local.toQuizEntity
import com.infinityquiz.quizModule.data.network.QuizApi
import com.infinityquiz.quizModule.domain.model.Question
import com.infinityquiz.quizModule.domain.repository.QuizRepository
import com.infinityquiz.quizModule.util.Either
import com.infinityquiz.quizModule.util.NetworkError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

/**
 * Concrete implementation of the [QuizRepository] interface.
 *
 * This class handles fetching quiz data from a remote API, managing the local
 * database for bookmarked questions, and orchestrating the data flow between
 * the two. It also handles network error scenarios and data conversion.
 *
 * @property apiService The [QuizApi] service for making network requests.
 * @property quizDao The [QuizDao] for interacting with the local database.
 * @property ioCoroutineDispatcher The [CoroutineDispatcher] for performing IO-bound
 *                                 operations, defaults to [Dispatchers.IO].
 */
data class QuizRepoImpl @Inject constructor(
    private val apiService: QuizApi,
    private val quizDao: QuizDao,
    private val ioCoroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : QuizRepository {


    /**
     * Retrieves a list of quizzes (questions) from the remote data source.
     *
     * This function attempts to fetch a list of quizzes from the API. It handles
     * various network-related errors and maps the received data to a list of domain
     * `Question` objects.
     *
     * @return An [Either] object representing the result of the operation.
     *         - [Either.Success] containing a [List] of [Question] objects if the request is successful and data is available.
     *         - [Either.Failure] containing a [NetworkError] if the request fails or data is not available. The possible [NetworkError] types are:
     *           - [NetworkError.EmptyDataFound]: If the API returns an empty list.
     *           - [NetworkError.NoBodyFound]: If the API response is successful but the response body is null.
     *           - [NetworkError.ServerError]: If the API returns an HTTP error code. Contains the error code and message.
     *           - [NetworkError.NoInternet]: If there is an `IOException` (e.g., no internet connection).
     *           - [NetworkError.UnknownError]: If any other unexpected error occurs. Contains the error message.
     * @throws CancellationException if the coroutine is cancelled during the execution.
     */
    override suspend fun getQuizzes(): Either<NetworkError, List<Question>> {
        return withContext(ioCoroutineDispatcher) {
            return@withContext try {
                val response = apiService.getQuizList()
                if (response.isSuccessful) {
                    val body = response.body()
                    return@withContext if (body != null) {
                        // todo convert body dto to domain model using mapper here
                        if (body.isEmpty()) {
                            Either.Failure(NetworkError.EmptyDataFound)
                        } else
                            Either.Success(data = body.map { it.toQuestion() })
                    } else {
                        Either.Failure(NetworkError.NoBodyFound)
                    }
                } else {
                    Either.Failure(NetworkError.NoBodyFound)
                }
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> throw e
                    is HttpException -> {
                        return@withContext Either.Failure(NetworkError.ServerError(e.code(), e.message ?: ""))
                    }

                    is IOException -> {
                        return@withContext Either.Failure(NetworkError.NoInternet)
                    }

                    else -> {
                        return@withContext Either.Failure(
                            NetworkError.UnknownError(
                                e.message ?: "no message found"
                            )
                        )
                    }
                }

            }
        }
    }


    /**
     * Retrieves all bookmarked questions from the data source.
     *
     * This function fetches a list of bookmarked quizzes from the underlying data source (e.g., a database)
     * via the `quizDao`. It then transforms each bookmarked quiz entity into a `Question` object
     * using the `toQuestion()` extension function.
     *
     * @return A [List] of [Question] objects representing all the bookmarked questions.
     *         Returns an empty list if there are no bookmarked questions.
     */
    override suspend fun getAllBookmarkedQuestions(): List<Question> {
        return quizDao.getBookmarkQuizList().map { it.toQuestion() }
    }

    /**
     * Bookmarks a given question.
     *
     * This function takes a [Question] object and saves it as a bookmark in the local data source.
     * It internally converts the [Question] to a [QuizEntity] using the `toQuizEntity()` method
     * before saving it via the [quizDao.saveBookmark] function.
     */
    override suspend fun bookmarkQuestion(question: Question) {
        quizDao.saveBookmark(question.toQuizEntity())
    }

    /**
     * Removes a question from the user's bookmarked questions.
     *
     * This function takes a [Question] object and removes its corresponding
     * [QuizEntity] from the database, effectively unbookmarking the question.
     * @throws Exception if there is an error accessing the database or performing the unbookmark operation.
     */
    override suspend fun unbookmarkQuestion(question: Question) {
        quizDao.removeBookmark(question.toQuizEntity())
    }

    /**
     * Checks if a question is bookmarked.
     *
     * This function queries the underlying data access object (DAO) to determine
     * whether a question, identified by its unique [questionId], has been
     * bookmarked by the user.
     * @return `true` if the question is bookmarked, `false` otherwise.
     */
    override suspend fun isQuestionBookmarked(questionId: String): Boolean {
        return quizDao.isQuestionBookmarked(questionId)
    }
}