package com.ola.recoverunsold.ui.components.distributor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun DistributorInformationLine(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    label: String? = null,
    text: @Composable (() -> Unit)? = null,
    data: String? = null,
    textStyle: TextStyle? = null
) {
    require((data == null) xor (text == null)) { "Exactly one element between Data and Text must be passed" }

    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        Row(horizontalArrangement = Arrangement.Start) {
            if (leadingIcon != null) {
                leadingIcon()
            }

            if (label != null) {
                Text(modifier = Modifier.padding(start = 10.dp), text = "$label : ")
            } else {
                Spacer(modifier = Modifier.padding(start = 10.dp))
            }
        }

        if (data != null) {
            if (textStyle == null) {
                Text(data)
            } else {
                Text(data, style = textStyle)
            }
        }

        if (text != null) {
            text()
        }
    }
}