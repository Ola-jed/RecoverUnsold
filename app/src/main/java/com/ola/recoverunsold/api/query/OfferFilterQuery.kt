package com.ola.recoverunsold.api.query

import com.ola.recoverunsold.utils.misc.format
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

    override fun toQueryMap(): Map<String, String> {
        val resultMap = mutableMapOf("page" to page.toString(), "perPage" to perPage.toString())
        if (minPrice != null) {
            resultMap["minPrice"] = minPrice!!.toString()
        }
        if (maxPrice != null) {
            resultMap["maxPrice"] = maxPrice!!.toString()
        }
        if (minDate != null) {
            resultMap["minDate"] = minDate!!.format()
        }
        if (maxDate != null) {
            resultMap["maxDate"] = maxDate!!.format()
        }
        if (active != null) {
            resultMap["active"] = active!!.toString()
        }
        return resultMap
    }

    override fun nextPage(): OfferFilterQuery {
        return OfferFilterQuery(
            page + 1, perPage, minPrice, maxPrice, minDate, maxDate, active
        )
    }

    override fun previousPage(): OfferFilterQuery {
        return OfferFilterQuery(
            page - 1, perPage, minPrice, maxPrice, minDate, maxDate, active
        )
    }

    override operator fun inc(): OfferFilterQuery = nextPage()
    override operator fun dec(): OfferFilterQuery = previousPage()
}
