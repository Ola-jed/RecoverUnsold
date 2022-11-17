package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.api.requests.FcmTokenCreateRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface FcmTokenService : BaseApiService {
    @POST(ApiUrls.fcmTokenUrl)
    suspend fun createFcmToken(
        @Body fcmTokenCreateRequest: FcmTokenCreateRequest
    ): NoContentResponse

    @DELETE(ApiUrls.fcmTokenUrl + "/{value}")
    suspend fun deleteFcmToken(
        @Path("value") value: String
    ): NoContentResponse

    @DELETE(ApiUrls.fcmTokenUrl)
    suspend fun deleteAllFcmTokens(): NoContentResponse
}