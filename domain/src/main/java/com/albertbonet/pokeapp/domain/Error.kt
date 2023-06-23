package com.albertbonet.pokeapp.domain

sealed interface Error {
    class Server(val code: Int) : Error
    object Connectivity : Error
    class NullPointer(val stacktrace: String) : Error
    class Unknown(val message: String) : Error
    object SocketTimeout : Error
}
