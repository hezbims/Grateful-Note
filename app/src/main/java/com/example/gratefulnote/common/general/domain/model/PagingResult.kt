package com.example.gratefulnote.common.general.domain.model

sealed class PagingResult<T> {
    class Succeed<T>(val data: T,
                     val nextPage: Int?,
                     val prevPage: Int?,) : PagingResult<T>()

    class Error<T> : PagingResult<T>()
}