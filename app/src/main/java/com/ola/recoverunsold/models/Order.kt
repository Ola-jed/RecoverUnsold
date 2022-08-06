package com.ola.recoverunsold.models

import java.util.Date

data class Order(
    val id: String,
    val status: OrderStatus,
    val customerId: String,
    val customer: Customer? = null,
    val offerId: String,
    val offer: Offer?,
    val withdrawalDate: Date,
    val createdAt: Date,
    val opinions: List<Opinion>
)