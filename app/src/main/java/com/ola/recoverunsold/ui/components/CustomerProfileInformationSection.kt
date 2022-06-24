package com.ola.recoverunsold.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
    onUsernameValidated: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onFirstNameValidated: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onLastNameValidated: (String) -> Unit,
    onDelete: () -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Text(
        stringResource(R.string.profile_information_label),
        modifier = Modifier
            .padding(vertical = 10.dp),
        fontSize = 17.sp
    )
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
                UserUpdateComponent<Customer>(
                    username = username,
                    firstName = firstName,
                    lastName = lastName,
                    onUsernameChange = onUsernameChange,
                    onUsernameValidated = onUsernameValidated,
                    onFirstNameChange = onFirstNameChange,
                    onFirstNameValidated = onFirstNameValidated,
                    onLastNameChange = onLastNameChange,
                    onLastNameValidated = onLastNameValidated
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = onEditingEnd,
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                    ) {
                        Text(stringResource(R.string.save), color = MaterialTheme.colors.onPrimary)
                    }
                    Button(
                        onClick = onEditingCancel,
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                    ) {
                        Text(stringResource(R.string.cancel), color = MaterialTheme.colors.onError)
                    }
                }
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                UserInformationList(user = customer)
                Button(onClick = onEditingStart) {
                    Text(stringResource(R.string.edit_my_profile))
                }
                Button(
                    onClick = {
                        showDialog = true
                        onDelete()
                    },
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