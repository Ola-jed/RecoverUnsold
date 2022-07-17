package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.core.StatusCode
import com.ola.recoverunsold.api.query.PaginationQuery
import com.ola.recoverunsold.api.services.wrappers.LocationServiceWrapper
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.models.Page
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class LocationsSectionViewModel(
    private val locationServiceWrapper: LocationServiceWrapper = KoinJavaComponent.get(
        LocationServiceWrapper::class.java
    )
) : ViewModel() {
    private val token = TokenStore.get()!!
    var locationsGetResponse: ApiCallResult<Page<Location>> by mutableStateOf(ApiCallResult.Inactive)
    private var paginationQuery by mutableStateOf(PaginationQuery())

    init {
        getLocations()
    }

    fun getLocations() {
        locationsGetResponse = ApiCallResult.Loading
        viewModelScope.launch {
            val response = locationServiceWrapper.getLocations(token.bearerToken, paginationQuery)
            locationsGetResponse = if (response.isSuccessful) {
                ApiCallResult.Success(_data = response.body())
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun getNext() {
        paginationQuery = paginationQuery.inc()
        getLocations()
    }

    fun getPrevious() {
        paginationQuery = paginationQuery.dec()
        getLocations()
    }

    fun deleteLocation(location: Location, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            val response = locationServiceWrapper.deleteLocation(token.bearerToken, location.id)
            if (response.isSuccessful) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun errorMessage(): String? {
        if (locationsGetResponse.status == ApiStatus.ERROR) {
            return when (locationsGetResponse.statusCode) {
                StatusCode.Unauthorized.code -> Strings.get(R.string.not_authenticated_full_message)
                else -> Strings.get(R.string.unknown_error_occured)
            }
        }
        return null
    }
}