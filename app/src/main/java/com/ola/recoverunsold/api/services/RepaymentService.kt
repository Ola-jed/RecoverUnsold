package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.models.Page
import com.ola.recoverunsold.models.Repayment
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface RepaymentService : BaseApiService {
    @GET(ApiUrls.repaymentsUrl)
    suspend fun getRepayments(
        @QueryMap filters: Map<String, String>
    ): Response<Page<Repayment>>

    @GET("${ApiUrls.repaymentsUrl}/{id}")
    suspend fun getRepayment(
        @Path("id") id: String,
    ): Response<Repayment>
}