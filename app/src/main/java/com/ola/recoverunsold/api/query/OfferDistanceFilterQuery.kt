package com.ola.recoverunsold.api.query

import com.ola.recoverunsold.models.LatLong

data class OfferDistanceFilterQuery(
    override var page: Int = 1,
    override var perPage: Int = 10,
    var latLong: LatLong,
    var distance: Double
) : PaginationQuery(page, perPage), ApiClientQueryInterface {
    override fun toQueryMap(): Map<String, String> {
        return mapOf(
            "page" to page.toString(),
            "perPage" to perPage.toString(),
            "latitude" to latLong.latitude.toString(),
            "longitude" to latLong.longitude.toString(),
            "distance" to distance.toString()
        )
    }

    override fun nextPage(): OfferDistanceFilterQuery = copy(page = page + 1)
    override fun previousPage(): OfferDistanceFilterQuery = copy(page = page - 1)
    override operator fun inc(): OfferDistanceFilterQuery = nextPage()
    override operator fun dec(): OfferDistanceFilterQuery = previousPage()
}