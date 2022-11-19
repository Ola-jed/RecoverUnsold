package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class Opinion(
    @Json(name = "id") val id: String,
    @Json(name = "comment") val comment: String,
    @Json(name = "orderId") val orderId: String,
    @Json(name = "createdAt") val createdAt: Date
)
