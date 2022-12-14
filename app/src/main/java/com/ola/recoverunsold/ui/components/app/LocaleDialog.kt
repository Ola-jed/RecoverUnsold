package com.ola.recoverunsold.ui.components.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.utils.misc.Locale

@Composable
fun LocaleDialog(
    current: Locale,
    onChange: (Locale) -> Unit,
    onDismiss: () -> Unit
) {
    val localeOptions = listOf(
        Triple(current == Locale.En, { onChange(Locale.En) }, R.string.english),
        Triple(current == Locale.Fr, { onChange(Locale.Fr) }, R.string.french),
        Triple(current == Locale.Es, { onChange(Locale.Es) }, R.string.spanish),
        Triple(current == Locale.Auto, { onChange(Locale.Auto) }, R.string.follow_system_language),
    )

    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.select_a_language),
                style = MaterialTheme.typography.h6
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                localeOptions.forEach {
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
                            style = MaterialTheme.typography.body1.merge(),
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        dismissButton = {
            TextButton(modifier = Modifier.padding(bottom = 3.dp), onClick = onDismiss) {
                Text(
                    text = stringResource(id = R.string.cancel).uppercase(java.util.Locale.getDefault())
                )
            }
        },
        confirmButton = {}
    )
}