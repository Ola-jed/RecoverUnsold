package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.api.requests.FcmTokenCreateRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface FcmTokenService : BaseApiService {
    @POST(ApiUrls.fcmTokenUrl)
    suspend fun createFcmToken(
        @Header("Authorization") authorization: String,
        @Body fcmTokenCreateRequest: FcmTokenCreateRequest
    ): NoContentResponse

    @DELETE(ApiUrls.fcmTokenUrl + "/{value}")
    suspend fun deleteFcmToken(
        @Header("Authorization") authorization: String,
        @Path("value") value: String
    ): NoContentResponse

    @DELETE(ApiUrls.fcmTokenUrl)
    suspend fun deleteAllFcmTokens(
        @Header("Authorization") authorization: String
    ): NoContentResponse
}