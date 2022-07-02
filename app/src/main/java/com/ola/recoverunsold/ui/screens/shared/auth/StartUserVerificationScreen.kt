package com.ola.recoverunsold.ui.screens.shared.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.ola.recoverunsold.api.requests.UserVerificationStartRequest
import com.ola.recoverunsold.api.services.UserVerificationService
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.NavigationTextButton
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.EmailValidator
import com.ola.recoverunsold.utils.validation.FormState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get
import kotlin.ranges.contains

@Composable
fun StartUserVerificationScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    userVerificationViewModel: StartUserVerificationViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    ) { padding ->
        StartUserVerificationContent(
            email = userVerificationViewModel.email,
            onEmailChange = { userVerificationViewModel.email = it },
            onSubmit = {
                if (!userVerificationViewModel.formState.isValid) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = userVerificationViewModel.formState.errorMessage
                                ?: Strings.get(R.string.invalid_data),
                            actionLabel = Strings.get(R.string.ok),
                            duration = SnackbarDuration.Long
                        )
                    }
                } else {
                    userVerificationViewModel.submit()
                }
            },
            loading = userVerificationViewModel.apiCallResult.status == ApiStatus.LOADING,
            errorMessage = userVerificationViewModel.errorMessage(),
            navController = navController,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            modifier = Modifier.padding(padding),
            isSuccessful = userVerificationViewModel.apiCallResult.status == ApiStatus.SUCCESS,
            onValidationSuccess = {
                userVerificationViewModel.formState = userVerificationViewModel.formState.copy(
                    isValid = true,
                    errorMessage = null
                )
            },
            onValidationError = {
                userVerificationViewModel.formState = userVerificationViewModel.formState.copy(
                    isValid = false,
                    errorMessage = it
                )
            }
        )
    }
}

@Composable
fun StartUserVerificationContent(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
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
    val fieldsModifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.user_verification_instruction),
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
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            validator = EmailValidator(),
            onValidationSuccess = onValidationSuccess,
            onValidationError = onValidationError
        )

        if (loading) {
            Button(
                onClick = {},
                modifier = fieldsModifier,
                enabled = false
            ) {
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
            route = Routes.ConfirmUserVerification.path,
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

class StartUserVerificationViewModel(
    private val userVerificationService: UserVerificationService = get(
        UserVerificationService::class.java
    )
) : ViewModel() {
    var apiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive())
    var email by mutableStateOf("")
    var formState by mutableStateOf(FormState())

    fun submit() {
        apiCallResult = ApiCallResult.Loading()
        viewModelScope.launch {
            val response = userVerificationService.startUserVerification(
                UserVerificationStartRequest(email)
            )
            apiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = Unit)
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun errorMessage(): String? = when (apiCallResult.statusCode) {
        StatusCode.BadRequest.code -> Strings.get(R.string.account_already_verified)
        StatusCode.NotFound.code -> Strings.get(R.string.user_not_found)
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}