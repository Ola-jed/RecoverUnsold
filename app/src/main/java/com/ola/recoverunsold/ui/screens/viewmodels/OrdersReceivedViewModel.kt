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
import com.ola.recoverunsold.utils.misc.toApiCallResult
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersReceivedViewModel @Inject constructor(
    private val orderServiceWrapper: OrderServiceWrapper
) : ViewModel() {
    private val token = TokenStore.get()!!.bearerToken
    var ordersGetResponse: ApiCallResult<Page<Order>> by mutableStateOf(ApiCallResult.Inactive)
    var orderQuery by mutableStateOf(OrderFilterQuery())

    init {
        getOrders()
    }

    fun getOrders() {
        ordersGetResponse = ApiCallResult.Loading
        viewModelScope.launch {
            ordersGetResponse = orderServiceWrapper
                .getDistributorOrders(token, orderQuery)
                .toApiCallResult()
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

    fun errorMessage(): String? {
        return when (ordersGetResponse.statusCode) {
            StatusCode.Unauthorized.code -> Strings.get(R.string.not_authenticated_full_message)
            in 400..600 -> Strings.get(R.string.unknown_error_occured)
            else -> null
        }
    }
}