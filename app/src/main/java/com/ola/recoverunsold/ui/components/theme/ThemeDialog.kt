package com.ola.recoverunsold.ui.components.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.ui.theme.ThemeMode
import java.util.Locale

@Composable
fun ThemeDialog(
    current: ThemeMode,
    onChange: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    val themeOptions = listOf(
        Triple(current == ThemeMode.Light, { onChange(ThemeMode.Light) }, R.string.light),
        Triple(current == ThemeMode.Dark, { onChange(ThemeMode.Dark) }, R.string.dark),
        Triple(
            current == ThemeMode.Auto,
            { onChange(ThemeMode.Auto) },
            R.string.follow_system_theme
        )
    )

    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.select_a_theme),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                themeOptions.forEach {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = it.first,
                                onClick = it.second
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = it.first, onClick = it.second)

                        Text(
                            text = stringResource(id = it.third),
                            style = MaterialTheme.typography.bodyLarge.merge(),
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        dismissButton = {
            TextButton(modifier = Modifier.padding(bottom = 3.dp), onClick = onDismiss) {
                Text(
                    text = stringResource(id = R.string.cancel).uppercase(Locale.getDefault())
                )
            }
        },
        confirmButton = {}
    )
}