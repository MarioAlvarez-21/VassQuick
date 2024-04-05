package com.vassteam2.vassquick.domain.model

sealed class Response<out T> {
    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val exception: Exception, val message: String? = exception.localizedMessage) : Response<Nothing>()
}

