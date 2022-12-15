package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.requests.OpinionCreateRequest
import com.ola.recoverunsold.api.requests.TransactionRequest
import com.ola.recoverunsold.api.services.OpinionsService
import com.ola.recoverunsold.api.services.PaymentsService
import com.ola.recoverunsold.api.services.wrappers.OrderServiceWrapper
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.models.Opinion
import com.ola.recoverunsold.models.Order
import com.ola.recoverunsold.models.OrderStatus
import com.ola.recoverunsold.utils.misc.toApiCallResult
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.UserObserver
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderDetailsViewModel @AssistedInject constructor(
    private val ordersServiceWrapper: OrderServiceWrapper,
    private val opinionsService: OpinionsService,
    private val paymentsService: PaymentsService,
    @Assisted val orderId: String
) : ViewModel() {
    var orderApiCallResult: ApiCallResult<Order> by mutableStateOf(ApiCallResult.Inactive)
    var opinionComment by mutableStateOf("")
    var opinionCommentApiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive)
    val isCustomer = UserObserver.user.value is Customer
    val customer = UserObserver.user.value as? Customer
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    init {
        getOrder()
    }

    fun getOrder() {
        opinionCommentApiCallResult = ApiCallResult.Inactive
        viewModelScope.launch {
            orderApiCallResult = ordersServiceWrapper
                .getOrder(orderId)
                .toApiCallResult()
        }
    }

    fun refresh() {
        opinionCommentApiCallResult = ApiCallResult.Inactive
        viewModelScope.launch {
            _isRefreshing.emit(true)
            orderApiCallResult = ordersServiceWrapper
                .getOrder(orderId)
                .toApiCallResult()
            _isRefreshing.emit(false)
        }
    }

    fun publishOpinion() {
        opinionCommentApiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = opinionsService.createOpinion(
                orderId,
                OpinionCreateRequest(opinionComment)
            )
            opinionCommentApiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = Unit)
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun deleteOpinion(opinion: Opinion, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            val response = opinionsService.deleteOpinion(opinion.id)
            if (response.isSuccessful) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun acceptOrder(onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            val response = ordersServiceWrapper.acceptOrder(orderId)
            if (response.isSuccessful) {
                getOrder()
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun rejectOrder(onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            val response = ordersServiceWrapper.rejectOrder(orderId)
            if (response.isSuccessful) {
                getOrder()
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun completeOrder(onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            val response = ordersServiceWrapper.completeOrder(orderId)
            if (response.isSuccessful) {
                getOrder()
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun verifyPayment(transactionId: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            val response = paymentsService.payOrder(orderId, TransactionRequest(transactionId))
            if (response.isSuccessful) {
                onSuccess()
                getOrder()
                return@launch
            } else {
                onFailure()
                return@launch
            }
        }
    }

    fun canPay(order: Order): Boolean {
        return isCustomer && (order.offer?.onlinePayment ?: false)
                && order.status == OrderStatus.Approved
                && order.payment == null
    }

    fun isOrderOwner(order: Order): Boolean {
        return order.offer?.distributorId == UserObserver.user.value?.id
                && (UserObserver.user.value is Distributor)
    }

    fun errorMessage(): String? = when (orderApiCallResult.statusCode) {
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }

    @AssistedFactory
    interface Factory {
        fun create(orderId: String): OrderDetailsViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            orderDetailsViewModelFactory: Factory,
            orderId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return orderDetailsViewModelFactory.create(orderId) as T
            }
        }
    }
}