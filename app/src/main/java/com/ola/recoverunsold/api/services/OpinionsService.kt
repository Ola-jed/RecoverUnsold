package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.api.requests.OpinionCreateRequest
import com.ola.recoverunsold.api.requests.OpinionUpdateRequest
import com.ola.recoverunsold.models.Opinion
import retrofit2.Response
import retrofit2.http.*

interface OpinionsService : BaseApiService {
    @POST(ApiUrls.ordersUrl + "/{id}/Opinions")
    suspend fun createOpinion(
        @Path("id") id: String,
        @Body opinionCreateRequest: OpinionCreateRequest
    ): Response<Opinion>

    @POST(ApiUrls.opinionsUrl + "/{id}")
    suspend fun getOpinion(
        @Path("id") id: String,
    ): Response<Opinion>

    @PUT(ApiUrls.opinionsUrl + "/{id}")
    suspend fun updateOpinion(
        @Path("id") id: String,
        @Body opinionUpdateRequest: OpinionUpdateRequest
    ): NoContentResponse

    @DELETE(ApiUrls.opinionsUrl + "/{id}")
    suspend fun deleteOpinion(
        @Path("id") id: String,
    ): NoContentResponse
}