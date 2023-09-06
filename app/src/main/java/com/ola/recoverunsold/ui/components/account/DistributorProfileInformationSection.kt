package com.ola.recoverunsold.ui.components.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.ui.components.app.ConfirmDialog

@Composable
fun DistributorProfileInformationSection(
    distributor: Distributor,
    username: String,
    phone: String,
    rccm: String,
    taxId: String,
    websiteUrl: String,
    isEditing: Boolean,
    onEditingStart: () -> Unit,
    onEditingEnd: () -> Unit,
    onEditingCancel: () -> Unit,
    loading: Boolean,
    onUsernameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onRccmChange: (String) -> Unit,
    onTaxIdChange: (String) -> Unit,
    onWebsiteUrlChange: (String) -> Unit,
    onDelete: () -> Unit,
    onValidationError: (String) -> Unit,
    onValidationSuccess: () -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Text(
        stringResource(R.string.profile_information_label),
        modifier = Modifier.padding(vertical = 10.dp),
        fontSize = 17.sp
    )
    if (loading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        if (isEditing) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                UserUpdateComponent<Distributor>(
                    username = username,
                    phone = phone,
                    rccm = rccm,
                    taxId = taxId,
                    websiteUrl = websiteUrl,
                    onUsernameChange = onUsernameChange,
                    onPhoneChange = onPhoneChange,
                    onRccmChange = onRccmChange,
                    onTaxIdChange = onTaxIdChange,
                    onWebsiteUrlChange = onWebsiteUrlChange,
                    onValidationSuccess = onValidationSuccess,
                    onValidationError = onValidationError
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp),
                    onClick = onEditingEnd,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        stringResource(R.string.save),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp),
                    onClick = onEditingCancel,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(
                        stringResource(R.string.cancel),
                        color = MaterialTheme.colorScheme.onError
                    )
                }
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                UserInformationList(user = distributor)
                val buttonsModifier = Modifier.fillMaxWidth(fraction = 0.75F)

                Button(modifier = buttonsModifier, onClick = onEditingStart) {
                    Text(stringResource(R.string.edit_my_profile))
                }

                Button(
                    modifier = buttonsModifier,
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(
                        stringResource(R.string.delete_account),
                        color = MaterialTheme.colorScheme.onError
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

                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}