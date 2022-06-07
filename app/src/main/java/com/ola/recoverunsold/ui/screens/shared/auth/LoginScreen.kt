package com.ola.recoverunsold.ui.screens.shared.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.ui.components.CustomTextInput
import com.ola.recoverunsold.utils.validation.EmailValidator
import com.ola.recoverunsold.utils.validation.IsRequiredValidator


@Composable
@Preview
fun LoginScreen() {
    var emailText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }


    LoginFields(
        email = emailText,
        password = passwordText,
        onEmailChange = { emailText = it },
        onEmailValidated = { email = it },
        onPasswordChange = { passwordText = it },
        onPasswordValidated = { password = it },
        onSubmit = { emailAdress, pwd ->

        }
    )
}

@Composable
fun LoginFields(
    email: String = "",
    password: String = "",
    onEmailChange: (String) -> Unit,
    onEmailValidated: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordValidated: (String) -> Unit,
    onSubmit: (String, String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.login_label))

        CustomTextInput(
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

        CustomTextInput(
            value = password,
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
            placeholder = { Text(text = stringResource(R.string.password_placeholder)) },
            label = { Text(text = stringResource(R.string.password_label)) },
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            validator = IsRequiredValidator(),
            onValidatedValue = onPasswordValidated
        )

        Button(onClick = { onSubmit(email, password) }) {
            Text(stringResource(R.string.login_label))
        }
    }
}