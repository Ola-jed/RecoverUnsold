package com.ola.recoverunsold.api.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CustomerUpdateRequest(
    @Json(name = "username") val username: String,
    @Json(name = "firstName") val firstName: String?,
    @Json(name = "lastName") val lastName: String?
)