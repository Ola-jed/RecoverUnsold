package com.ola.recoverunsold.models

import java.util.Date

data class Offer(
    val id: String,
    val startDate: Date,
    val duration: ULong,
    val beneficiaries: Int?,
    val price: Double,
    val createdAt: Date,
    val location: Location?,
    val products: List<Product>?
)
