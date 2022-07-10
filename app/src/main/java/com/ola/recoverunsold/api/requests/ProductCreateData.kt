package com.ola.recoverunsold.api.requests

import android.net.Uri

data class ProductCreateData(
    val name: String,
    val description: String,
    val images: List<Uri>? = null
)