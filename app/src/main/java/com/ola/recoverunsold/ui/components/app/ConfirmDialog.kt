package com.ola.recoverunsold.ui.components.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R

@Composable
fun ConfirmDialog(
    title: String? = null,
    content: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isDanger: Boolean = false
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { onDismiss() },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                if (!title.isNullOrEmpty()) {
                    Text(
                        title,
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text(content)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = if (!isDanger) {
                    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                } else {
                    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                }
            ) {
                Text(
                    stringResource(id = R.string.no), color = if (!isDanger) {
                        MaterialTheme.colorScheme.onError
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm, colors = if (isDanger) {
                    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                } else {
                    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                }
            ) {
                Text(
                    stringResource(id = R.string.yes), color = if (isDanger) {
                        MaterialTheme.colorScheme.onError
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    }
                )
            }
        }
    )
}