package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class DistributorInformation(
    @Json(name = "id") val id: String,
    @Json(name = "username") val username: String,
    @Json(name = "email") val email: String,
    @Json(name = "phone") val phone: String,
    @Json(name = "websiteUrl") val websiteUrl: String? = null,
    @Json(name = "createdAt") val createdAt: Date,
    @Json(name = "offers") val offers: List<Offer>? = null
)