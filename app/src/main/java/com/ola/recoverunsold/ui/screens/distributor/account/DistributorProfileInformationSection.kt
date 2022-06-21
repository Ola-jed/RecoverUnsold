package com.ola.recoverunsold.ui.screens.distributor.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.ui.components.DistributorInformationComponent

@Composable
fun DistributorProfileInformationSection(
    distributor: Distributor,
    isEditing: Boolean,
    onEditingStart: () -> Unit,
    onEditingEnd: () -> Unit,
    onEditingCancel: () -> Unit,
    loading: Boolean
) {
    Text(
        stringResource(R.string.profile_information_label),
        modifier = Modifier
            .padding(vertical = 10.dp),
        fontSize = 17.sp
    )
    if (loading) {
        CircularProgressIndicator(color = MaterialTheme.colors.background)
    } else {
        if (isEditing) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("You are editing this data")


                // TODO : Show Distributor update component
                Row {
                    TextButton(onClick = onEditingEnd) {
                        Text(stringResource(R.string.save))
                    }
                    TextButton(onClick = onEditingCancel) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                DistributorInformationComponent(distributor = distributor)
                TextButton(onClick = onEditingStart) {
                    Text(stringResource(R.string.edit_my_profile))
                }
            }
        }
    }
}