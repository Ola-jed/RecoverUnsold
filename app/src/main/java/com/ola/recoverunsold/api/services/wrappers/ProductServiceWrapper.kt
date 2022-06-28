package com.ola.recoverunsold.api.services.wrappers

import com.ola.recoverunsold.api.query.PaginationQuery
import com.ola.recoverunsold.api.requests.ProductCreateRequest
import com.ola.recoverunsold.api.requests.ProductUpdateRequest
import com.ola.recoverunsold.api.services.ProductService
import com.ola.recoverunsold.models.Page
import com.ola.recoverunsold.models.Product
import com.ola.recoverunsold.utils.misc.toMultipartRequestBody
import retrofit2.Response

class ProductServiceWrapper(private val productService: ProductService) {
    suspend fun getProduct(id: String): Response<Product> {
        return productService.getProduct(id)
    }

    suspend fun getDistributorProducts(
        id: String,
        paginationQuery: PaginationQuery
    ): Response<Page<Product>> {
        return productService.getDistributorProducts(id, paginationQuery.toQueryMap())
    }

    suspend fun getOfferProducts(
        id: String,
        paginationQuery: PaginationQuery
    ): Response<Page<Product>> {
        return productService.getOfferProducts(id, paginationQuery.toQueryMap())
    }

    suspend fun createProduct(
        id: String,
        token: String,
        productCreateRequest: ProductCreateRequest
    ): Response<Product> {
        return productService.createProduct(
            id,
            token,
            productCreateRequest.name.toMultipartRequestBody(),
            productCreateRequest.description.toMultipartRequestBody(),
            productCreateRequest.images
        )
    }

    suspend fun updateProduct(
        id: String,
        token: String,
        productUpdateRequest: ProductUpdateRequest
    ): Response<Void> {
        return productService.updateProduct(
            id,
            token,
            productUpdateRequest
        )
    }

    suspend fun deleteProduct(
        id: String,
        token: String
    ): Response<Void> {
        return productService.deleteProduct(id, token)
    }
}