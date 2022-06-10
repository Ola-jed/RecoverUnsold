package com.ola.recoverunsold.ui.screens.shared.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.core.StatusCode
import com.ola.recoverunsold.api.requests.UserVerificationConfirmRequest
import com.ola.recoverunsold.api.services.UserVerificationService
import com.ola.recoverunsold.ui.components.CustomTextInput
import com.ola.recoverunsold.ui.components.NavigationTextButton
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.IsRequiredValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

@Composable
fun UserVerificationScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    userVerificationViewModel: UserVerificationViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    ) { padding ->
        UserVerificationContent(
            modifier = Modifier.padding(padding),
            token = userVerificationViewModel.tokenFieldText,
            onTokenChange = { userVerificationViewModel.tokenFieldText = it },
            onTokenValidated = { userVerificationViewModel.token = it },
            onSubmit = { userVerificationViewModel.submit() },
            loading = userVerificationViewModel.apiCallResult.status == ApiStatus.LOADING,
            navController = navController,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            isSuccessful = userVerificationViewModel.apiCallResult.status == ApiStatus.SUCCESS
        )
    }
}

@Composable
fun UserVerificationContent(
    modifier: Modifier = Modifier,
    token: String,
    onTokenChange: (String) -> Unit,
    onTokenValidated: (String) -> Unit,
    onSubmit: () -> Unit,
    loading: Boolean,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    errorMessage: String? = null,
    isSuccessful: Boolean
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.user_verification_confirm_instruction),
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 10.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )

        CustomTextInput(
            value = token,
            leadingIcon = { Icon(Icons.Filled.Menu, contentDescription = null) },
            placeholder = { Text(text = stringResource(R.string.code)) },
            label = { Text(text = stringResource(R.string.code)) },
            onValueChange = onTokenChange,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            validator = IsRequiredValidator(),
            onValidatedValue = onTokenValidated
        )

        if (loading) {
            Button(onClick = {}) {
                CircularProgressIndicator(color = MaterialTheme.colors.background)
            }
        } else {
            Button(onClick = onSubmit, modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(stringResource(R.string.verify_account_action))
            }
        }

        NavigationTextButton(
            navController = navController,
            route = Routes.Login.path,
            text = R.string.login_action
        )
        NavigationTextButton(
            navController = navController,
            route = Routes.StartUserVerification.path,
            text = R.string.code_not_sent
        )

        if (errorMessage != null) {
            LaunchedEffect(snackbarHostState) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = errorMessage,
                        actionLabel = Strings.get(R.string.ok),
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }

        if (isSuccessful) {
            LaunchedEffect(snackbarHostState) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = Strings.get(R.string.account_verified_success_message),
                        actionLabel = Strings.get(R.string.ok),
                        duration = SnackbarDuration.Long
                    )
                }
                navController.navigate(Routes.Login.path)
            }
        }
    }
}

class UserVerificationViewModel(
    private val userVerificationService: UserVerificationService = get(
        UserVerificationService::class.java
    )
) : ViewModel() {
    var apiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive())
    var tokenFieldText by mutableStateOf("")
    var token by mutableStateOf("")

    fun submit() {
        if (token.isBlank()) return
        apiCallResult = ApiCallResult.Loading()
        viewModelScope.launch {
            val response = userVerificationService.confirmUserVerification(
                UserVerificationConfirmRequest(
                    token
                )
            )
            apiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = Unit)
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun errorMessage(): String? = when (apiCallResult.statusCode) {
        StatusCode.BadRequest.code -> Strings.get(R.string.invalid_expired_code)
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}