package com.ola.recoverunsold.ui.components.alerts

import androidx.compose.foundation.layout.Column
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
    var showDistributorsLabelDropdown by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = showAlertTypeDropdown,
            onExpandedChange = {
                showAlertTypeDropdown = !showAlertTypeDropdown
            }
        ) {
            CustomTextInput(
                modifier = Modifier,
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
                expanded = showDistributorsLabelDropdown,
                onExpandedChange = {
                    showDistributorsLabelDropdown = !showDistributorsLabelDropdown
                }
            ) {
                CustomTextInput(
                    modifier = Modifier,
                    value = distributorLabel?.name ?: "",
                    readOnly = true,
                    onValueChange = {},
                    label = { Text(text = stringResource(R.string.distributor)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = showDistributorsLabelDropdown
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = showDistributorsLabelDropdown,
                    onDismissRequest = { showDistributorsLabelDropdown = false }
                ) {
                    distributorLabels.forEach {
                        DropdownMenuItem(onClick = {
                            onDistributorLabelChange(it)
                            showDistributorsLabelDropdown = false
                        }) {
                            Text(text = it.name)
                        }
                    }
                }
            }
        }

        Button(
            onClick = onSubmit,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(id = R.string.submit))
        }
    }
}