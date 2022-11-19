package com.ola.recoverunsold.api.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordResetRequest(
    @Json(name = "password") val password: String,
    @Json(name = "token") val token: String
)