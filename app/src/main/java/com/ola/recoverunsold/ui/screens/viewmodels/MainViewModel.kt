package com.ola.recoverunsold.ui.screens.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ola.recoverunsold.api.responses.TokenRoles
import com.ola.recoverunsold.api.services.AccountService
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver
import com.ola.recoverunsold.utils.store.toApiToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val accountService: AccountService) : ViewModel() {
    private val _hasFetchedData = MutableStateFlow(false)
    val hasFinishedLoading = _hasFetchedData.asStateFlow()

    fun fetchData(context: Context) {
        viewModelScope.launch {
            val storedToken = TokenStore(context).token().firstOrNull()?.toApiToken()
            try {
                if (storedToken != null) {
                    val token = TokenStore.getOr { storedToken }
                    if (token.expirationDate.before(Date())) {
                        TokenStore(context).removeToken()
                    } else {
                        val response = if (token.role == TokenRoles.CUSTOMER) {
                            accountService.getCustomer(token.bearerToken)
                        } else {
                            accountService.getDistributor(token.bearerToken)
                        }
                        if (response.isSuccessful) {
                            val user = response.body()
                            if (user != null) {
                                UserObserver.update(user)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Nothing
            }
            _hasFetchedData.value = true
        }
    }
}