package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.api.requests.ReportCreateRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ReportsService : BaseApiService {
    @POST("${ApiUrls.distributorsUrl}/{id}/Reports")
    suspend fun reportDistributor(
        @Path("id") id: String,
        @Body reportCreateRequest: ReportCreateRequest
    ): NoContentResponse
}