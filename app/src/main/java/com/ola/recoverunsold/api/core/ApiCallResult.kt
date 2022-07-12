package com.ola.recoverunsold.api.core

sealed class ApiCallResult<out T>(
    val status: ApiStatus,
    val data: T? = null,
    val statusCode: Int? = null
) {
    data class Success<out R>(val _data: R?) : ApiCallResult<R>(
        status = ApiStatus.SUCCESS,
        data = _data
    )

    data class Error(val code: Int?) : ApiCallResult<Nothing>(
        status = ApiStatus.ERROR,
        statusCode = code
    )

    data class Loading<out R>(val isLoading: Boolean = true) : ApiCallResult<R>(
        status = ApiStatus.LOADING
    )

    object Inactive : ApiCallResult<Nothing>(status = ApiStatus.INACTIVE)
}