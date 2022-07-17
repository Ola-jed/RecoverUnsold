package com.ola.recoverunsold.models

data class Order(
    val id: String,
    val status: OrderStatus,
    val customerId: String,
    val customer: Customer?,
    val offerId: String,
    val offer: Offer?
)

