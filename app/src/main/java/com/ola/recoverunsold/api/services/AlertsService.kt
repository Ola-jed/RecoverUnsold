package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.api.requests.AlertCreateRequest
import com.ola.recoverunsold.models.Alert
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AlertsService : BaseApiService {
    @GET(ApiUrls.alertsUrl)
    suspend fun getAlerts(
        @Header("Authorization") authorization: String
    ): Response<List<Alert>>

    @POST(ApiUrls.alertsUrl)
    suspend fun createAlert(
        @Header("Authorization") authorization: String,
        @Body alertCreateRequest: AlertCreateRequest
    ): NoContentResponse

    @DELETE(ApiUrls.alertsUrl + "/{id}")
    suspend fun deleteAlert(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): NoContentResponse
}