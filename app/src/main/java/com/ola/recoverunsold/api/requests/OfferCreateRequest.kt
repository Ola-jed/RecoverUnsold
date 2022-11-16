package com.ola.recoverunsold.api.requests

import java.util.Date

data class OfferCreateRequest(
    val startDate: Date,
    val duration: ULong,
    val onlinePayment: Boolean,
    val beneficiaries: Int? = null,
    val price: Double,
    val locationId: String,
    val products: List<ProductCreateRequest>? = null
)