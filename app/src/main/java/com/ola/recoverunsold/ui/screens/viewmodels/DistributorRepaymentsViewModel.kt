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
import com.ola.recoverunsold.api.query.RepaymentFilterQuery
import com.ola.recoverunsold.api.services.RepaymentService
import com.ola.recoverunsold.models.Page
import com.ola.recoverunsold.models.Repayment
import com.ola.recoverunsold.utils.extensions.toApiCallResult
import com.ola.recoverunsold.utils.resources.Strings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DistributorRepaymentsViewModel @Inject constructor(
    private val repaymentService: RepaymentService
) : ViewModel() {
    var repaymentsResponse: ApiCallResult<Page<Repayment>> by mutableStateOf(ApiCallResult.Inactive)
    var repaymentsFilterQuery by mutableStateOf(RepaymentFilterQuery())

    init {
        getRepayments()
    }

    fun getRepayments() {
        repaymentsResponse = ApiCallResult.Loading
        viewModelScope.launch {
            repaymentsResponse = repaymentService
                .getRepayments(repaymentsFilterQuery.toQueryMap())
                .toApiCallResult()
        }
    }

    fun loadMore() {
        repaymentsFilterQuery = repaymentsFilterQuery.inc()
        val savedPage = repaymentsResponse.data!!
        repaymentsResponse = ApiCallResult.Loading
        viewModelScope.launch {
            val extraApiCallResponse = repaymentService
                .getRepayments(repaymentsFilterQuery.toQueryMap())
                .toApiCallResult()

            if (extraApiCallResponse is ApiCallResult.Error) {
                repaymentsResponse = extraApiCallResponse
            } else if (extraApiCallResponse is ApiCallResult.Success) {
                repaymentsResponse = extraApiCallResponse.copy(
                    _data = extraApiCallResponse.data!!.prepend(savedPage)
                )
            }
        }
    }

    fun errorMessage(): String? {
        if (repaymentsResponse.status == ApiStatus.ERROR) {
            return when (repaymentsResponse.statusCode) {
                StatusCode.Unauthorized.code -> Strings.get(R.string.not_authenticated_full_message)
                else -> Strings.get(R.string.unknown_error_occured)
            }
        }
        return null
    }
}