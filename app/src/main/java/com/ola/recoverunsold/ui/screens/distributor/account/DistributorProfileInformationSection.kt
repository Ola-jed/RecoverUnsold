package com.ola.recoverunsold.ui.screens.distributor.account

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.ui.components.DistributorInformationList

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
    onUsernameValidated: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPhoneValidated: (String) -> Unit,
    onRccmChange: (String) -> Unit,
    onRccmValidated: (String) -> Unit,
    onTaxIdChange: (String) -> Unit,
    onTaxIdValidated: (String) -> Unit,
    onWebsiteUrlChange: (String) -> Unit,
    onWebsiteUrlValidated: (String) -> Unit
) {
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
                DistributorUpdateComponent(
                    username = username,
                    phone = phone,
                    rccm = rccm,
                    taxId = taxId,
                    websiteUrl = websiteUrl,
                    onUsernameChange = onUsernameChange,
                    onUsernameValidated = onUsernameValidated,
                    onPhoneChange = onPhoneChange,
                    onPhoneValidated = onPhoneValidated,
                    onRccmChange = onRccmChange,
                    onRccmValidated = onRccmValidated,
                    onTaxIdChange = onTaxIdChange,
                    onTaxIdValidated = onTaxIdValidated,
                    onWebsiteUrlChange = onWebsiteUrlChange,
                    onWebsiteUrlValidated = onWebsiteUrlValidated
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
                DistributorInformationList(distributor = distributor)
                Button(onClick = onEditingStart) {
                    Text(stringResource(R.string.edit_my_profile))
                }
            }
        }
    }
}