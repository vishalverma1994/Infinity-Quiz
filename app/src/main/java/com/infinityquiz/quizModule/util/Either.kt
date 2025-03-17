package com.infinityquiz.quizModule.util

sealed class Either<out L : NetworkError, out R>() {
    data class Failure<out L : NetworkError>(val error: L) : Either<L, Nothing>()
    data class Success<out R>(val data: R) : Either<Nothing, R>()
}

sealed interface Error
sealed interface NetworkError : Error {
    data object NoBodyFound : NetworkError
    data object EmptyDataFound : NetworkError
    data object NoInternet : NetworkError
    data class UnknownError (val message:String): NetworkError
    data class ServerError(val code:Int, val message:String) : NetworkError
}