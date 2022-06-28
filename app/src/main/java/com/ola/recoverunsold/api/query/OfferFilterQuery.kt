package com.ola.recoverunsold.api.query

import java.util.Date

data class OfferFilterQuery(
    override var page: Int = 1,
    override var perPage: Int = 10,
    var minPrice: Double? = null,
    var maxPrice: Double? = null,
    var minDate: Date? = null,
    var maxDate: Date? = null,
    var active: Boolean? = null
) : PaginationQuery(page, perPage), ApiClientQueryInterface {
    override fun toQueryMap(): Map<String, Any> {
        val resultMap =
            mutableMapOf<String, Any>("page" to page.toString(), "perPage" to perPage.toString())
        if (minPrice != null) {
            resultMap["minPrice"] = minPrice!!
        }
        if (maxPrice != null) {
            resultMap["maxPrice"] = maxPrice!!
        }
        if (minDate != null) {
            resultMap["minDate"] = minDate!!
        }
        if (maxDate != null) {
            resultMap["maxDate"] = maxDate!!
        }
        if (active != null) {
            resultMap["active"] = active!!
        }
        return resultMap
    }
}
