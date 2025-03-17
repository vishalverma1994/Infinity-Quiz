package com.infinityquiz.quizModule.data.repository

import com.infinityquiz.quizModule.data.dto.QuizDto
import com.infinityquiz.quizModule.data.local.QuizDao
import com.infinityquiz.quizModule.data.local.QuizDataStore
import com.infinityquiz.quizModule.data.local.QuizEntity
import com.infinityquiz.quizModule.data.network.QuizApi
import com.infinityquiz.quizModule.domain.repository.QuizRepo
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
    private val quizDataStore: QuizDataStore,
    private val ioCoroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): QuizRepo {


    override suspend fun getQuizzes(): Either<NetworkError, QuizDto> {
        return withContext(ioCoroutineDispatcher) {
            return@withContext try {
                val response = apiService.getQuizList()
                if (response.isSuccessful) {
                    val body = response.body()
                    return@withContext if (body != null) {
                        // todo convert body dto to domain model using mapper here
                        if (body.quizList.isNullOrEmpty()) {
                            Either.Failure(NetworkError.EmptyDataFound)
                        } else
                            Either.Success(body)
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
                        return@withContext Either.Failure(NetworkError.ServerError(e.code()?:0, e.message?:""))
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

    private suspend fun insertQuizList(quizList: List<QuizEntity>) {
        quizDao.insertAll(quizList)
    }
}