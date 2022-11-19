package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class DistributorHomeData(
    @Json(name = "ordersPerDay") val ordersPerDay: Map<Date, Int>,
    @Json(name = "orders") val orders: List<Order>
)
