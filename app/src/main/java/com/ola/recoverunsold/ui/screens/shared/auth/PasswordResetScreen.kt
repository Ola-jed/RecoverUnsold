package com.ola.recoverunsold.ui.screens.shared.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
import com.ola.recoverunsold.api.requests.PasswordResetRequest
import com.ola.recoverunsold.api.services.ForgotPasswordService
import com.ola.recoverunsold.ui.components.CustomTextInput
import com.ola.recoverunsold.ui.components.NavigationTextButton
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.IsRequiredValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

@Composable
fun PasswordResetScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    passwordResetViewModel: PasswordResetViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    ) { padding ->
        PasswordResetContent(
            modifier = Modifier.padding(padding),
            token = passwordResetViewModel.tokenFieldText,
            onTokenChange = { passwordResetViewModel.tokenFieldText = it },
            onTokenValidated = { passwordResetViewModel.token = it },
            password = passwordResetViewModel.passwordFieldText,
            onPasswordChange = { passwordResetViewModel.password = it },
            onPasswordValidated = { passwordResetViewModel.password = it },
            onSubmit = { passwordResetViewModel.submit() },
            loading = passwordResetViewModel.apiCallResult.status == ApiStatus.LOADING,
            errorMessage = passwordResetViewModel.errorMessage(),
            navController = navController,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            isSuccessful = passwordResetViewModel.apiCallResult.status == ApiStatus.SUCCESS,
        )
    }
}

@Composable
fun PasswordResetContent(
    modifier: Modifier = Modifier,
    token: String,
    onTokenChange: (String) -> Unit,
    onTokenValidated: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onPasswordValidated: (String) -> Unit,
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
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val fieldsModifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)

        Text(
            stringResource(R.string.password_reset_instruction),
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 10.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )

        CustomTextInput(
            modifier = fieldsModifier,
            value = token,
            leadingIcon = { Icon(Icons.Filled.Menu, contentDescription = null) },
            placeholder = { Text(text = stringResource(R.string.code)) },
            label = { Text(text = stringResource(R.string.code)) },
            onValueChange = onTokenChange,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            validator = IsRequiredValidator(),
            onValidatedValue = onTokenValidated
        )

        CustomTextInput(
            modifier = fieldsModifier,
            value = password,
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
            placeholder = { Text(text = stringResource(R.string.password_placeholder)) },
            label = { Text(text = stringResource(R.string.password_label)) },
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            validator = IsRequiredValidator(),
            onValidatedValue = onPasswordValidated
        )

        if (loading) {
            Button(onClick = {}) {
                CircularProgressIndicator(color = MaterialTheme.colors.background)
            }
        } else {
            Button(onClick = onSubmit, modifier = fieldsModifier) {
                Text(
                    stringResource(R.string.reset_password_message),
                    modifier = Modifier.padding(vertical = 5.dp)
                )
            }
        }

        NavigationTextButton(
            navController = navController,
            route = Routes.Login.path,
            text = R.string.login_action
        )
        NavigationTextButton(
            navController = navController,
            route = Routes.ForgotPassword.path,
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
                navController.navigate(Routes.PasswordResetSuccess.path)
            }
        }
    }
}

class PasswordResetViewModel(
    private val forgotPasswordService: ForgotPasswordService = get(
        ForgotPasswordService::class.java
    )
) : ViewModel() {
    var apiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive())
    var tokenFieldText by mutableStateOf("")
    var token by mutableStateOf("")
    var passwordFieldText by mutableStateOf("")
    var password by mutableStateOf("")

    fun submit() {
        if (token.isBlank() || password.isBlank()) return
        apiCallResult = ApiCallResult.Loading()
        viewModelScope.launch {
            val response = forgotPasswordService.resetPassword(
                PasswordResetRequest(
                    password, token
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