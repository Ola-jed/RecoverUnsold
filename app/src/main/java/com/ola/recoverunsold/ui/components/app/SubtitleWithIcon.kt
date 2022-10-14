package com.ola.recoverunsold.ui.components.app

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Component for subtitles with an Icon
 */
@Composable
fun SubtitleWithIcon(
    modifier: Modifier = Modifier,
    text: String,
    imageVector: ImageVector
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector,
            contentDescription = null,
            modifier = Modifier.padding(end = 3.dp)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(vertical = 10.dp)
        )
    }
}