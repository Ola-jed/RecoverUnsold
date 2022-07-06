package com.ola.recoverunsold.ui.screens.shared.auth

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.ui.components.app.AppHero
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.NavigationTextButton
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorRegisterViewModel
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.EmailValidator
import com.ola.recoverunsold.utils.validation.IsRequiredValidator
import com.ola.recoverunsold.utils.validation.PhoneValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
            email = distributorRegisterViewModel.email,
            password = distributorRegisterViewModel.password,
            username = distributorRegisterViewModel.username,
            phone = distributorRegisterViewModel.phone,
            rccm = distributorRegisterViewModel.rccm,
            taxId = distributorRegisterViewModel.taxId,
            websiteUrl = distributorRegisterViewModel.websiteUrl,
            onUsernameChange = { distributorRegisterViewModel.username = it },
            onEmailChange = { distributorRegisterViewModel.email = it },
            onPasswordChange = { distributorRegisterViewModel.password = it },
            onPhoneChange = { distributorRegisterViewModel.phone = it },
            onRccmChange = { distributorRegisterViewModel.rccm = it },
            onTaxIdChange = { distributorRegisterViewModel.taxId = it },
            onWebsiteUrlChange = { distributorRegisterViewModel.websiteUrl = it },
            onSubmit = {
                if (!distributorRegisterViewModel.formState.isValid) {
                    coroutineScope.launch {
                        snackbarHostState.show(
                            message = distributorRegisterViewModel.formState.errorMessage
                                ?: Strings.get(R.string.invalid_data)
                        )
                    }
                } else {
                    distributorRegisterViewModel.register()
                }
            },
            loading = distributorRegisterViewModel.apiCallResult.status == ApiStatus.LOADING,
            errorMessage = distributorRegisterViewModel.errorMessage(),
            navController = navController,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            modifier = Modifier.padding(padding),
            isSuccessful = distributorRegisterViewModel.apiCallResult.status == ApiStatus.SUCCESS,
            onValidationSuccess = {
                distributorRegisterViewModel.formState =
                    distributorRegisterViewModel.formState.copy(
                        isValid = true,
                        errorMessage = null
                    )
            },
            onValidationError = {
                distributorRegisterViewModel.formState =
                    distributorRegisterViewModel.formState.copy(
                        isValid = false,
                        errorMessage = it
                    )
            }
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
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onRccmChange: (String) -> Unit,
    onTaxIdChange: (String) -> Unit,
    onWebsiteUrlChange: (String) -> Unit,
    onSubmit: () -> Unit,
    loading: Boolean,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    errorMessage: String? = null,
    isSuccessful: Boolean = false,
    onValidationError: (String) -> Unit,
    onValidationSuccess: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val fieldsModifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
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
                placeholder = { Text(text = stringResource(R.string.distributor_username_placeholder)) },
                label = { Text(text = stringResource(R.string.username_label)) },
                onValueChange = onUsernameChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                validator = IsRequiredValidator(),
                onValidationSuccess = onValidationSuccess,
                onValidationError = onValidationError
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

            CustomTextInput(
                modifier = fieldsModifier,
                value = phone,
                leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
                placeholder = { Text(text = stringResource(R.string.phone_placeholder)) },
                label = { Text(text = stringResource(R.string.phone_label)) },
                onValueChange = onPhoneChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Phone
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                validator = PhoneValidator(),
                onValidationSuccess = onValidationSuccess,
                onValidationError = onValidationError
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
                onValidationSuccess = onValidationSuccess,
                onValidationError = onValidationError
            )

            CustomTextInput(
                modifier = fieldsModifier,
                value = taxId,
                leadingIcon = { Icon(Icons.Filled.Numbers, contentDescription = null) },
                placeholder = { Text(text = stringResource(R.string.tax_id_placeholder)) },
                label = { Text(text = stringResource(R.string.tax_id_label)) },
                onValueChange = onTaxIdChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                validator = IsRequiredValidator(),
                onValidationSuccess = onValidationSuccess,
                onValidationError = onValidationError
            )

            CustomTextInput(
                modifier = fieldsModifier,
                value = rccm,
                leadingIcon = { Icon(Icons.Filled.Info, contentDescription = null) },
                placeholder = { Text(text = stringResource(R.string.rccm_placeholder)) },
                label = { Text(text = stringResource(R.string.rccm_label)) },
                onValueChange = onRccmChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                validator = IsRequiredValidator(),
                onValidationSuccess = onValidationSuccess,
                onValidationError = onValidationError
            )

            CustomTextInput(
                modifier = fieldsModifier,
                value = websiteUrl,
                leadingIcon = { Icon(Icons.Filled.Home, contentDescription = null) },
                placeholder = { Text(text = stringResource(R.string.website_url_placeholder)) },
                label = { Text(text = stringResource(R.string.website_url_label)) },
                onValueChange = onWebsiteUrlChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Uri
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            )

            if (loading) {
                Button(modifier = fieldsModifier, enabled = false, onClick = {}) {
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
                        snackbarHostState.show(message = errorMessage)
                    }
                }
            }

            if (isSuccessful) {
                LaunchedEffect(snackbarHostState) {
                    coroutineScope.launch {
                        snackbarHostState.show(
                            message = Strings.get(R.string.code_sent_successfully)
                        )
                    }
                }
                navController.navigate(Routes.ConfirmUserVerification.path)
            }
        }
    }
}