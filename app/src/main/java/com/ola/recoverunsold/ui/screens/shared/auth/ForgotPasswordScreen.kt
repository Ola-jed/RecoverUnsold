package com.ola.recoverunsold.ui.screens.shared.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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
import com.ola.recoverunsold.api.requests.ForgotPasswordStartRequest
import com.ola.recoverunsold.api.services.ForgotPasswordService
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.NavigationTextButton
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.EmailValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    forgotPasswordViewModel: ForgotPasswordViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    ) { padding ->
        ForgotPasswordContent(
            email = forgotPasswordViewModel.emailFieldText,
            onEmailChange = { forgotPasswordViewModel.emailFieldText = it },
            onEmailValidated = { forgotPasswordViewModel.email = it },
            onSubmit = { forgotPasswordViewModel.submit() },
            loading = forgotPasswordViewModel.apiCallResult.status == ApiStatus.LOADING,
            errorMessage = forgotPasswordViewModel.errorMessage(),
            navController = navController,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            modifier = Modifier.padding(padding),
            isSuccessful = forgotPasswordViewModel.apiCallResult.status == ApiStatus.SUCCESS
        )
    }
}

@Composable
fun ForgotPasswordContent(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    onEmailValidated: (String) -> Unit,
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
            stringResource(R.string.forgot_password_indications),
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 10.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )

        CustomTextInput(
            modifier = fieldsModifier,
            value = email,
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
            placeholder = { Text(text = stringResource(R.string.email_placeholder)) },
            label = { Text(text = stringResource(R.string.email_label)) },
            onValueChange = onEmailChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            validator = EmailValidator(),
            onValidatedValue = onEmailValidated
        )

        if (loading) {
            Button(onClick = {}) {
                CircularProgressIndicator(color = MaterialTheme.colors.background)
            }
        } else {
            Button(onClick = onSubmit, modifier = fieldsModifier) {
                Text(
                    stringResource(R.string.send_code_action),
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
            route = Routes.PasswordReset.path,
            text = R.string.code_already_sent
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
                        message = Strings.get(R.string.code_sent_successfully),
                        actionLabel = Strings.get(R.string.ok),
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }
}


class ForgotPasswordViewModel(
    private val forgotPasswordService: ForgotPasswordService = get(
        ForgotPasswordService::class.java
    )
) : ViewModel() {
    var apiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive())
    var emailFieldText by mutableStateOf("")
    var email by mutableStateOf("")

    fun submit() {
        if (email.isBlank()) return
        apiCallResult = ApiCallResult.Loading()
        viewModelScope.launch {
            val response = forgotPasswordService.startForgotPassword(
                ForgotPasswordStartRequest(email)
            )
            apiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = Unit)
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun errorMessage(): String? = when (apiCallResult.statusCode) {
        StatusCode.NotFound.code -> Strings.get(R.string.user_not_found)
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}