package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.api.requests.ForgotPasswordStartRequest
import com.ola.recoverunsold.api.requests.PasswordResetRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ForgotPasswordService : BaseApiService {
    @POST(ApiUrls.forgotPasswordUrl)
    suspend fun startForgotPassword(
        @Body forgotPasswordStartRequest: ForgotPasswordStartRequest
    ): NoContentResponse

    @POST(ApiUrls.passwordResetUrl)
    suspend fun resetPassword(
        @Body passwordResetRequest: PasswordResetRequest
    ): NoContentResponse
}