package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.requests.AlertCreateRequest
import com.ola.recoverunsold.api.services.AlertsService
import com.ola.recoverunsold.api.services.DistributorService
import com.ola.recoverunsold.models.Alert
import com.ola.recoverunsold.models.AlertType
import com.ola.recoverunsold.models.DistributorLabel
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val alertsService: AlertsService,
    private val distributorsService: DistributorService
) : ViewModel() {
    private val token = TokenStore.get()!!.bearerToken
    var alertsApiCallResult: ApiCallResult<List<Alert>> by mutableStateOf(ApiCallResult.Inactive)
    var distributorsLabels: List<DistributorLabel> = emptyList()
    var alertType by mutableStateOf(AlertType.AnyOfferPublished)
    var distributorLabel: DistributorLabel? by mutableStateOf(null)

    init {
        getAlerts()
        getDistributorLabels()
    }

    fun getAlerts() {
        alertsApiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = alertsService.getAlerts(token)
            alertsApiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = response.body())
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    private fun getDistributorLabels() {
        viewModelScope.launch {
            val response = distributorsService.getDistributorsLabels()
            if (response.isSuccessful) {
                distributorsLabels = response.body()!!
            }
        }
    }

    fun createAlert(onSuccess: () -> Unit, onFailure: (Int) -> Unit) {
        viewModelScope.launch {
            val alertCreateRequest = AlertCreateRequest(
                alertType = alertType,
                distributorId = if (alertType == AlertType.AnyOfferPublished || distributorLabel == null) {
                    null
                } else {
                    distributorLabel!!.id
                }
            )
            val response = alertsService.createAlert(token, alertCreateRequest)
            if (response.isSuccessful) {
                onSuccess()
            } else {
                onFailure(response.code())
            }
        }
    }

    fun deleteAlert(alert: Alert, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            val response = alertsService.deleteAlert(token, alert.id)
            if (response.isSuccessful) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun errorMessage(): String? {
        return when (alertsApiCallResult.statusCode) {
            in 400..600 -> Strings.get(R.string.unknown_error_occured)
            else -> null
        }
    }
}