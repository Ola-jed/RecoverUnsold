package com.ola.recoverunsold.utils.extensions

import com.ola.recoverunsold.api.core.ApiCallResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

/**
 * Convert a data type to Multipart/Form-data request body element
 */
fun <T> T.toMultipartRequestBody(): RequestBody {
    return this.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
}

fun <T> Response<T>.toApiCallResult(onSuccess: (() -> Unit)? = null): ApiCallResult<T> {
    return if (this.isSuccessful) {
        if (onSuccess != null) {
            onSuccess()
        }
        ApiCallResult.Success(_data = this.body())
    } else {
        ApiCallResult.Error(code = this.code())
    }
}

@JvmName("toApiCallResultVoid")
fun Response<Void>.toApiCallResult(onSuccess: (() -> Unit)? = null): ApiCallResult<Unit> {
    return if (this.isSuccessful) {
        if (onSuccess != null) {
            onSuccess()
        }
        ApiCallResult.Success(_data = Unit)
    } else {
        ApiCallResult.Error(code = this.code())
    }
}