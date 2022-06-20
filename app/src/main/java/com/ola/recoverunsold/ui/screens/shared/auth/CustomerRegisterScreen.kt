package com.ola.recoverunsold.ui.screens.shared.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.core.StatusCode
import com.ola.recoverunsold.api.requests.CustomerRegisterRequest
import com.ola.recoverunsold.api.requests.UserVerificationStartRequest
import com.ola.recoverunsold.api.services.AuthService
import com.ola.recoverunsold.api.services.UserVerificationService
import com.ola.recoverunsold.ui.components.AppHero
import com.ola.recoverunsold.ui.components.CustomTextInput
import com.ola.recoverunsold.ui.components.NavigationTextButton
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.EmailValidator
import com.ola.recoverunsold.utils.validation.IsRequiredValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

@Composable
fun CustomerRegisterScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    customerRegisterViewModel: CustomerRegisterViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    ) { padding ->
        CustomerRegisterContent(
            email = customerRegisterViewModel.emailFieldText,
            password = customerRegisterViewModel.passwordFieldText,
            username = customerRegisterViewModel.usernameFieldText,
            onUsernameChange = { customerRegisterViewModel.usernameFieldText = it },
            onUsernameValidated = { customerRegisterViewModel.username = it },
            onEmailChange = { customerRegisterViewModel.emailFieldText = it },
            onEmailValidated = { customerRegisterViewModel.email = it },
            onPasswordChange = { customerRegisterViewModel.passwordFieldText = it },
            onPasswordValidated = { customerRegisterViewModel.password = it },
            onSubmit = { customerRegisterViewModel.register() },
            loading = customerRegisterViewModel.apiCallResult.status == ApiStatus.LOADING,
            errorMessage = customerRegisterViewModel.errorMessage(),
            navController = navController,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            modifier = Modifier.padding(padding),
            isSuccessful = customerRegisterViewModel.apiCallResult.status == ApiStatus.SUCCESS,
        )
    }
}

@Composable
fun CustomerRegisterContent(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    username: String,
    onUsernameChange: (String) -> Unit,
    onUsernameValidated: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onEmailValidated: (String) -> Unit,
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
    val fieldsModifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHero(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp),
            text = stringResource(R.string.login_label)
        )

        Column(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextInput(
                modifier = fieldsModifier,
                value = username,
                leadingIcon = { Icon(Icons.Filled.AccountBox, contentDescription = null) },
                placeholder = { Text(text = stringResource(R.string.username_placeholder)) },
                label = { Text(text = stringResource(R.string.username_label)) },
                onValueChange = onUsernameChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                validator = IsRequiredValidator(),
                onValidatedValue = onUsernameValidated
            )

            CustomTextInput(
                modifier = fieldsModifier,
                value = email,
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                placeholder = { Text(text = stringResource(R.string.email_placeholder)) },
                label = { Text(text = stringResource(R.string.email_label)) },
                onValueChange = onEmailChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                validator = EmailValidator(),
                onValidatedValue = onEmailValidated
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
                        stringResource(R.string.register_action),
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
            }

            NavigationTextButton(
                navController = navController,
                route = Routes.Login.path,
                text = R.string.already_registered
            )

            NavigationTextButton(
                navController = navController,
                route = Routes.StartUserVerification.path,
                text = R.string.verification_code_not_sent
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
                    navController.navigate(Routes.ConfirmUserVerification.path)
                }
            }
        }
    }
}


class CustomerRegisterViewModel(
    private val authService: AuthService = get(AuthService::class.java),
    private val userVerificationService: UserVerificationService = get(UserVerificationService::class.java),
) : ViewModel() {
    var apiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive())
    var emailFieldText by mutableStateOf("")
    var passwordFieldText by mutableStateOf("")
    var usernameFieldText by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var username by mutableStateOf("")

    fun register() {
        if (email.isBlank() || password.isBlank() || username.isBlank()) return
        apiCallResult = ApiCallResult.Loading()
        viewModelScope.launch {
            val response = authService.registerCustomer(
                CustomerRegisterRequest(
                    username,
                    email,
                    password
                )
            )
            apiCallResult = if (response.isSuccessful) {
                val userVerificationResponse = userVerificationService.startUserVerification(
                    UserVerificationStartRequest(email)
                )
                if (userVerificationResponse.isSuccessful) {
                    ApiCallResult.Success(Unit)
                } else {
                    ApiCallResult.Error(code = StatusCode.Unknown.code)
                }
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun errorMessage(): String? = when (apiCallResult.statusCode) {
        StatusCode.BadRequest.code -> Strings.get(R.string.invalid_data)
        StatusCode.Unknown.code -> Strings.get(R.string.register_success_verification_failed)
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}