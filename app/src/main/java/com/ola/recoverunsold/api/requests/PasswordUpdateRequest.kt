package com.ola.recoverunsold.api.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordUpdateRequest(
    @Json(name = "newPassword") val newPassword: String,
    @Json(name = "oldPassword") val oldPassword: String
)
