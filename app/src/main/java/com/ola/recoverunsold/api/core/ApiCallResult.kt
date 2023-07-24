package com.ola.recoverunsold.api.core

sealed class ApiCallResult<out T>(
    val status: ApiStatus,
    val data: T? = null,
    val statusCode: Int = StatusCode.Unknown.code
) {
    data class Success<out R>(val _data: R?) : ApiCallResult<R>(
        status = ApiStatus.SUCCESS,
        data = _data,
        statusCode = 200
    )

    data class Error(val code: Int) : ApiCallResult<Nothing>(
        status = ApiStatus.ERROR,
        statusCode = code
    )

    object Loading : ApiCallResult<Nothing>(status = ApiStatus.LOADING)

    object Inactive : ApiCallResult<Nothing>(status = ApiStatus.INACTIVE)
}