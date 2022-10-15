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
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.messaging.FirebaseMessaging
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.requests.FcmTokenCreateRequest
import com.ola.recoverunsold.api.responses.TokenRoles
import com.ola.recoverunsold.ui.components.app.AppHero
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.NavigationTextButton
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.LoginViewModel
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver
import com.ola.recoverunsold.utils.validation.EmailValidator
import com.ola.recoverunsold.utils.validation.IsRequiredValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun LoginScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    loginViewModel: LoginViewModel = hiltViewModel(),
    tokenStore: TokenStore = TokenStore(LocalContext.current)
) {
    val coroutineScope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    ) { padding ->
        LoginScreenContent(
            modifier = Modifier.padding(padding),
            email = loginViewModel.email,
            password = loginViewModel.password,
            onEmailChange = { loginViewModel.email = it },
            onPasswordChange = { loginViewModel.password = it },
            onSubmit = {
                if (!loginViewModel.formState.isValid) {
                    coroutineScope.launch {
                        snackbarHostState.show(
                            message = loginViewModel.formState.errorMessage
                                ?: Strings.get(R.string.invalid_data)
                        )
                    }
                } else {
                    loginViewModel.login {
                        val token = (loginViewModel.apiCallResult as ApiCallResult.Success)._data
                        coroutineScope.launch {
                            if (token != null) {
                                tokenStore.removeToken()
                                tokenStore.storeToken(token)
                                TokenStore.init { token }
                                val accountService = loginViewModel.accountService()
                                val fcmService = loginViewModel.fcmTokenService()
                                FirebaseMessaging.getInstance()
                                    .token
                                    .addOnSuccessListener {
                                        runBlocking {
                                            fcmService.createFcmToken(
                                                token.bearerToken,
                                                FcmTokenCreateRequest(it)
                                            )
                                        }
                                    }
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
                                navController.navigate(Routes.Home.path) {
                                    popUpTo(Routes.Home.path) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
                }
            },
            loading = loginViewModel.loading,
            errorMessage = loginViewModel.errorMessage(),
            navController = navController,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            isSuccessful = loginViewModel.apiCallResult.status == ApiStatus.SUCCESS,
            onValidationSuccess = {
                loginViewModel.formState = loginViewModel.formState.copy(
                    isValid = true,
                    errorMessage = null
                )
            },
            onValidationError = {
                loginViewModel.formState = loginViewModel.formState.copy(
                    isValid = false,
                    errorMessage = it
                )
            },
            isPasswordVisible = passwordVisible,
            onPasswordHideOrShow = { passwordVisible = !passwordVisible }
        )
    }
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onPasswordHideOrShow: () -> Unit,
    onSubmit: () -> Unit,
    loading: Boolean,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    errorMessage: String? = null,
    onValidationError: (String) -> Unit,
    onValidationSuccess: () -> Unit,
    isSuccessful: Boolean
) {
    val focusManager = LocalFocusManager.current
//    Log.e("LOADING", loading.toString())

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
                onValidationSuccess = onValidationSuccess,
                onValidationError = onValidationError
            )

            CustomTextInput(
                modifier = fieldsModifier,
                value = password,
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = onPasswordHideOrShow) {
                        Icon(
                            imageVector = if (isPasswordVisible) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = null
                        )
                    }
                },
                placeholder = { Text(text = stringResource(R.string.password_placeholder)) },
                label = { Text(text = stringResource(R.string.password_label)) },
                onValueChange = onPasswordChange,
                visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                validator = IsRequiredValidator(),
                onValidationSuccess = onValidationSuccess,
                onValidationError = onValidationError
            )

            NavigationTextButton(
                navController = navController,
                route = Routes.ForgotPassword.path,
                text = R.string.forgot_password_question,
                outerModifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.Start)
            )

            Button(modifier = fieldsModifier, onClick = onSubmit, enabled = !loading) {
                if (loading) {
                    CircularProgressIndicator(color = MaterialTheme.colors.background)
                } else {
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
                        snackbarHostState.show(message = errorMessage)
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