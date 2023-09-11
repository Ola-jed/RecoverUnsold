package com.ola.recoverunsold.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Page<T>(
    @Json(name = "items") val items: List<T>,
    @Json(name = "pageNumber") val pageNumber: Int,
    @Json(name = "pageSize") val pageSize: Int,
    @Json(name = "hasNext") val hasNext: Boolean,
    @Json(name = "total") val total: Int
) {
    // Will add all the items of the given page at the end of the list
    fun append(page: Page<T>): Page<T> {
        return Page(
            items = items + page.items,
            pageNumber = pageNumber,
            pageSize = pageSize,
            hasNext = hasNext,
            total = total
        )
    }

    // Will add all the items of the given page at the start of the list
    fun prepend(page: Page<T>): Page<T> {
        return Page(
            items = page.items + items,
            pageNumber = pageNumber,
            pageSize = pageSize,
            hasNext = hasNext,
            total = total
        )
    }
}