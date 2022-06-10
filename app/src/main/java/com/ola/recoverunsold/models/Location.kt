package com.ola.recoverunsold.models

import java.util.Date

data class Location(
    val id: String,
    val name: String,
    val coordinates: LatLong,
    val indication: String? = null,
    val image: String? = null,
    val createdAt: Date
)