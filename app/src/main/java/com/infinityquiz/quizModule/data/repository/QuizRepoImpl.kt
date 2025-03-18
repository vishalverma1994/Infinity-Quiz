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

data class QuizRepoImpl @Inject constructor(
    private val apiService: QuizApi,
    private val quizDao: QuizDao,
    private val ioCoroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : QuizRepository {


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


    override suspend fun getAllBookmarkedQuestions(): List<Question> {
        return quizDao.getBookmarkQuizList().map { it.toQuestion() }
    }

    override suspend fun bookmarkQuestion(question: Question) {
        quizDao.saveBookmark(question.toQuizEntity())
    }

    override suspend fun unbookmarkQuestion(question: Question) {
        quizDao.removeBookmark(question.toQuizEntity())
    }

    override suspend fun isQuestionBookmarked(questionId: String): Boolean {
        return quizDao.isQuestionBookmarked(questionId)
    }
}