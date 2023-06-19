package com.albertbonet.pokeapp.data

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

sealed interface Error {
    class Server(val code: Int) : Error
    object Connectivity : Error
    class NullPointer(val stacktrace: String) : Error
    class Unknown(val message: String) : Error
    object SocketTimeout : Error
}

fun Throwable.toError(): Error = when (this) {
    is SocketTimeoutException -> Error.SocketTimeout
    is IOException -> Error.Connectivity
    is HttpException -> Error.Server(code())
    is NullPointerException -> Error.NullPointer(message ?: "")
    else -> Error.Unknown(message ?: "")
}

inline fun <T> tryCall(action: () -> T): Error? = try {
    action()
    null
} catch (e: Exception) {
    e.toError()
}