package com.ola.recoverunsold.api.services

import com.ola.recoverunsold.api.core.ApiConstants
import com.ola.recoverunsold.api.requests.ProductUpdateRequest
import com.ola.recoverunsold.models.Page
import com.ola.recoverunsold.models.Product
import okhttp3.MultipartBody
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

interface ProductService : BaseApiService {
    @GET(ApiConstants.productsUrl + "/{id}")
    suspend fun getProduct(
        @Path("id") id: String
    ): Response<Product>

    @GET(ApiConstants.distributorProductsUrl + "/{id}")
    suspend fun getDistributorProducts(
        @Path("id") id: String,
        @QueryMap pagination: Map<String, Any>
    ): Response<Page<Product>>

    @GET(ApiConstants.offersUrl + "/{id}/Products")
    suspend fun getOfferProducts(
        @Path("id") id: String,
        @QueryMap pagination: Map<String, Any>
    ): Response<Page<Product>>

    @POST(ApiConstants.offersUrl + "/{id}/Products")
    @Multipart
    suspend fun createProduct(
        @Path("id") id: String,
        @Header("Authorization") authorization: String,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part images: List<MultipartBody.Part>? = null
    ): Response<Product>

    @PUT(ApiConstants.productsUrl + "/{id}")
    suspend fun updateProduct(
        @Path("id") id: String,
        @Header("Authorization") authorization: String,
        @Body productUpdateRequest: ProductUpdateRequest
    ): NoContentResponse

    @DELETE(ApiConstants.productsUrl + "/{id}")
    suspend fun deleteProduct(
        @Path("id") id: String,
        @Header("Authorization") authorization: String
    ): NoContentResponse
}