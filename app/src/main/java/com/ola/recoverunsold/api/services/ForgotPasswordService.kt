package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiConstants
import com.ola.recoverunsold.api.requests.ForgotPasswordStartRequest
import com.ola.recoverunsold.api.requests.PasswordResetRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ForgotPasswordService : BaseApiService {
    @POST(ApiConstants.forgotPasswordUrl)
    suspend fun startForgotPassword(
        @Body forgotPasswordStartRequest: ForgotPasswordStartRequest
    ): NoContentResponse

    @POST(ApiConstants.passwordResetUrl)
    suspend fun resetPassword(
        @Body passwordResetRequest: PasswordResetRequest
    ): NoContentResponse
}