package com.ola.recoverunsold.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.requests.DistributorUpdateRequest
import com.ola.recoverunsold.api.services.AccountService
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.utils.misc.nullIfBlank
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver
import com.ola.recoverunsold.utils.validation.FormState
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class DistributorAccountViewModel(
    private val accountService: AccountService = KoinJavaComponent.get(AccountService::class.java)
) : ViewModel() {
    private val distributor = (UserObserver.user.value!! as Distributor)
    private val token = TokenStore.get()!!
    var phone by mutableStateOf(distributor.phone)
    var username by mutableStateOf(distributor.username)
    var taxId by mutableStateOf(distributor.taxId)
    var rccm by mutableStateOf(distributor.rccm)
    var websiteUrl by mutableStateOf(distributor.websiteUrl ?: "")
    var accountApiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive)
    var formState by mutableStateOf(FormState())

    fun updateDistributor() {
        accountApiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = accountService.updateDistributor(
                token.bearerToken,
                DistributorUpdateRequest(
                    username = username,
                    phone = phone,
                    taxId = taxId,
                    rccm = rccm,
                    websiteUrl = websiteUrl.nullIfBlank()
                )
            )
            accountApiCallResult = if (response.isSuccessful) {
                UserObserver.update(
                    (UserObserver.user.value as Distributor).copy(
                        username = username,
                        phone = phone,
                        taxId = taxId,
                        rccm = rccm,
                        websiteUrl = websiteUrl.nullIfBlank()
                    )
                )
                ApiCallResult.Success(_data = Unit)
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun deleteDistributor(onDeleteSuccess: () -> Unit) {
        accountApiCallResult = ApiCallResult.Loading
        viewModelScope.launch {
            val response = accountService.deleteAccount(token.bearerToken)
            accountApiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = Unit).also {
                    onDeleteSuccess()
                }
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun errorMessage(): String? = when (accountApiCallResult.statusCode) {
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}