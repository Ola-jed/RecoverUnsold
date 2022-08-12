package com.ola.recoverunsold.ui.components.order

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.OrderStatus
import com.ola.recoverunsold.utils.misc.internationalizedValue
import com.ola.recoverunsold.utils.misc.toIcon

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderFilterComponent(
    modifier: Modifier = Modifier,
    orderStatus: OrderStatus?,
    onOrderStatusChange: (OrderStatus?) -> Unit
) {
    val onBgModified = MaterialTheme.colors.onBackground.copy(alpha = 0.3F)

    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {
        Chip(
            modifier = Modifier.padding(end = 5.dp),
            onClick = { onOrderStatusChange(null) },
            colors = ChipDefaults.chipColors(
                backgroundColor = if (orderStatus == null) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.background
                }
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = stringResource(id = R.string.all),
                    tint = if (orderStatus == null) {
                        MaterialTheme.colors.onPrimary
                    } else {
                        MaterialTheme.colors.onBackground
                    }
                )
            }
        ) {
            Text(
                text = stringResource(id = R.string.all),
                color = if (orderStatus == null) MaterialTheme.colors.onPrimary else onBgModified
            )
        }

        OrderStatus.values().forEach {
            val bgColor = if (it == orderStatus) {
                MaterialTheme.colors.onPrimary
            } else {
                MaterialTheme.colors.onBackground
            }

            Chip(
                modifier = Modifier.padding(end = 5.dp),
                onClick = { onOrderStatusChange(it) },
                colors = ChipDefaults.chipColors(
                    backgroundColor = if (it == orderStatus) {
                        MaterialTheme.colors.onPrimary
                    } else {
                        onBgModified
                    }
                ),
                leadingIcon = {
                    Icon(
                        imageVector = it.toIcon(),
                        contentDescription = it.name,
                        tint = bgColor
                    )
                }
            ) {
                Text(text = it.internationalizedValue(), color = bgColor)
            }
        }
    }
}