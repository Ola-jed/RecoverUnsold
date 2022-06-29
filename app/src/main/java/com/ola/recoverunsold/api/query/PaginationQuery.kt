package com.ola.recoverunsold.api.query

open class PaginationQuery(open var page: Int = 1, open var perPage: Int = 10) :
    ApiClientQueryInterface {
    override fun toQueryMap(): Map<String, String> {
        return mapOf("page" to page.toString(), "perPage" to perPage.toString())
    }

    open fun nextPage(): PaginationQuery {
        return PaginationQuery(page + 1, perPage)
    }

    open fun previousPage(): PaginationQuery {
        return PaginationQuery(page - 1, perPage)
    }

    open operator fun inc(): PaginationQuery = nextPage()
    open operator fun dec(): PaginationQuery = previousPage()
}