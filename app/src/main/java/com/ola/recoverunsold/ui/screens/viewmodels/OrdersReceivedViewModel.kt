package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.StatusCode
import com.ola.recoverunsold.api.query.OrderFilterQuery
import com.ola.recoverunsold.api.services.wrappers.OrderServiceWrapper
import com.ola.recoverunsold.models.Order
import com.ola.recoverunsold.models.Page
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class OrdersReceivedViewModel(
    private val orderServiceWrapper: OrderServiceWrapper = KoinJavaComponent.get(
        OrderServiceWrapper::class.java
    )
) : ViewModel() {
    private val token = TokenStore.get()!!.bearerToken
    var ordersGetResponse: ApiCallResult<Page<Order>> by mutableStateOf(ApiCallResult.Inactive)
    var orderQuery by mutableStateOf(OrderFilterQuery())

    fun getOrders() {
        ordersGetResponse = ApiCallResult.Loading
        viewModelScope.launch {
            val response = orderServiceWrapper.getCustomerOrders(token, orderQuery)
            ordersGetResponse = if (response.isSuccessful) {
                ApiCallResult.Success(_data = response.body())
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun getNext() {
        orderQuery = orderQuery.inc()
        getOrders()
    }

    fun getPrevious() {
        orderQuery = orderQuery.dec()
        getOrders()
    }

    fun resetFilters() {
        orderQuery = OrderFilterQuery()
    }

    fun acceptOrder(order: Order, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            val response = orderServiceWrapper.acceptOrder(token, order.id)
            if(response.isSuccessful) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun rejectOrder(order: Order, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            val response = orderServiceWrapper.rejectOrder(token, order.id)
            if(response.isSuccessful) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun errorMessage(): String? {
        return when (ordersGetResponse.statusCode) {
            StatusCode.Unauthorized.code -> Strings.get(R.string.not_authenticated_full_message)
            in 400..600 -> Strings.get(R.string.unknown_error_occured)
            else -> null
        }
    }
}