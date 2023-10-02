package com.ola.recoverunsold.ui.components.alerts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Web
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Alert
import com.ola.recoverunsold.models.AlertType
import com.ola.recoverunsold.ui.components.app.ConfirmDialog
import com.ola.recoverunsold.ui.components.distributor.DistributorInformationLine
import com.ola.recoverunsold.utils.extensions.formatDate

@Composable
fun AlertItem(
    modifier: Modifier = Modifier,
    alert: Alert,
    onDelete: () -> Unit
) {
    var showDeleteConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    var showDistributorInformation by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = modifier,
        shadowElevation = 10.dp,
        tonalElevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(color = MaterialTheme.colorScheme.primary) {
                    Row(
                        modifier = Modifier.padding(horizontal = 7.5.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                        )

                        Text(
                            modifier = Modifier.padding(start = 3.dp),
                            text = stringResource(
                                id = when (alert.alertType) {
                                    AlertType.AnyOfferPublished -> R.string.all_new_publications
                                    AlertType.DistributorOfferPublished -> R.string.publications_of_given_distributor
                                }
                            )
                        )
                    }
                }
            }

            if (alert.distributorInformation != null) {
                IconButton(onClick = { showDistributorInformation = !showDistributorInformation }) {
                    Icon(
                        imageVector = if (showDistributorInformation) {
                            Icons.Default.ExpandLess
                        } else {
                            Icons.Default.ExpandMore
                        },
                        contentDescription = null
                    )
                }

                if (showDistributorInformation) {
                    DistributorInformationLine(
                        modifier = Modifier.padding(vertical = 5.dp),
                        leadingIcon = {
                            Icon(
                                Icons.Filled.AccountBox,
                                contentDescription = null
                            )
                        },
                        data = alert.distributorInformation.username
                    )

                    DistributorInformationLine(
                        modifier = Modifier.padding(vertical = 5.dp),
                        leadingIcon = {
                            Icon(Icons.Filled.Email, contentDescription = null)
                        },
                        data = alert.distributorInformation.email
                    )

                    DistributorInformationLine(
                        modifier = Modifier.padding(vertical = 5.dp),
                        leadingIcon = {
                            Icon(Icons.Filled.Phone, contentDescription = null)
                        },
                        data = alert.distributorInformation.phone
                    )

                    if (alert.distributorInformation.websiteUrl != null) {
                        DistributorInformationLine(
                            modifier = Modifier.padding(vertical = 5.dp),
                            leadingIcon = {
                                Icon(Icons.Filled.Web, contentDescription = null)
                            },
                            label = stringResource(id = R.string.website_url_label),
                            data = alert.distributorInformation.websiteUrl
                        )
                    }

                    DistributorInformationLine(
                        modifier = Modifier.padding(vertical = 5.dp),
                        leadingIcon = {
                            Icon(Icons.Filled.CalendarToday, contentDescription = null)
                        },
                        label = stringResource(id = R.string.member_since_label),
                        data = alert.distributorInformation.createdAt.formatDate()
                    )
                }
            }

            IconButton(
                modifier = Modifier.align(Alignment.End),
                onClick = { showDeleteConfirmationDialog = true }) {
                Icon(
                    Icons.Default.Delete,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = null
                )
            }
        }
    }

    if (showDeleteConfirmationDialog) {
        ConfirmDialog(
            title = stringResource(R.string.delete_alert_label),
            content = stringResource(R.string.delete_alert_question),
            onDismiss = { showDeleteConfirmationDialog = false },
            onConfirm = {
                showDeleteConfirmationDialog = false
                onDelete()
            },
            isDanger = true
        )
    }
}