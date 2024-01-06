package com.example.gratefulnote.common.data

sealed class ResponseWrapper {
    class ResponseSucceed<T>(val data : T? = null) : ResponseWrapper()
    class ResponseError(val exception : Exception) : ResponseWrapper()
    class ResponseLoading(val message : String? = null) : ResponseWrapper()
}
