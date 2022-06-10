package com.ola.recoverunsold.models

data class Page<T>(
    val items: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val hasNext: Boolean,
    val total: Int,
    val baseUrl: String? = null,
    val previousUrl: String? = null,
    val nextUrl: String? = null
)