package com.ola.recoverunsold.ui.components.offer

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.DateTimePicker
import com.ola.recoverunsold.utils.misc.formatDateTime
import java.util.Date

// TODO : finish

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
    onActiveChange: (Boolean) -> Unit,
    onApply: () -> Unit,
    onReset: () -> Unit
) {
    var itemsAreVisible by rememberSaveable { mutableStateOf(false) }
    var showMinDatePicker by remember { mutableStateOf(false) }

    Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
        TextButton(onClick = { itemsAreVisible = !itemsAreVisible }) {
            Text(stringResource(id = R.string.filters), modifier = Modifier.padding(end = 5.dp))
            if (itemsAreVisible) {
                Icon(Icons.Default.FilterListOff, contentDescription = null)
            } else {
                Icon(Icons.Default.FilterList, contentDescription = null)
            }
        }
        if (itemsAreVisible) {
            Surface(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)) {
                    CustomTextInput(
                        value = minPrice?.toInt()?.toString() ?: "",
                        onValueChange = {
                            if (it.isNotBlank()) {
                                onMinPriceChange(it.toDouble())
                            }
                        },
                        label = { Text(text = stringResource(R.string.minimum_price_label)) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                    )

                    CustomTextInput(
                        value = maxPrice?.toInt()?.toString() ?: "",
                        onValueChange = {
                            if (it.isNotBlank()) {
                                onMaxPriceChange(it.toDouble())
                            }
                        },
                        label = { Text(text = stringResource(R.string.maximum_price_label)) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                    )

                    CustomTextInput(
                        value = minDate?.formatDateTime() ?: "",
                        readOnly = true,
                        onValueChange = {},
                        label = { Text(text = "Minimum date") },
                        trailingIcon = {
                            IconButton(onClick = { showMinDatePicker = true }) {
                                Icon(Icons.Default.EditCalendar, contentDescription = null)
                            }
                        }
                    )

                    if (showMinDatePicker) {
                        DateTimePicker(
                            date = minDate,
                            onDateUpdate = onMinDateChange
                        )
                    }

                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(0.8F),
                        onClick = { onReset() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                    ) {
                        Text(
                            text = stringResource(id = R.string.reset),
                            color = MaterialTheme.colors.onError
                        )
                    }

                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(0.8F),
                        onClick = { onApply() }
                    ) {
                        Text(text = stringResource(id = R.string.apply_filters))
                    }
                }
            }
        }
    }
}