package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiUrls
import com.ola.recoverunsold.api.requests.ProductUpdateRequest
import com.ola.recoverunsold.models.Page
import com.ola.recoverunsold.models.Product
import okhttp3.MultipartBody
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

interface ProductService : BaseApiService {
    @GET(ApiUrls.productsUrl + "/{id}")
    suspend fun getProduct(
        @Path("id") id: String
    ): Response<Product>

    @GET(ApiUrls.distributorProductsUrl + "/{id}")
    suspend fun getDistributorProducts(
        @Path("id") id: String,
        @QueryMap pagination: Map<String, String>
    ): Response<Page<Product>>

    @GET(ApiUrls.offersUrl + "/{id}/Products")
    suspend fun getOfferProducts(
        @Path("id") id: String,
        @QueryMap pagination: Map<String, String>
    ): Response<Page<Product>>

    @POST(ApiUrls.offersUrl + "/{id}/Products")
    @Multipart
    suspend fun createProduct(
        @Path("id") id: String,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part images: List<MultipartBody.Part>? = null
    ): Response<Product>

    @PUT(ApiUrls.productsUrl + "/{id}")
    suspend fun updateProduct(
        @Path("id") id: String,
        @Body productUpdateRequest: ProductUpdateRequest
    ): NoContentResponse

    @DELETE(ApiUrls.productsUrl + "/{id}")
    suspend fun deleteProduct(
        @Path("id") id: String,
    ): NoContentResponse
}