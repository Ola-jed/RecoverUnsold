package com.ola.recoverunsold.api.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DistributorUpdateRequest(
    @Json(name = "username") val username: String,
    @Json(name = "phone") val phone: String,
    @Json(name = "taxId") val taxId: String,
    @Json(name = "rccm") val rccm: String,
    @Json(name = "websiteUrl") val websiteUrl: String?
)