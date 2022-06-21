package com.ola.recoverunsold.ui.screens.distributor.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.ui.components.CustomTextInput
import com.ola.recoverunsold.utils.validation.EmailValidator
import com.ola.recoverunsold.utils.validation.IsRequiredValidator
import com.ola.recoverunsold.utils.validation.PhoneValidator

@Composable
fun DistributorUpdateComponent(
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
) {
    val focusManager = LocalFocusManager.current
    val fieldsModifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)

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
            onValidatedValue = onPhoneValidated
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

        CustomTextInput(
            modifier = fieldsModifier,
            value = taxId,
            leadingIcon = { Icon(Icons.Filled.Info, contentDescription = null) },
            placeholder = { Text(text = stringResource(R.string.tax_id_placeholder)) },
            label = { Text(text = stringResource(R.string.tax_id_label)) },
            onValueChange = onTaxIdChange,
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
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Uri
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            onValidatedValue = onWebsiteUrlValidated
        )
    }
}