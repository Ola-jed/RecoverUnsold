package com.ola.recoverunsold.ui.screens.shared.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.ola.recoverunsold.api.requests.LoginRequest
import com.ola.recoverunsold.api.responses.Token
import com.ola.recoverunsold.api.responses.TokenRoles
import com.ola.recoverunsold.api.services.AccountService
import com.ola.recoverunsold.api.services.AuthService
import com.ola.recoverunsold.ui.components.app.AppHero
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.NavigationTextButton
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver
import com.ola.recoverunsold.utils.validation.EmailValidator
import com.ola.recoverunsold.utils.validation.IsRequiredValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get
import kotlin.ranges.contains

@Composable
fun LoginScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    loginViewModel: LoginViewModel = viewModel(),
    tokenStore: TokenStore = TokenStore(LocalContext.current)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    ) { padding ->
        LoginScreenContent(
            email = loginViewModel.emailFieldText,
            password = loginViewModel.passwordFieldText,
            onEmailChange = { loginViewModel.emailFieldText = it },
            onEmailValidated = { loginViewModel.email = it },
            onPasswordChange = { loginViewModel.passwordFieldText = it },
            onPasswordValidated = { loginViewModel.password = it },
            onSubmit = {
                loginViewModel.login {
                    val token = (loginViewModel.apiCallResult as ApiCallResult.Success)._data
                    coroutineScope.launch {
                        if (token != null) {
                            tokenStore.removeToken()
                            tokenStore.storeToken(token)
                            TokenStore.init { token }
                            val accountService: AccountService = get(AccountService::class.java)
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
                }
            },
            loading = loginViewModel.apiCallResult.status == ApiStatus.LOADING,
            errorMessage = loginViewModel.errorMessage(),
            navController = navController,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            modifier = Modifier.padding(padding),
            isSuccessful = loginViewModel.apiCallResult.status == ApiStatus.SUCCESS,
        )
    }
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppHero(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 60.dp),
            text = stringResource(R.string.login_label)
        )

        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val fieldsModifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)

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

            NavigationTextButton(
                navController = navController,
                route = Routes.ForgotPassword.path,
                text = R.string.forgot_password_question,
                outerModifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.Start)
            )

            if (loading) {
                Button(onClick = {}) {
                    CircularProgressIndicator(color = MaterialTheme.colors.background)
                }
            } else {
                Button(
                    onClick = onSubmit,
                    modifier = fieldsModifier,
                ) {
                    Text(stringResource(R.string.login_action), modifier = Modifier.padding(5.dp))
                }
            }

            NavigationTextButton(
                navController = navController,
                route = Routes.Register.path,
                text = R.string.not_registered
            )

            NavigationTextButton(
                navController = navController,
                route = Routes.StartUserVerification.path,
                text = R.string.account_not_verified_question
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
                    navController.navigate(Routes.Home.path) {
                        popUpTo(Routes.Home.path) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}


class LoginViewModel(private val authService: AuthService = get(AuthService::class.java)) :
    ViewModel() {
    var apiCallResult: ApiCallResult<Token> by mutableStateOf(ApiCallResult.Inactive())
    var emailFieldText by mutableStateOf("")
    var passwordFieldText by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    fun login(onSuccess: () -> Unit = {}) {
        if (email.isBlank() || password.isBlank()) return
        apiCallResult = ApiCallResult.Loading()
        viewModelScope.launch {
            val response = authService.login(LoginRequest(email = email, password = password))
            apiCallResult = if (response.isSuccessful) {
                val token = response.body()
                ApiCallResult.Success(_data = token)
            } else {
                ApiCallResult.Error(code = response.code())
            }
            if (apiCallResult is ApiCallResult.Success) {
                onSuccess()
            }
        }
    }

    fun errorMessage(): String? = when (apiCallResult.statusCode) {
        StatusCode.Unauthorized.code -> Strings.get(R.string.invalid_credentials)
        StatusCode.Forbidden.code -> Strings.get(R.string.account_not_verified)
        StatusCode.BadRequest.code -> Strings.get(R.string.invalid_data)
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}