package com.ola.recoverunsold.api.requests

import okhttp3.MultipartBody

data class LocationCreateOrUpdateRequest(
    val image: MultipartBody.Part? = null,
    val indication: String? = null,
    val name: String,
    val latitude: Double,
    val longitude: Double
)