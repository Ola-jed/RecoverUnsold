package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.api.requests.FcmTokenCreateRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface FcmTokenService : BaseApiService {
    @POST(ApiUrls.fcmTokenUrl)
    suspend fun createFcmToken(
        @Body fcmTokenCreateRequest: FcmTokenCreateRequest
    ): NoContentResponse

    @DELETE(ApiUrls.fcmTokenUrl)
    suspend fun deleteAllFcmTokens(): NoContentResponse
}