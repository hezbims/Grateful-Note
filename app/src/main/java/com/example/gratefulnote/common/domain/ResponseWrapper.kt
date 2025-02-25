package com.example.gratefulnote.common.domain

sealed class ResponseWrapper<T> {
    class Succeed<T>(val data : T) : ResponseWrapper<T>()
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

    fun isLoading() : Boolean = this is Loading
    fun isSucceed() : Boolean = this is Succeed
    fun isError() : Boolean = this is Error
}
