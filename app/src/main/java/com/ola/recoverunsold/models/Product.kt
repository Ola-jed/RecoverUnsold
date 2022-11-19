package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class Product(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "offerId") val offerId: String,
    @Json(name = "images") val images: List<ProductImage>,
    @Json(name = "createdAt") val createdAt: Date
)
