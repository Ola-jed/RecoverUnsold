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
import com.ola.recoverunsold.api.services.OpinionsService
import com.ola.recoverunsold.api.services.wrappers.OrderServiceWrapper
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.models.Opinion
import com.ola.recoverunsold.models.Order
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class OrderDetailsViewModelFactory(private val orderId: String) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OrderDetailsViewModel(orderId = orderId) as T
    }
}

class OrderDetailsViewModel(
    val orderId: String,
    private val ordersServiceWrapper: OrderServiceWrapper = KoinJavaComponent.get(
        OrderServiceWrapper::class.java
    ),
    private val opinionsService: OpinionsService = KoinJavaComponent.get(
        OpinionsService::class.java
    )
) : ViewModel() {
    private val token = TokenStore.get()!!.bearerToken
    var orderApiCallResult: ApiCallResult<Order> by mutableStateOf(ApiCallResult.Inactive)
    var opinionComment by mutableStateOf("")
    var opinionCommentApiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive)
    val isCustomer = UserObserver.user.value is Customer
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    init {
        getOrder()
    }

    fun getOrder() {
        opinionCommentApiCallResult = ApiCallResult.Inactive
        viewModelScope.launch {
            val response = ordersServiceWrapper.getOrder(token, orderId)
            orderApiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = response.body())
            } else {
                ApiCallResult.Error(code = null)
            }
        }
    }

    fun refresh() {
        opinionCommentApiCallResult = ApiCallResult.Inactive
        viewModelScope.launch {
            _isRefreshing.emit(true)
            val response = ordersServiceWrapper.getOrder(token, orderId)
            if (response.isSuccessful) {
                orderApiCallResult = ApiCallResult.Success(_data = response.body())
            }
            _isRefreshing.emit(false)
        }
    }

    fun publishOpinion() {
        opinionCommentApiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = opinionsService.createOpinion(
                orderId,
                token,
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
            val response = opinionsService.deleteOpinion(opinion.id, token)
            if (response.isSuccessful) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun errorMessage(): String? = when (orderApiCallResult.statusCode) {
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}