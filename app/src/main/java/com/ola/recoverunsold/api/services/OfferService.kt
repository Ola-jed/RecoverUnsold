package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.api.requests.OfferUpdateRequest
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.models.OfferWithRelativeDistance
import com.ola.recoverunsold.models.Page
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface OfferService : BaseApiService {
    @GET(ApiUrls.offersUrl)
    suspend fun getOffers(
        @QueryMap filters: Map<String, String>
    ): Response<Page<Offer>>

    @GET(ApiUrls.distributorOffersUrl + "/{distributorId}")
    suspend fun getDistributorOffers(
        @Path("distributorId") distributorId: String,
        @QueryMap filters: Map<String, String>
    ): Response<Page<Offer>>

    @GET(ApiUrls.offersUrl + "/{id}")
    suspend fun getOffer(
        @Path("id") id: String
    ): Response<Offer>

    @GET(ApiUrls.closeOffersUrl)
    suspend fun getCloseOffers(
        @QueryMap filters: Map<String, String>
    ): Response<Page<OfferWithRelativeDistance>>

    @POST(ApiUrls.offersUrl)
    @Multipart
    suspend fun createOffer(
        @Part("startDate") startDate: RequestBody,
        @Part("duration") duration: RequestBody,
        @Part("beneficiaries") beneficiaries: RequestBody? = null,
        @Part("price") price: RequestBody,
        @Part("locationId") locationId: RequestBody
    ): Response<Offer>

    @PUT(ApiUrls.offersUrl + "/{id}")
    suspend fun updateOffer(
        @Path("id") id: String,
        @Body offerUpdateRequest: OfferUpdateRequest
    ): NoContentResponse

    @DELETE(ApiUrls.offersUrl + "/{id}")
    suspend fun deleteOffer(
        @Path("id") id: String
    ): NoContentResponse
}