package com.ola.recoverunsold.ui.components.offer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.DateTimePicker
import com.ola.recoverunsold.utils.misc.formatDateTime
import com.ola.recoverunsold.utils.misc.formatWithoutTrailingZeros
import com.ola.recoverunsold.utils.misc.toSecureDouble
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OfferFilterComponent(
    modifier: Modifier = Modifier,
    minPrice: Double? = null,
    maxPrice: Double? = null,
    minDate: Date? = null,
    maxDate: Date? = null,
    active: Boolean? = null,
    onMinPriceChange: (Double) -> Unit,
    onMaxPriceChange: (Double) -> Unit,
    onMinDateChange: (Date) -> Unit,
    onMaxDateChange: (Date) -> Unit,
    onActiveChange: (Boolean?) -> Unit,
    onApply: () -> Unit,
    onReset: () -> Unit
) {
    val activeLabelsMapping = mapOf(
        stringResource(id = R.string.ongoing_label) to true,
        stringResource(id = R.string.expired_label) to false,
        stringResource(id = R.string.n_a) to null
    )
    var showDropDownActiveLabels by rememberSaveable { mutableStateOf(false) }
    val currentDropdownActiveLabel by mutableStateOf(activeLabelsMapping.filter { it.value == active }.keys.first())
    var itemsAreVisible by rememberSaveable { mutableStateOf(false) }
    var showMinDatePicker by remember { mutableStateOf(false) }
    var showMaxDatePicker by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        TextButton(
            modifier = Modifier.align(Alignment.Start),
            onClick = { itemsAreVisible = !itemsAreVisible },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colors.background.copy(alpha = 0.5F),
                contentColor = MaterialTheme.colors.onBackground
            )
        ) {
            Text(stringResource(id = R.string.filters), modifier = Modifier.padding(end = 5.dp))
            if (itemsAreVisible) {
                Icon(Icons.Default.FilterListOff, contentDescription = null)
            } else {
                Icon(Icons.Default.FilterList, contentDescription = null)
            }
        }
        if (itemsAreVisible) {
            val componentsModifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)

            Column(modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)) {
                Text(
                    stringResource(id = R.string.price_label),
                    style = MaterialTheme.typography.h6
                )

                CustomTextInput(
                    modifier = componentsModifier,
                    value = minPrice.formatWithoutTrailingZeros(),
                    onValueChange = {
                        if (it.isNotBlank()) {
                            onMinPriceChange(it.toSecureDouble())
                        }
                    },
                    label = { Text(text = stringResource(R.string.minimum_price_label)) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                CustomTextInput(
                    modifier = componentsModifier,
                    value = maxPrice.formatWithoutTrailingZeros(),
                    onValueChange = {
                        if (it.isNotBlank()) {
                            onMaxPriceChange(it.toSecureDouble())
                        }
                    },
                    label = { Text(text = stringResource(R.string.maximum_price_label)) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                Text(
                    stringResource(id = R.string.date_label),
                    style = MaterialTheme.typography.h6
                )

                CustomTextInput(
                    modifier = componentsModifier.clickable { showMinDatePicker = true },
                    value = minDate?.formatDateTime() ?: "",
                    readOnly = true,
                    enabled = false,
                    onValueChange = {},
                    label = { Text(text = stringResource(R.string.minimum_date_label)) },
                    trailingIcon = { Icon(Icons.Default.EditCalendar, contentDescription = null) }
                )

                CustomTextInput(
                    modifier = componentsModifier.clickable { showMaxDatePicker = true },
                    value = maxDate?.formatDateTime() ?: "",
                    readOnly = true,
                    enabled = false,
                    onValueChange = {},
                    label = { Text(text = stringResource(R.string.maximum_date_label)) },
                    trailingIcon = { Icon(Icons.Default.EditCalendar, contentDescription = null) }
                )

                ExposedDropdownMenuBox(
                    expanded = showDropDownActiveLabels,
                    onExpandedChange = { showDropDownActiveLabels = !showDropDownActiveLabels }
                ) {
                    CustomTextInput(
                        modifier = componentsModifier,
                        value = currentDropdownActiveLabel,
                        readOnly = true,
                        onValueChange = {},
                        label = { Text(text = stringResource(R.string.status_label)) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = showDropDownActiveLabels
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = showDropDownActiveLabels,
                        onDismissRequest = { showDropDownActiveLabels = false }
                    ) {
                        activeLabelsMapping.forEach {
                            DropdownMenuItem(onClick = {
                                onActiveChange(it.value)
                                showDropDownActiveLabels = false
                            }) {
                                Text(text = it.key)
                            }
                        }
                    }
                }

                if (showMinDatePicker) {
                    DateTimePicker(
                        date = minDate,
                        onDateUpdate = onMinDateChange,
                        maxDate = maxDate
                    )
                }

                if (showMaxDatePicker) {
                    DateTimePicker(
                        date = maxDate,
                        onDateUpdate = onMaxDateChange,
                        minDate = minDate
                    )
                }

                Button(
                    modifier = componentsModifier,
                    onClick = onReset,
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                ) {
                    Text(
                        text = stringResource(id = R.string.reset),
                        color = MaterialTheme.colors.onError
                    )
                }

                Button(
                    modifier = componentsModifier,
                    onClick = onApply
                ) {
                    Text(text = stringResource(id = R.string.apply_filters))
                }
            }
        }
    }
}