package com.ola.recoverunsold.api.query

import java.text.SimpleDateFormat
import java.util.Date

data class PeriodQuery(
    val periodStart: Date,
    val periodEnd: Date
) : ApiClientQueryInterface {
    companion object {
        private val formatter by lazy { SimpleDateFormat("yyyy-MM-dd") }
    }

    override fun toQueryMap(): Map<String, String> {
        return mapOf(
            "periodStart" to formatter.format(periodStart),
            "periodEnd" to formatter.format(periodEnd)
        )
    }
}
