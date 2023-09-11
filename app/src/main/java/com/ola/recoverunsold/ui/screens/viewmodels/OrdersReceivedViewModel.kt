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
import com.ola.recoverunsold.utils.extensions.toApiCallResult
import com.ola.recoverunsold.utils.resources.Strings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersReceivedViewModel @Inject constructor(
    private val orderServiceWrapper: OrderServiceWrapper
) : ViewModel() {
    var ordersGetResponse: ApiCallResult<Page<Order>> by mutableStateOf(ApiCallResult.Inactive)
    var orderQuery by mutableStateOf(OrderFilterQuery())

    init {
        getOrders()
    }

    fun getOrders() {
        ordersGetResponse = ApiCallResult.Loading
        viewModelScope.launch {
            ordersGetResponse = orderServiceWrapper
                .getDistributorOrders(orderQuery)
                .toApiCallResult()
        }
    }

    fun loadMore() {
        orderQuery = orderQuery.inc()
        val savedPage = ordersGetResponse.data!!
        ordersGetResponse = ApiCallResult.Loading
        viewModelScope.launch {
            val extraApiCallResponse = orderServiceWrapper
                .getDistributorOrders(orderQuery)
                .toApiCallResult()

            if (extraApiCallResponse is ApiCallResult.Error) {
                ordersGetResponse = extraApiCallResponse
            } else if (extraApiCallResponse is ApiCallResult.Success) {
                ordersGetResponse = extraApiCallResponse.copy(
                    _data = extraApiCallResponse.data!!.prepend(savedPage)
                )
            }
        }
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