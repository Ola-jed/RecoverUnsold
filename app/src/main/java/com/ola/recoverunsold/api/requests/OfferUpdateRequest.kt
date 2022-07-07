package com.ola.recoverunsold.api.requests

import java.util.Date

data class OfferUpdateRequest(
    val startDate: String,
    val duration: ULong,
    val beneficiaries: Int? = null,
    val price: Double,
    val locationId: String,
)