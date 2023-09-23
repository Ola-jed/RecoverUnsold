package com.ola.recoverunsold.api.services.wrappers

import com.ola.recoverunsold.api.query.OrderFilterQuery
import com.ola.recoverunsold.api.requests.OrderCreateRequest
import com.ola.recoverunsold.api.services.NoContentResponse
import com.ola.recoverunsold.api.services.OrderService
import com.ola.recoverunsold.models.Order
import com.ola.recoverunsold.models.Page
import retrofit2.Response

class OrderServiceWrapper(private val orderService: OrderService) {
    suspend fun getOrder(id: String): Response<Order> {
        return orderService.getOrder(id)
    }

    suspend fun getCustomerOrders(
        orderFilterQuery: OrderFilterQuery
    ): Response<Page<Order>> = orderService.getCustomerOrders(orderFilterQuery.toQueryMap())

    suspend fun getDistributorOrders(
        orderFilterQuery: OrderFilterQuery
    ): Response<Page<Order>> {
        return orderService.getDistributorOrders(orderFilterQuery.toQueryMap())
    }

    suspend fun getOfferOrders(
        id: String,
        orderFilterQuery: OrderFilterQuery
    ): Response<Page<Order>> {
        return orderService.getOfferOrders(id, orderFilterQuery.toQueryMap())
    }

    suspend fun createOrder(
        orderId: String,
        orderCreateRequest: OrderCreateRequest
    ): Response<Order> {
        return orderService.createOrder(orderId, orderCreateRequest)
    }

    suspend fun acceptOrder(orderId: String): NoContentResponse {
        return orderService.acceptOrder(orderId)
    }

    suspend fun rejectOrder(orderId: String): NoContentResponse {
        return orderService.rejectOrder(orderId)
    }

    suspend fun completeOrder(orderId: String): NoContentResponse {
        return orderService.completeOrder(orderId)
    }

    suspend fun sendOrderInvoice(orderId: String): NoContentResponse {
        return orderService.sendOrderInvoice(orderId)
    }
}