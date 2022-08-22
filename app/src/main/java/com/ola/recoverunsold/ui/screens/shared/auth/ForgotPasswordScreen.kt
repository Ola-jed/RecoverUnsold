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
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.ui.components.app.AppHero
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.NavigationTextButton
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.ForgotPasswordViewModel
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.EmailValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    ) { padding ->
        ForgotPasswordContent(
            email = forgotPasswordViewModel.email,
            onEmailChange = { forgotPasswordViewModel.email = it },
            onSubmit = {
                if (!forgotPasswordViewModel.formState.isValid) {
                    coroutineScope.launch {
                        snackbarHostState.show(
                            message = forgotPasswordViewModel.formState.errorMessage
                                ?: Strings.get(R.string.invalid_data)
                        )
                    }
                } else {
                    forgotPasswordViewModel.submit()
                }
            },
            loading = forgotPasswordViewModel.apiCallResult.status == ApiStatus.LOADING,
            errorMessage = forgotPasswordViewModel.errorMessage(),
            navController = navController,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            modifier = Modifier.padding(padding),
            isSuccessful = forgotPasswordViewModel.apiCallResult.status == ApiStatus.SUCCESS,
            onValidationSuccess = {
                forgotPasswordViewModel.formState = forgotPasswordViewModel.formState.copy(
                    isValid = true,
                    errorMessage = null
                )
            },
            onValidationError = {
                forgotPasswordViewModel.formState = forgotPasswordViewModel.formState.copy(
                    isValid = false,
                    errorMessage = it
                )
            }
        )
    }
}

@Composable
fun ForgotPasswordContent(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
    loading: Boolean,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    errorMessage: String? = null,
    isSuccessful: Boolean,
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHero(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 60.dp),
            text = stringResource(R.string.reset_password)
        )

        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                onValidationError = onValidationError,
                onValidationSuccess = onValidationSuccess
            )


            Button(onClick = onSubmit, modifier = fieldsModifier, enabled = !loading) {
                if (loading) {
                    CircularProgressIndicator(color = MaterialTheme.colors.background)
                } else {
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
        }

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
        }
    }
}
