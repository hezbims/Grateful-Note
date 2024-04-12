package com.example.gratefulnote.common.domain

sealed class ResponseWrapper<T> {
    class ResponseSucceed<T>(val data : T? = null) : ResponseWrapper<T>()
    class ResponseError<T>(val exception : Throwable? = null) : ResponseWrapper<T>() {
        override fun toString(): String {
            return exception?.toString() ?: "Unknown Error"
        }
    }
    class ResponseLoading<T>(val message : String? = null) : ResponseWrapper<T>() {
        override fun toString(): String {
            return "Response loading, message : $message"
        }
    }
}
