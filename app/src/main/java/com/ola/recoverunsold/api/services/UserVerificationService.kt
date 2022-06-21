package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiConstants
import com.ola.recoverunsold.api.requests.UserVerificationConfirmRequest
import com.ola.recoverunsold.api.requests.UserVerificationStartRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface UserVerificationService : BaseApiService {
    @POST(ApiConstants.userVerificationStartUrl)
    suspend fun startUserVerification(
        @Body userVerificationStartRequest: UserVerificationStartRequest
    ): NoContentResponse

    @POST(ApiConstants.userVerificationConfirmUrl)
    suspend fun confirmUserVerification(
        @Body userVerificationConfirmRequest: UserVerificationConfirmRequest
    ): NoContentResponse
}