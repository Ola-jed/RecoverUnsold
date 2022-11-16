package com.ola.recoverunsold.models

import java.util.Date

data class Offer(
    val id: String,
    val startDate: Date,
    val duration: ULong,
    val onlinePayment: Boolean,
    val beneficiaries: Int? = null,
    val price: Double,
    val createdAt: Date,
    val distributorId: String,
    val location: Location? = null,
    val products: List<Product>? = null
)