package com.ola.recoverunsold.ui.components.account

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.ui.components.app.ConfirmDialog

@Composable
fun CustomerProfileInformationSection(
    customer: Customer,
    username: String,
    firstName: String,
    lastName: String,
    isEditing: Boolean,
    onEditingStart: () -> Unit,
    onEditingEnd: () -> Unit,
    onEditingCancel: () -> Unit,
    loading: Boolean,
    onUsernameChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onDelete: () -> Unit,
    onValidationError: (String) -> Unit,
    onValidationSuccess: () -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (loading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary, modifier = Modifier.align(
                    Alignment.Center
                )
            )
        }
    } else {
        if (isEditing) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    stringResource(R.string.profile_information_label),
                    modifier = Modifier
                        .padding(vertical = 10.dp),
                    fontSize = 17.sp
                )

                UserUpdateComponent<Customer>(
                    username = username,
                    firstName = firstName,
                    lastName = lastName,
                    onUsernameChange = onUsernameChange,
                    onFirstNameChange = onFirstNameChange,
                    onLastNameChange = onLastNameChange,
                    onValidationSuccess = onValidationSuccess,
                    onValidationError = onValidationError
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = onEditingEnd,
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                    ) {
                        Text(
                            stringResource(R.string.save),
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                    Button(
                        onClick = onEditingCancel,
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                    ) {
                        Text(
                            stringResource(R.string.cancel),
                            color = MaterialTheme.colors.onError
                        )
                    }
                }
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    stringResource(R.string.profile_information_label),
                    modifier = Modifier
                        .padding(vertical = 10.dp),
                    fontSize = 17.sp
                )

                UserInformationList(user = customer)

                val buttonsModifier = Modifier.fillMaxWidth(fraction = 0.75F)

                Button(modifier = buttonsModifier, onClick = onEditingStart) {
                    Text(stringResource(R.string.edit_my_profile))
                }

                Button(
                    modifier = buttonsModifier,
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                ) {
                    Text(
                        stringResource(R.string.delete_account),
                        color = MaterialTheme.colors.onError
                    )
                }

                if (showDialog) {
                    ConfirmDialog(
                        title = stringResource(id = R.string.delete_account),
                        content = stringResource(id = R.string.delete_account_message),
                        onDismiss = { showDialog = false },
                        onConfirm = {
                            showDialog = false
                            onDelete()
                        },
                        isDanger = true
                    )
                }
            }
        }
    }
}