package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.query.DistributorFilterQuery
import com.ola.recoverunsold.api.services.wrappers.DistributorServiceWrapper
import com.ola.recoverunsold.models.DistributorInformation
import com.ola.recoverunsold.models.Page
import com.ola.recoverunsold.utils.extensions.toApiCallResult
import com.ola.recoverunsold.utils.resources.Strings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DistributorsViewModel @Inject constructor(
    private val distributorServiceWrapper: DistributorServiceWrapper
) : ViewModel() {
    var distributorFilterQuery by mutableStateOf(DistributorFilterQuery())
    var distributorsApiResult: ApiCallResult<Page<DistributorInformation>> by mutableStateOf(
        ApiCallResult.Inactive
    )
    var isSearching by mutableStateOf(true)


    init {
        getDistributors()
    }

    fun getDistributors() {
        distributorsApiResult = ApiCallResult.Loading
        viewModelScope.launch {
            distributorsApiResult = distributorServiceWrapper
                .getDistributors(distributorFilterQuery)
                .toApiCallResult()
        }
    }

    fun getNext() {
        distributorFilterQuery = distributorFilterQuery.inc()
        getDistributors()
    }

    fun getPrevious() {
        distributorFilterQuery = distributorFilterQuery.dec()
        getDistributors()
    }

    fun resetFilter() {
        distributorFilterQuery = DistributorFilterQuery()
    }

    fun errorMessage(): String? {
        return if (distributorsApiResult.status == ApiStatus.ERROR) {
            Strings.get(R.string.unknown_error_occured)
        } else {
            null
        }
    }
}