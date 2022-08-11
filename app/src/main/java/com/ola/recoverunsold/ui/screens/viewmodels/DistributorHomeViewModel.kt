package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.query.PeriodQuery
import com.ola.recoverunsold.api.services.HomeService
import com.ola.recoverunsold.models.CustomerHomeData
import com.ola.recoverunsold.models.DistributorHomeData
import com.ola.recoverunsold.utils.misc.addSeconds
import com.ola.recoverunsold.utils.misc.minusSeconds
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import java.util.Date

private const val secondsPerWeek: ULong = 604800UL

class DistributorHomeViewModel(
    private val homeService: HomeService = KoinJavaComponent.get(HomeService::class.java)
) : ViewModel() {
    private val token = TokenStore.get()!!.bearerToken
    var homeDataApiCallResult: ApiCallResult<DistributorHomeData> by mutableStateOf(ApiCallResult.Inactive)
    var periodQuery: PeriodQuery by mutableStateOf(
        PeriodQuery(
            periodStart = Date().minusSeconds(secondsPerWeek),
            periodEnd = Date()
        )
    )

    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    init {
        getHomeData()
    }

    fun getHomeData() {
        homeDataApiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = homeService.getDistributorHomeData(
                token = token,
                period = periodQuery.toQueryMap()
            )

            homeDataApiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = response.body())
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun refresh() {
        homeDataApiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            _isRefreshing.emit(true)
            val response = homeService.getDistributorHomeData(
                token = token,
                period = periodQuery.toQueryMap()
            )
            homeDataApiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = response.body())
            } else {
                ApiCallResult.Error(code = response.code())
            }
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