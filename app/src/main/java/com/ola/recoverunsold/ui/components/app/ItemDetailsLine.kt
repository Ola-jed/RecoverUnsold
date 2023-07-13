package com.ola.recoverunsold.ui.components.app

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ItemDetailsLine(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String
) {
    Row(modifier = modifier) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.padding(end = 10.dp)
        )
        Text(text = text)
    }
}