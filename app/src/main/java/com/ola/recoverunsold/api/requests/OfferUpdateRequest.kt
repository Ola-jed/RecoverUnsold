package com.ola.recoverunsold.api.requests

data class OfferUpdateRequest(
    val startDate: String,
    val duration: ULong,
    val onlinePayment: Boolean,
    val beneficiaries: Int? = null,
    val price: Double,
    val locationId: String,
)