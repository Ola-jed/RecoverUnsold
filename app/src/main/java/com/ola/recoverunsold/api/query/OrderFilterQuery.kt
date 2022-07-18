package com.ola.recoverunsold.api.query

data class OrderFilterQuery(
    override var page: Int = 1,
    override var perPage: Int = 10,
    var status: String? = null
) : PaginationQuery(page, perPage), ApiClientQueryInterface {
    override fun toQueryMap(): Map<String, String> {
        val resultMap = mutableMapOf("page" to page.toString(), "perPage" to perPage.toString())
        if (status != null) {
            resultMap["status"] = status!!.toString().lowercase()
        }
        return resultMap
    }

    override fun nextPage(): OrderFilterQuery = copy(page = page + 1)
    override fun previousPage(): OrderFilterQuery = copy(page = page - 1)
    override operator fun inc(): OrderFilterQuery = nextPage()
    override operator fun dec(): OrderFilterQuery = previousPage()
}
