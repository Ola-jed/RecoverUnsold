package com.ola.recoverunsold.api.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DistributorRegisterRequest(
    @Json(name = "email") val email: String,
    @Json(name = "taxId") val taxId: String,
    @Json(name = "password") val password: String,
    @Json(name = "phone") val phone: String,
    @Json(name = "rccm") val rccm: String,
    @Json(name = "username") val username: String,
    @Json(name = "websiteUrl") val websiteUrl: String?
)