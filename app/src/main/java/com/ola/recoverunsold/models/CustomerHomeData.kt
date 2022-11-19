package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CustomerHomeData(
    @Json(name = "offers") val offers: List<Offer>,
    @Json(name = "distributors") val distributors: List<DistributorInformation>,
    @Json(name = "orderStats") val orderStats: CustomerOrderStats? = null
)