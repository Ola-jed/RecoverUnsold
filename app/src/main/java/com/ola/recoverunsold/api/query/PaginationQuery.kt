package com.ola.recoverunsold.api.query

data class PaginationQuery(val page: Int = 1, val perPage: Int = 10) : ApiClientQueryInterface {
    override fun toQueryMap(): Map<String, String> {
        return mapOf("page" to page.toString(), "perPage" to perPage.toString())
    }
}