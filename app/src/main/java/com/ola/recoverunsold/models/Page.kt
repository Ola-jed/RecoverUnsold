package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Page<T>(
    @Json(name = "items") val items: List<T>,
    @Json(name = "pageNumber") val pageNumber: Int,
    @Json(name = "pageSize") val pageSize: Int,
    @Json(name = "hasNext") val hasNext: Boolean,
    @Json(name = "total") val total: Int,
    @Json(name = "baseUrl") val baseUrl: String? = null,
    @Json(name = "previousUrl") val previousUrl: String? = null,
    @Json(name = "nextUrl") val nextUrl: String? = null
)