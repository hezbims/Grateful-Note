package com.example.gratefulnote.common.domain

sealed class ResponseWrapper<T> {
    class Succeed<T>(val data : T? = null) : ResponseWrapper<T>()
    class Error<T>(val exception : Throwable? = null) : ResponseWrapper<T>() {
        override fun toString(): String {
            return exception?.message ?: "Unknown Error"
        }
    }
    class Loading<T>(val message : String? = null) : ResponseWrapper<T>() {
        override fun toString(): String {
            return "Response loading, message : $message"
        }
    }
}
