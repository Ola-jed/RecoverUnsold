package com.ola.recoverunsold.models

import java.util.Date

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val offerId: String,
    val images: List<ProductImage>,
    val createdAt: Date
)
