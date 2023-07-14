package com.ola.recoverunsold.ui.components.order

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.OrderStatus
import com.ola.recoverunsold.utils.extensions.internationalizedValue
import com.ola.recoverunsold.utils.extensions.toIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderFilterComponent(
    modifier: Modifier = Modifier,
    orderStatus: OrderStatus?,
    onOrderStatusChange: (OrderStatus?) -> Unit
) {
    val onBgModified = Color.Black.copy(alpha = 0.1F)

    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {
        FilterChip(
            selected = orderStatus == null,
            modifier = Modifier.padding(end = 5.dp),
            onClick = { onOrderStatusChange(null) },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = if (orderStatus == null) {
                    MaterialTheme.colorScheme.primary
                } else {
                    onBgModified
                }
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = stringResource(id = R.string.all),
                    tint = if (orderStatus == null) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    }
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.all),
                    color = if (orderStatus == null) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    }
                )
            }
        )

        OrderStatus.values().forEach {
            val bgColor = if (it == orderStatus) {
                MaterialTheme.colorScheme.primary
            } else {
                onBgModified
            }

            val fgColor = if (it == orderStatus) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onBackground
            }

            FilterChip(
                selected = it == orderStatus,
                modifier = Modifier.padding(end = 5.dp),
                onClick = { onOrderStatusChange(it) },
                colors = FilterChipDefaults.filterChipColors(containerColor = bgColor),
                leadingIcon = {
                    Icon(
                        imageVector = it.toIcon(),
                        contentDescription = it.name,
                        tint = fgColor
                    )
                },
                label = { Text(text = it.internationalizedValue(), color = fgColor) }
            )
        }
    }
}