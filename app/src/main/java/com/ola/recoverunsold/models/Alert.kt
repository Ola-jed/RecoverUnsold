package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Alert(
    @Json(name = "id") val id: String,
    @Json(name = "alertType") val alertType: AlertType,
    @Json(name = "distributorInformation") val distributorInformation: DistributorInformation? = null
)