package com.ola.recoverunsold.api.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class OrderCreateRequest(@Json(name = "withdrawalDate") val withdrawalDate: Date)
