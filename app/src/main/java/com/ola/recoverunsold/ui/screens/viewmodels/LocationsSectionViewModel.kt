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
import com.ola.recoverunsold.utils.extensions.toApiCallResult
import com.ola.recoverunsold.utils.resources.Strings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationsSectionViewModel @Inject constructor(
    private val locationServiceWrapper: LocationServiceWrapper
) : ViewModel() {
    var locationsGetResponse: ApiCallResult<Page<Location>> by mutableStateOf(ApiCallResult.Inactive)
    private var paginationQuery by mutableStateOf(PaginationQuery())

    init {
        getLocations()
    }

    fun getLocations() {
        locationsGetResponse = ApiCallResult.Loading
        viewModelScope.launch {
            locationsGetResponse = locationServiceWrapper
                .getLocations(paginationQuery)
                .toApiCallResult()
        }
    }

    fun loadMore() {
        paginationQuery = paginationQuery.inc()
        val savedPage = locationsGetResponse.data!!
        locationsGetResponse = ApiCallResult.Loading
        viewModelScope.launch {
            val extraApiCallResponse = locationServiceWrapper
                .getLocations(paginationQuery)
                .toApiCallResult()

            if (extraApiCallResponse is ApiCallResult.Error) {
                locationsGetResponse = extraApiCallResponse
            } else if (extraApiCallResponse is ApiCallResult.Success) {
                locationsGetResponse = extraApiCallResponse.copy(
                    _data = extraApiCallResponse.data!!.prepend(savedPage)
                )
            }
        }
    }

    fun deleteLocation(location: Location, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            val response = locationServiceWrapper.deleteLocation(location.id)
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