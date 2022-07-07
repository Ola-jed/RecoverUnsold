package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiConstants
import com.ola.recoverunsold.api.requests.OfferUpdateRequest
import com.ola.recoverunsold.api.requests.ProductCreateRequest
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.models.Page
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface OfferService : BaseApiService {
    @GET(ApiConstants.offersUrl)
    suspend fun getOffers(
        @QueryMap filters: Map<String, String>
    ): Response<Page<Offer>>

    @GET(ApiConstants.distributorOffersUrl + "/{distributorId}")
    suspend fun getDistributorOffers(
        @Path("distributorId") distributorId: String,
        @QueryMap filters: Map<String, String>
    ): Response<Page<Offer>>

    @GET(ApiConstants.offersUrl + "/{id}")
    suspend fun getOffer(
        @Path("id") id: String
    ): Response<Offer>

    @POST(ApiConstants.offersUrl)
    @Multipart
    suspend fun createOffer(
        @Header("Authorization") authorization: String,
        @Part("startDate") startDate: RequestBody,
        @Part("duration") duration: RequestBody,
        @Part("beneficiaries") beneficiaries: RequestBody? = null,
        @Part("price") price: RequestBody,
        @Part("locationId") locationId: RequestBody,
        @Part("products") products: List<ProductCreateRequest>? = null
    ): Response<Offer>

    @PUT(ApiConstants.offersUrl + "/{id}")
    suspend fun updateOffer(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Body offerUpdateRequest: OfferUpdateRequest
    ): NoContentResponse

    @DELETE(ApiConstants.offersUrl + "/{id}")
    suspend fun deleteOffer(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): NoContentResponse
}