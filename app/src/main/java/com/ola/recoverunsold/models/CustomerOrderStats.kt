package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CustomerOrderStats(
    @Json(name = "totalOrders") val totalOrders: Int,
    @Json(name = "totalOrdersAmount") val totalOrdersAmount: Double
)
