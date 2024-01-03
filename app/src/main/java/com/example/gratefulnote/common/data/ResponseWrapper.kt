package com.example.gratefulnote.common.data

sealed class ResponseWrapper {
    class ResponseLoaded<T>(val data : T?) : ResponseWrapper()
    class ResponseError(val exception : Exception) : ResponseWrapper()
    class ResponseLoading(val message : String? = null) : ResponseWrapper()
}
