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
import androidx.compose.material.icons.filled.Menu
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
import com.ola.recoverunsold.ui.screens.viewmodels.UserVerificationViewModel
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.IsRequiredValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun UserVerificationScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    userVerificationViewModel: UserVerificationViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    ) { padding ->
        UserVerificationContent(
            modifier = Modifier.padding(padding),
            token = userVerificationViewModel.token,
            onTokenChange = { userVerificationViewModel.token = it },
            onSubmit = {
                if (!userVerificationViewModel.formState.isValid) {
                    coroutineScope.launch {
                        snackbarHostState.show(
                            message = userVerificationViewModel.formState.errorMessage
                                ?: Strings.get(R.string.invalid_data)
                        )
                    }
                } else {
                    userVerificationViewModel.submit()
                }
            },
            loading = userVerificationViewModel.apiCallResult.status == ApiStatus.LOADING,
            navController = navController,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            errorMessage = userVerificationViewModel.errorMessage(),
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
fun UserVerificationContent(
    modifier: Modifier = Modifier,
    token: String,
    onTokenChange: (String) -> Unit,
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
            text = stringResource(R.string.verification_of_your_account)
        )

        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.user_verification_confirm_instruction),
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

            Button(onClick = onSubmit, enabled = !loading, modifier = fieldsModifier) {
                if (loading) {
                    CircularProgressIndicator(color = MaterialTheme.colors.background)
                } else {
                    Text(
                        stringResource(R.string.verify_account_action),
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
                route = Routes.StartUserVerification.path,
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
                navController.navigate(Routes.UserVerificationSuccess.path)
            }
        }
    }
}