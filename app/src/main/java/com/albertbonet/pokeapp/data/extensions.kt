package com.albertbonet.pokeapp.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.albertbonet.pokeapp.domain.Error
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

fun Throwable.toError(): Error = when (this) {
    is SocketTimeoutException -> Error.SocketTimeout
    is IOException -> Error.Connectivity
    is HttpException -> Error.Server(code())
    is NullPointerException -> Error.NullPointer(message ?: "")
    else -> Error.Unknown(message ?: "")
}

suspend fun <T> tryCall(action: suspend () -> T): Either<Error, T> = try {
    action().right()
} catch (e: Exception) {
    e.toError().left()
}