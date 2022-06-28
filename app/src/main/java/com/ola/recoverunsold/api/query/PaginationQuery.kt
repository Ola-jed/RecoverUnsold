package com.ola.recoverunsold.api.query

open class PaginationQuery(open var page: Int = 1, open var perPage: Int = 10) : ApiClientQueryInterface {
    override fun toQueryMap(): Map<String, Any> {
        return mapOf("page" to page.toString(), "perPage" to perPage.toString())
    }

    fun nextPage(): PaginationQuery {
        return PaginationQuery(page + 1, perPage)
    }

    fun previousPage(): PaginationQuery {
        return PaginationQuery(page - 1, perPage)
    }

    operator fun inc(): PaginationQuery = nextPage()
    operator fun dec(): PaginationQuery = previousPage()
}