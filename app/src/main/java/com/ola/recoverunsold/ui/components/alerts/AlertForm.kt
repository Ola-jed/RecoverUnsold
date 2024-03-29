package com.ola.recoverunsold.ui.components.alerts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.AlertType
import com.ola.recoverunsold.models.DistributorLabel
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.utils.extensions.label

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertForm(
    modifier: Modifier = Modifier,
    alertType: AlertType,
    onAlertTypeChange: (AlertType) -> Unit,
    distributorLabel: DistributorLabel?,
    distributorLabels: List<DistributorLabel>,
    onDistributorLabelChange: (DistributorLabel) -> Unit,
    onSubmit: () -> Unit
) {
    var showAlertTypeDropdown by remember { mutableStateOf(false) }
    var showLabelsDropdown by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = showAlertTypeDropdown,
            onExpandedChange = { showAlertTypeDropdown = !showAlertTypeDropdown }
        ) {
            CustomTextInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = alertType.label(),
                readOnly = true,
                onValueChange = {},
                label = { Text(text = stringResource(R.string.trigger)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = showAlertTypeDropdown)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            ExposedDropdownMenu(
                expanded = showAlertTypeDropdown,
                onDismissRequest = { showAlertTypeDropdown = false }
            ) {
                AlertType.values().forEach {
                    DropdownMenuItem(onClick = {
                        onAlertTypeChange(it)
                        showAlertTypeDropdown = false
                    }, text = {
                        Text(text = it.label())
                    })
                }
            }
        }

        if (alertType == AlertType.DistributorOfferPublished) {
            ExposedDropdownMenuBox(
                expanded = showLabelsDropdown,
                onExpandedChange = { showLabelsDropdown = !showLabelsDropdown }
            ) {
                CustomTextInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = distributorLabel?.name ?: "",
                    readOnly = true,
                    onValueChange = {},
                    label = { Text(text = stringResource(R.string.distributor)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = showLabelsDropdown)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = showLabelsDropdown,
                    onDismissRequest = { showLabelsDropdown = false }
                ) {
                    distributorLabels.forEach {
                        DropdownMenuItem(onClick = {
                            onDistributorLabelChange(it)
                            showLabelsDropdown = false
                        }, text = {
                            Text(text = it.name)
                        })
                    }
                }
            }
        }

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.submit))
        }
    }
}