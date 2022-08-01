package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.api.requests.ReviewMessageRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ReviewsService : BaseApiService {
    @POST(ApiUrls.reviewsUrl)
    suspend fun publishReview(
        @Header("Authorization") authorization: String,
        @Body reviewMessageRequest: ReviewMessageRequest
    ): NoContentResponse
}