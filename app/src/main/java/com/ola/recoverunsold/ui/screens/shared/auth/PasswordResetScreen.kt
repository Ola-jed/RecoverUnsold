package com.ola.recoverunsold.ui.screens.shared.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.ui.components.app.AppHero
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.NavigationTextButton
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.PasswordResetViewModel
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.IsRequiredValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun PasswordResetScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    passwordResetViewModel: PasswordResetViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()


    Scaffold { padding ->
        PasswordResetContent(
            modifier = Modifier.padding(padding),
            token = passwordResetViewModel.token,
            onTokenChange = { passwordResetViewModel.token = it },
            password = passwordResetViewModel.password,
            onPasswordChange = { passwordResetViewModel.password = it },
            onSubmit = {
                if (!passwordResetViewModel.formState.isValid) {
                    coroutineScope.launch {
                        snackbarHostState.show(
                            message = passwordResetViewModel.formState.errorMessage
                                ?: Strings.get(R.string.invalid_data)
                        )
                    }
                } else {
                    passwordResetViewModel.submit()
                }
            },
            loading = passwordResetViewModel.passwordResetApiCallResult.status == ApiStatus.LOADING,
            errorMessage = passwordResetViewModel.errorMessage(),
            navController = navController,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            isSuccessful = passwordResetViewModel.passwordResetApiCallResult.status == ApiStatus.SUCCESS,
            onValidationSuccess = {
                passwordResetViewModel.formState = passwordResetViewModel.formState.copy(
                    isValid = true,
                    errorMessage = null
                )
            },
            onValidationError = {
                passwordResetViewModel.formState = passwordResetViewModel.formState.copy(
                    isValid = false,
                    errorMessage = it
                )
            }
        )
    }
}

@Composable
fun PasswordResetContent(
    modifier: Modifier = Modifier,
    token: String,
    onTokenChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
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
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                validator = IsRequiredValidator(),
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

            Button(modifier = fieldsModifier, onClick = onSubmit, enabled = !loading) {
                if (loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.background)
                } else {
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
                navController.navigate(Routes.PasswordResetSuccess.path)
            }
        }
    }
}