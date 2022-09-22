package com.ola.recoverunsold.ui.components.alerts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Text
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
import com.ola.recoverunsold.utils.misc.label

@OptIn(ExperimentalMaterialApi::class)
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
                modifier = Modifier.fillMaxWidth(),
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
                    }) {
                        Text(text = it.label())
                    }
                }
            }
        }

        if (alertType == AlertType.DistributorOfferPublished) {
            ExposedDropdownMenuBox(
                expanded = showLabelsDropdown,
                onExpandedChange = { showLabelsDropdown = !showLabelsDropdown }
            ) {
                CustomTextInput(
                    modifier = Modifier.fillMaxWidth(),
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
                        }) {
                            Text(text = it.name)
                        }
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