package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DistributorLabel(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String
)
