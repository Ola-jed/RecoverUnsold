package com.ola.recoverunsold.api.services.wrappers

import com.ola.recoverunsold.api.query.OrderFilterQuery
import com.ola.recoverunsold.api.requests.OrderCreateRequest
import com.ola.recoverunsold.api.services.OrderService
import com.ola.recoverunsold.models.Order
import com.ola.recoverunsold.models.Page
import retrofit2.Response

class OrderServiceWrapper(private val orderService: OrderService) {
    suspend fun getOrder(id: String): Response<Order> {
        return orderService.getOrder(id)
    }

    suspend fun getCustomerOrders(
        authorization: String,
        orderFilterQuery: OrderFilterQuery
    ): Response<Page<Order>> {
        return orderService.getCustomerOrders(authorization, orderFilterQuery.toQueryMap())
    }

    suspend fun getDistributorOrders(
        authorization: String,
        orderFilterQuery: OrderFilterQuery
    ): Response<Page<Order>> {
        return orderService.getDistributorOrders(authorization, orderFilterQuery.toQueryMap())
    }

    suspend fun getOfferOrders(
        authorization: String,
        id: String,
        orderFilterQuery: OrderFilterQuery
    ): Response<Page<Order>> {
        return orderService.getOfferOrders(authorization, id, orderFilterQuery.toQueryMap())
    }

    suspend fun createOrder(
        authorization: String,
        offerId: String,
        orderCreateRequest: OrderCreateRequest
    ): Response<Order> {
        return orderService.createOrder(authorization, offerId, orderCreateRequest)
    }
}