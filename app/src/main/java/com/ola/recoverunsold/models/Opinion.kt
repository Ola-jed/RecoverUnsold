package com.ola.recoverunsold.models

import java.util.Date

data class Opinion(
    val id: String,
    val comment: String,
    val orderId: String,
    val createdAt: Date
)
