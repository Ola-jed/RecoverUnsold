package com.ola.recoverunsold.ui.components.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Product
import com.ola.recoverunsold.ui.components.app.ConfirmDialog
import com.ola.recoverunsold.ui.components.app.ImageSlider
import com.ola.recoverunsold.utils.misc.formatDate

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: Product,
    isEditable: Boolean = false,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    var showDeleteConfirmationDialog by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = modifier,
        tonalElevation = 10.dp,
        shadowElevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Text(
                product.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )

            if (product.images.isNotEmpty()) {
                val height = (LocalConfiguration.current.screenHeightDp * 0.1).dp
                ImageSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height),
                    imageUris = product.images.map { it.url }
                )
            }

            Text(product.description)

            Text(
                stringResource(R.string.published_the, product.createdAt.formatDate()),
                modifier = Modifier.padding(top = 5.dp)
            )

            if (isEditable) {
                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                    }
                    IconButton(onClick = { showDeleteConfirmationDialog = true }) {
                        Icon(
                            Icons.Default.Delete,
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = null
                        )
                    }
                }
            }
        }

        if (showDeleteConfirmationDialog) {
            ConfirmDialog(
                title = stringResource(R.string.delete_product_label),
                content = stringResource(R.string.delete_product_question),
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