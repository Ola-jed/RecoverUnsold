package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.api.requests.UserVerificationConfirmRequest
import com.ola.recoverunsold.api.requests.UserVerificationStartRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface UserVerificationService : BaseApiService {
    @POST(ApiUrls.userVerificationStartUrl)
    suspend fun startUserVerification(
        @Body userVerificationStartRequest: UserVerificationStartRequest
    ): NoContentResponse

    @POST(ApiUrls.userVerificationConfirmUrl)
    suspend fun confirmUserVerification(
        @Body userVerificationConfirmRequest: UserVerificationConfirmRequest
    ): NoContentResponse
}