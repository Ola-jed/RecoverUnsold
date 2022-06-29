package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiConstants
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.models.Page
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface LocationService : BaseApiService {
    @GET(ApiConstants.distributorsLocationsUrl + "/{distributorId}")
    suspend fun getDistributorLocations(
        @Path("distributorId") distributorId: String,
        @QueryMap pagination: Map<String, String>
    ): Response<Page<Location>>

    @GET(ApiConstants.locationsSearchUrl + "/{query}")
    suspend fun searchLocations(
        @Path("query") query: String,
        @QueryMap pagination: Map<String, String>
    ): Response<Page<Location>>

    @GET(ApiConstants.locationsUrl + "/{id}")
    suspend fun getLocation(@Path("id") id: String): Response<Location>

    @GET(ApiConstants.locationsUrl)
    suspend fun getLocations(
        @Header("Authorization") authorization: String,
        @QueryMap pagination: Map<String, String>
    ): Response<Page<Location>>

    @POST(ApiConstants.locationsUrl)
    @Multipart
    suspend fun createLocation(
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part? = null,
        @Part("indication") indication: RequestBody? = null,
        @Part("name") name: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody
    ): Response<Location>

    @PUT(ApiConstants.locationsUrl + "/{id}")
    @Multipart
    suspend fun updateLocation(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Part image: MultipartBody.Part? = null,
        @Part("indication") indication: RequestBody? = null,
        @Part("name") name: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody
    ): NoContentResponse

    @DELETE(ApiConstants.locationsUrl + "/{id}")
    suspend fun deleteLocation(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): NoContentResponse
}