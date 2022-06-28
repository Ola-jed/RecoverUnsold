package com.ola.recoverunsold.api.requests

import okhttp3.MultipartBody

data class ProductCreateRequest(
    val name: String,
    val description: String,
    val images: List<MultipartBody.Part>? = null
)