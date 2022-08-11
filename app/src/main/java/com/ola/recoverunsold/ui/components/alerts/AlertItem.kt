package com.ola.recoverunsold.ui.components.alerts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Alert
import com.ola.recoverunsold.models.AlertType
import com.ola.recoverunsold.ui.components.app.ConfirmDialog
import com.ola.recoverunsold.ui.components.distributor.DistributorInformationLine
import com.ola.recoverunsold.utils.misc.formatDate

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
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            Icon(
                imageVector = Icons.Default.NotificationsActive,
                contentDescription = null,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            Text(text = "${stringResource(id = R.string.trigger)} : ")

            Text(
                text = stringResource(
                    id = when (alert.alertType) {
                        AlertType.AnyOfferPublished -> R.string.all_new_publications
                        AlertType.DistributorOfferPublished -> R.string.publications_of_given_distributor
                    }
                ),
                fontWeight = FontWeight.SemiBold
            )

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
                    tint = MaterialTheme.colors.error,
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