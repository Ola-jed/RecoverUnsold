package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OfferWithRelativeDistance(
    @Json(name = "offer") val offer: Offer,
    @Json(name = "distance") val distance: Double
)