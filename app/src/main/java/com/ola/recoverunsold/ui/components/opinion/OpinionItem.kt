package com.ola.recoverunsold.ui.components.opinion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Opinion
import com.ola.recoverunsold.ui.components.app.ConfirmDialog
import com.ola.recoverunsold.utils.misc.formatDate

@Composable
fun OpinionItem(
    modifier: Modifier = Modifier,
    opinion: Opinion,
    canDelete: Boolean = false,
    onDelete: () -> Unit = {}
) {
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier,
        elevation = 20.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(5.dp)) {
            Text(opinion.comment)

            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = stringResource(id = R.string.published_the, opinion.createdAt.formatDate()),
                style = MaterialTheme.typography.caption
            )

            if (canDelete) {
                IconButton(
                    modifier = Modifier.align(Alignment.End),
                    onClick = { showDeleteConfirmationDialog = true }) {
                    Icon(
                        Icons.Default.Delete,
                        tint = MaterialTheme.colors.error,
                        contentDescription = null
                    )
                }
            }
        }

        if (showDeleteConfirmationDialog) {
            ConfirmDialog(
                title = stringResource(R.string.comment_deletion),
                content = stringResource(R.string.comment_deletion_question),
                onDismiss = { showDeleteConfirmationDialog = false },
                onConfirm = {
                    showDeleteConfirmationDialog = false
                    onDelete()
                },
                isDanger = true
            )
        }
    }
}