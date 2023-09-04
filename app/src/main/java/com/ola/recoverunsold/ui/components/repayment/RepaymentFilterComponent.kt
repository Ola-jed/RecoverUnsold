package com.ola.recoverunsold.ui.components.repayment

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Visibility
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepaymentFilterComponent(
    modifier: Modifier = Modifier,
    done: Boolean?,
    onDoneChange: (Boolean?) -> Unit
) {
    val repaymentStatuses = listOf(
        Triple(null, stringResource(R.string.all), Icons.Filled.Visibility),
        Triple(true, stringResource(R.string.repayments_done), Icons.Filled.DoneAll),
        Triple(false, stringResource(R.string.repayments_pending), Icons.Filled.Pending),
    )

    val onBgModified = Color.Black.copy(alpha = 0.1F)

    Row(modifier = modifier) {
        repaymentStatuses.forEach {
            val bgColor = if (it.first == done) {
                MaterialTheme.colorScheme.primary
            } else {
                onBgModified
            }

            val fgColor = if (it.first == done) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onBackground
            }

            FilterChip(
                selected = it.first == done,
                modifier = Modifier.padding(end = 5.dp),
                onClick = { onDoneChange(it.first) },
                colors = FilterChipDefaults.filterChipColors(containerColor = bgColor),
                leadingIcon = {
                    Icon(imageVector = it.third, contentDescription = it.second, tint = fgColor)
                },
                label = { Text(text = it.second, color = fgColor) }
            )
        }
    }
}
