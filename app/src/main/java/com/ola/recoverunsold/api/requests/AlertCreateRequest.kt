package com.ola.recoverunsold.api.requests

import com.ola.recoverunsold.models.AlertType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlertCreateRequest(
    @Json(name = "alertType") val alertType: AlertType,
    @Json(name = "distributorId") val distributorId: String? = null
)
