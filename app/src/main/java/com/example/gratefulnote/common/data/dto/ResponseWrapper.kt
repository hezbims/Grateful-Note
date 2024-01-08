package com.example.gratefulnote.common.data.dto

sealed class ResponseWrapper<T> {
    class ResponseSucceed<T>(val data : T? = null) : ResponseWrapper<T>()
    class ResponseError<T>(val exception : Exception? = null) : ResponseWrapper<T>()
    class ResponseLoading<T>(val message : String? = null) : ResponseWrapper<T>()
}
