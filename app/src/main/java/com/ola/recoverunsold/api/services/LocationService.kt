package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.models.Page
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface LocationService : BaseApiService {
    @GET("${ApiUrls.distributorsLocationsUrl}/{distributorId}")
    suspend fun getDistributorLocations(
        @Path("distributorId") distributorId: String,
        @QueryMap pagination: Map<String, String>
    ): Response<Page<Location>>

    @GET("${ApiUrls.locationsSearchUrl}/{query}")
    suspend fun searchLocations(
        @Path("query") query: String,
        @QueryMap pagination: Map<String, String>
    ): Response<Page<Location>>

    @GET("${ApiUrls.locationsUrl}/{id}")
    suspend fun getLocation(@Path("id") id: String): Response<Location>

    @GET(ApiUrls.locationsUrl)
    suspend fun getLocations(
        @QueryMap pagination: Map<String, String>
    ): Response<Page<Location>>

    @POST(ApiUrls.locationsUrl)
    @Multipart
    suspend fun createLocation(
        @Part image: MultipartBody.Part? = null,
        @Part("indication") indication: RequestBody? = null,
        @Part("name") name: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody
    ): Response<Location>

    @PUT("${ApiUrls.locationsUrl}/{id}")
    @Multipart
    suspend fun updateLocation(
        @Path("id") id: String,
        @Part image: MultipartBody.Part? = null,
        @Part("indication") indication: RequestBody? = null,
        @Part("name") name: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody
    ): NoContentResponse

    @DELETE("${ApiUrls.locationsUrl}/{id}")
    suspend fun deleteLocation(
        @Path("id") id: String
    ): NoContentResponse
}