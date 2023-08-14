package com.ola.recoverunsold.api.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReportCreateRequest(
    @Json(name = "reason") val reason: String,
    @Json(name = "description") val description: String? = null
)
