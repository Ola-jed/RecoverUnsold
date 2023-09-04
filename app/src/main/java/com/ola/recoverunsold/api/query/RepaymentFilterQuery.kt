package com.ola.recoverunsold.api.query

data class RepaymentFilterQuery(
    override var page: Int = 1,
    override var perPage: Int = 10,
    var done: Boolean? = null
) : PaginationQuery(page, perPage), ApiClientQueryInterface {
    override fun toQueryMap(): Map<String, String> {
        val resultMap = mutableMapOf("page" to page.toString(), "perPage" to perPage.toString())
        if (done != null) {
            resultMap["done"] = done!!.toString()
        }
        return resultMap
    }

    override fun nextPage(): RepaymentFilterQuery = copy(page = page + 1)
    override fun previousPage(): RepaymentFilterQuery = copy(page = page - 1)
    override operator fun inc(): RepaymentFilterQuery = nextPage()
    override operator fun dec(): RepaymentFilterQuery = previousPage()
}
