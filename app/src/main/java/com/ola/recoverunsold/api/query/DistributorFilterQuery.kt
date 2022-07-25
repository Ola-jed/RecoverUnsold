package com.ola.recoverunsold.api.query

data class DistributorFilterQuery(
    override var page: Int = 1,
    override var perPage: Int = 10,
    var name: String? = null
) : PaginationQuery(page, perPage), ApiClientQueryInterface {
    override fun toQueryMap(): Map<String, String> {
        val resultMap = mutableMapOf("page" to page.toString(), "perPage" to perPage.toString())
        if (name != null) {
            resultMap["name"] = name!!.toString()
        }
        return resultMap
    }

    override fun nextPage(): DistributorFilterQuery = copy(page = page + 1)
    override fun previousPage(): DistributorFilterQuery = copy(page = page - 1)
    override operator fun inc(): DistributorFilterQuery = nextPage()
    override operator fun dec(): DistributorFilterQuery = previousPage()
}
