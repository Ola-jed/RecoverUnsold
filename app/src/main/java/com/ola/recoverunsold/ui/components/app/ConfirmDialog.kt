package com.ola.recoverunsold.ui.components.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                Text(content)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = if (!isDanger) {
                    ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                } else {
                    ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                }
            ) {
                Text(
                    stringResource(id = R.string.no), color = if (!isDanger) {
                        MaterialTheme.colors.onError
                    } else {
                        MaterialTheme.colors.onPrimary
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm, colors = if (isDanger) {
                    ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                } else {
                    ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                }
            ) {
                Text(
                    stringResource(id = R.string.yes), color = if (isDanger) {
                        MaterialTheme.colors.onError
                    } else {
                        MaterialTheme.colors.onPrimary
                    }
                )
            }
        }
    )
}