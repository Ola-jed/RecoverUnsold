package com.ola.recoverunsold.ui.components.distributor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DistributorInformationLine(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    label: String? = null,
    data: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(horizontalArrangement = Arrangement.Start) {
            if (leadingIcon != null) {
                leadingIcon()
            }

            if (label != null) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "$label : "
                )
            }
            else {
                Spacer(modifier = Modifier.padding(start = 10.dp))
            }
        }

        Text(data)
    }
}