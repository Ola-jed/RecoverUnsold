package com.ola.recoverunsold.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.responses.Token
import com.ola.recoverunsold.api.responses.TokenRoles
import com.ola.recoverunsold.api.services.AccountService
import com.ola.recoverunsold.models.User
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.toApiToken
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.get

@Composable
fun DrawerContent(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    drawerViewModel: DrawerViewModel = viewModel(),
    tokenStore: TokenStore = TokenStore(LocalContext.current)
): @Composable ColumnScope.() -> Unit {
    var user by remember { mutableStateOf<User?>(null) }
    var fetched by remember { mutableStateOf(false) }

    runBlocking {
        if (!fetched) {
            val apiToken = tokenStore.token().firstOrNull()?.toApiToken()
            if (apiToken != null) {
                val token = TokenStore.getOr { apiToken }
                drawerViewModel.loadUser(token)
            }
            fetched = true
        }
    }

    when (drawerViewModel.apiCallResult.status) {
        ApiStatus.INACTIVE, ApiStatus.ERROR -> return VisitorDrawer(navController = navController)
        ApiStatus.SUCCESS -> {
            user = drawerViewModel.apiCallResult.data
            return AuthDrawer(
                user = user!!,
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
        ApiStatus.LOADING -> return {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

class DrawerViewModel(private val accountService: AccountService = get(AccountService::class.java)) :
    ViewModel() {
    var apiCallResult: ApiCallResult<User> by mutableStateOf(ApiCallResult.Inactive())


    fun loadUser(token: Token) {
        apiCallResult = ApiCallResult.Loading()
        viewModelScope.launch {
            val response = if (token.role == TokenRoles.CUSTOMER) {
                accountService.getCustomer(token.bearerToken)
            } else {
                accountService.getDistributor(token.bearerToken)
            }
            apiCallResult = if (response.isSuccessful) {
                val user = response.body()
                ApiCallResult.Success(user)
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }
}

