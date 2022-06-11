package com.ola.recoverunsold.ui.screens.shared.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.ola.recoverunsold.api.requests.DistributorRegisterRequest
import com.ola.recoverunsold.api.requests.UserVerificationStartRequest
import com.ola.recoverunsold.api.services.AuthService
import com.ola.recoverunsold.api.services.UserVerificationService
import com.ola.recoverunsold.ui.components.CustomTextInput
import com.ola.recoverunsold.ui.components.NavigationTextButton
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.EmailValidator
import com.ola.recoverunsold.utils.validation.IsRequiredValidator
import com.ola.recoverunsold.utils.validation.PhoneValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

@Composable
fun DistributorRegisterScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    distributorRegisterViewModel: DistributorRegisterViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    ) { padding ->
        DistributorRegisterContent(
            email = distributorRegisterViewModel.emailFieldText,
            password = distributorRegisterViewModel.passwordFieldText,
            username = distributorRegisterViewModel.usernameFieldText,
            phone = distributorRegisterViewModel.phoneFieldText,
            rccm = distributorRegisterViewModel.rccmFieldText,
            taxId = distributorRegisterViewModel.taxIdFieldText,
            websiteUrl = distributorRegisterViewModel.websiteUrlFieldText,
            onUsernameChange = { distributorRegisterViewModel.usernameFieldText = it },
            onUsernameValidated = { distributorRegisterViewModel.username = it },
            onEmailChange = { distributorRegisterViewModel.emailFieldText = it },
            onEmailValidated = { distributorRegisterViewModel.email = it },
            onPasswordChange = { distributorRegisterViewModel.passwordFieldText = it },
            onPasswordValidated = { distributorRegisterViewModel.password = it },
            onPhoneChange = { distributorRegisterViewModel.phoneFieldText = it },
            onPhoneValidated = { distributorRegisterViewModel.phone = it },
            onRccmChange = { distributorRegisterViewModel.rccmFieldText = it },
            onRccmValidated = { distributorRegisterViewModel.rccm = it },
            onTaxIdChange = { distributorRegisterViewModel.taxIdFieldText = it },
            onTaxIdValidated = { distributorRegisterViewModel.taxId = it },
            onWebsiteUrlChange = { distributorRegisterViewModel.websiteUrlFieldText = it },
            onWebsiteUrlValidated = { distributorRegisterViewModel.websiteUrl = it },
            onSubmit = { distributorRegisterViewModel.register() },
            loading = distributorRegisterViewModel.apiCallResult.status == ApiStatus.LOADING,
            errorMessage = distributorRegisterViewModel.errorMessage(),
            navController = navController,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            modifier = Modifier.padding(padding),
            isSuccessful = distributorRegisterViewModel.apiCallResult.status == ApiStatus.SUCCESS,
        )
    }
}


@Composable
fun DistributorRegisterContent(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    username: String,
    phone: String,
    rccm: String,
    taxId: String,
    websiteUrl: String,
    onUsernameChange: (String) -> Unit,
    onUsernameValidated: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onEmailValidated: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordValidated: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPhoneValidated: (String) -> Unit,
    onRccmChange: (String) -> Unit,
    onRccmValidated: (String) -> Unit,
    onTaxIdChange: (String) -> Unit,
    onTaxIdValidated: (String) -> Unit,
    onWebsiteUrlChange: (String) -> Unit,
    onWebsiteUrlValidated: (String) -> Unit,
    onSubmit: () -> Unit,
    loading: Boolean,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    errorMessage: String? = null,
    isSuccessful: Boolean = false,
) {
    val focusManager = LocalFocusManager.current
    val fieldsModifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.login_label),
            modifier = Modifier.padding(vertical = 10.dp)
        )

        CustomTextInput(
            modifier = fieldsModifier,
            value = username,
            leadingIcon = { Icon(Icons.Filled.AccountBox, contentDescription = null) },
            placeholder = { Text(text = stringResource(R.string.username_placeholder)) },
            label = { Text(text = stringResource(R.string.username_label)) },
            onValueChange = onUsernameChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Ascii
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
            singleLine = true,
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
            value = phone,
            leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
            placeholder = { Text(text = stringResource(R.string.phone_placeholder)) },
            label = { Text(text = stringResource(R.string.phone_label)) },
            onValueChange = onPhoneChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Phone
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            validator = PhoneValidator(),
            onValidatedValue = onPhoneValidated
        )

        CustomTextInput(
            modifier = fieldsModifier,
            value = password,
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
            placeholder = { Text(text = stringResource(R.string.password_placeholder)) },
            label = { Text(text = stringResource(R.string.password_label)) },
            onValueChange = onPasswordChange,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            validator = IsRequiredValidator(),
            onValidatedValue = onPasswordValidated
        )

        CustomTextInput(
            modifier = fieldsModifier,
            value = taxId,
            leadingIcon = { Icon(Icons.Filled.Info, contentDescription = null) },
            placeholder = { Text(text = stringResource(R.string.tax_id_placeholder)) },
            label = { Text(text = stringResource(R.string.tax_id_label)) },
            onValueChange = onTaxIdChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            validator = IsRequiredValidator(),
            onValidatedValue = onTaxIdValidated
        )

        CustomTextInput(
            modifier = fieldsModifier,
            value = rccm,
            leadingIcon = { Icon(Icons.Filled.Info, contentDescription = null) },
            placeholder = { Text(text = stringResource(R.string.rccm_placeholder)) },
            label = { Text(text = stringResource(R.string.rccm_label)) },
            onValueChange = onRccmChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            validator = IsRequiredValidator(),
            onValidatedValue = onRccmValidated
        )

        CustomTextInput(
            modifier = fieldsModifier,
            value = websiteUrl,
            leadingIcon = { Icon(Icons.Filled.Home, contentDescription = null) },
            placeholder = { Text(text = stringResource(R.string.website_url_placeholder)) },
            label = { Text(text = stringResource(R.string.website_url_label)) },
            onValueChange = onWebsiteUrlChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Uri
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            onValidatedValue = onWebsiteUrlValidated
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
            text = R.string.already_registered,
            textAlign = TextAlign.Center
        )

        NavigationTextButton(
            navController = navController,
            route = Routes.StartUserVerification.path,
            text = R.string.verification_code_not_sent,
            textAlign = TextAlign.Center
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
            navController.navigate(Routes.ConfirmUserVerification.path)
        }
    }
}


class DistributorRegisterViewModel(
    private val authService: AuthService = get(AuthService::class.java),
    private val userVerificationService: UserVerificationService = get(
        UserVerificationService::class.java
    ),
) : ViewModel() {
    var apiCallResult: ApiCallResult<Unit> by mutableStateOf(ApiCallResult.Inactive())
    var emailFieldText by mutableStateOf("")
    var phoneFieldText by mutableStateOf("")
    var passwordFieldText by mutableStateOf("")
    var usernameFieldText by mutableStateOf("")
    var taxIdFieldText by mutableStateOf("")
    var rccmFieldText by mutableStateOf("")
    var websiteUrlFieldText by mutableStateOf("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var password by mutableStateOf("")
    var username by mutableStateOf("")
    var taxId by mutableStateOf("")
    var rccm by mutableStateOf("")
    var websiteUrl by mutableStateOf("")

    fun register() {
        if (email.isBlank()
            || password.isBlank()
            || username.isBlank()
            || taxId.isBlank()
            || rccm.isBlank()
            || websiteUrl.isBlank()
        ) return
        apiCallResult = ApiCallResult.Loading()
        viewModelScope.launch {
            val response = authService.registerDistributor(
                DistributorRegisterRequest(
                    email,
                    taxId,
                    password,
                    phone,
                    rccm,
                    username,
                    websiteUrl.ifBlank { null }
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