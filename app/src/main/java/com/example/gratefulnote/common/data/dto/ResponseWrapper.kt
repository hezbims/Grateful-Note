package com.example.gratefulnote.common.data.dto

sealed class ResponseWrapper<T> {
    class ResponseSucceed<T>(val data : T? = null) : ResponseWrapper<T>()
    class ResponseError<T>(val exception : Throwable? = null) : ResponseWrapper<T>()
    class ResponseLoading<T>(val message : String? = null) : ResponseWrapper<T>()
}
