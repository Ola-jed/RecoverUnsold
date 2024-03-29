package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.services.HomeService
import com.ola.recoverunsold.models.CustomerHomeData
import com.ola.recoverunsold.utils.extensions.toApiCallResult
import com.ola.recoverunsold.utils.resources.Strings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeService: HomeService) : ViewModel() {
    var homeDataApiCallResult: ApiCallResult<CustomerHomeData> by mutableStateOf(ApiCallResult.Inactive)
    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    init {
        getHomeData()
    }

    private fun getHomeData() {
        homeDataApiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            homeDataApiCallResult = homeService
                .getCustomerHomeData()
                .toApiCallResult()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            homeDataApiCallResult = homeService
                .getCustomerHomeData()
                .toApiCallResult()
            _isRefreshing.emit(false)
        }
    }

    fun errorMessage(): String? {
        return when (homeDataApiCallResult.statusCode) {
            in 400..600 -> Strings.get(R.string.unknown_error_occured)
            else -> null
        }
    }
}