package com.ola.recoverunsold.ui.components.account

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.models.User
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.Tooltip
import com.ola.recoverunsold.utils.validation.IsRequiredValidator
import com.ola.recoverunsold.utils.validation.PhoneValidator

/**
 * Show input for updating users
 *
 * Some variations depending on the status of the User(Distributor/Customer)
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
inline fun <reified T : User> UserUpdateComponent(
    modifier: Modifier = Modifier,
    username: String,
    firstName: String = "",
    lastName: String = "",
    phone: String = "",
    rccm: String = "",
    taxId: String = "",
    websiteUrl: String = "",
    noinline onUsernameChange: (String) -> Unit,
    noinline onFirstNameChange: (String) -> Unit = {},
    noinline onLastNameChange: (String) -> Unit = {},
    noinline onPhoneChange: (String) -> Unit = {},
    noinline onRccmChange: (String) -> Unit = {},
    noinline onTaxIdChange: (String) -> Unit = {},
    noinline onWebsiteUrlChange: (String) -> Unit = {},
    noinline onValidationError: (String) -> Unit,
    noinline onValidationSuccess: () -> Unit,
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

        if (T::class == Distributor::class) {
            val showPhoneTooltip = remember { mutableStateOf(false) }

            CustomTextInput(
                modifier = fieldsModifier,
                value = phone,
                leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
                trailingIcon = {
                    Box(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 5.dp)
                            .combinedClickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(),
                                role = Role.Button,
                                onClick = { showPhoneTooltip.value = true },
                                onLongClick = { showPhoneTooltip.value = true }
                            )
                    ) {
                        Icon(imageVector = Icons.Default.Help, contentDescription = null)
                    }

                    Tooltip(showPhoneTooltip) {
                        Text(text = stringResource(id = R.string.distributor_phone_indication))
                    }
                },
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
                value = taxId,
                leadingIcon = { Icon(Icons.Filled.Numbers, contentDescription = null) },
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
                label = { Text(text = stringResource(R.string.website_url_label)) },
                onValueChange = onWebsiteUrlChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Uri
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                onValidationSuccess = onValidationSuccess,
                onValidationError = onValidationError
            )
        } else if (T::class == Customer::class) {
            CustomTextInput(
                modifier = fieldsModifier,
                value = firstName,
                leadingIcon = { Icon(Icons.Filled.People, contentDescription = null) },
                label = { Text(text = stringResource(R.string.first_name_label)) },
                onValueChange = onFirstNameChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Ascii
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                validator = IsRequiredValidator(),
                onValidationSuccess = onValidationSuccess,
                onValidationError = onValidationError
            )

            CustomTextInput(
                modifier = fieldsModifier,
                value = lastName,
                leadingIcon = { Icon(Icons.Filled.People, contentDescription = null) },
                label = { Text(text = stringResource(R.string.last_name_label)) },
                onValueChange = onLastNameChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Ascii
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                validator = IsRequiredValidator(),
                onValidationSuccess = onValidationSuccess,
                onValidationError = onValidationError
            )
        }
    }
}