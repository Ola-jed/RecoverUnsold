package com.ola.recoverunsold.ui.components.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ShoppingCartCheckout
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
import com.ola.recoverunsold.models.Order
import com.ola.recoverunsold.models.OrderStatus
import com.ola.recoverunsold.ui.components.app.ConfirmDialog
import com.ola.recoverunsold.utils.misc.backgroundColor
import com.ola.recoverunsold.utils.misc.foregroundColor
import com.ola.recoverunsold.utils.misc.formatDate
import com.ola.recoverunsold.utils.misc.formatDateTime
import com.ola.recoverunsold.utils.misc.formatWithoutTrailingZeros
import com.ola.recoverunsold.utils.misc.internationalizedValueSingular
import com.ola.recoverunsold.utils.misc.toIcon

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DistributorOrderItem(
    modifier: Modifier = Modifier,
    order: Order,
    onOrderAccept: () -> Unit,
    onOrderReject: () -> Unit,
    onOrderComplete: () -> Unit,
    onMoreInformationRequest: () -> Unit
) {
    val offer = order.offer!!
    var showAcceptOrderDialog by remember { mutableStateOf(false) }
    var showRejectOrderDialog by remember { mutableStateOf(false) }
    var showCompleteOrderDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.padding(horizontal = 10.dp),
        elevation = 10.dp,
        onClick = onMoreInformationRequest
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OrderInformationLine(
                    text = stringResource(
                        R.string.total_amount,
                        offer.price.formatWithoutTrailingZeros()
                    ),
                    icon = Icons.Default.Payments,
                    modifier = Modifier.padding(10.dp)
                )

                Surface(color = order.status.backgroundColor()) {
                    Row(
                        modifier = Modifier.padding(horizontal = 7.5.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = order.status.toIcon(),
                            contentDescription = null,
                            tint = order.status.foregroundColor()
                        )

                        Text(
                            modifier = Modifier.padding(start = 3.dp),
                            text = order.status.internationalizedValueSingular(),
                            color = order.status.foregroundColor()
                        )
                    }
                }
            }

            OrderInformationLine(
                text = stringResource(R.string.ordered_on, order.createdAt.formatDate()),
                icon = Icons.Default.ShoppingCartCheckout,
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
            )

            OrderInformationLine(
                text = "${stringResource(id = R.string.to_be_picked_up_on)} : ${order.withdrawalDate.formatDateTime()}",
                icon = Icons.Default.EventAvailable,
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
            )

            if (order.status == OrderStatus.Pending || order.status == OrderStatus.Rejected) {
                Button(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    onClick = { showAcceptOrderDialog = true }
                ) {
                    Text(text = stringResource(id = R.string.accept_order_label))
                }
            }

            if (order.status == OrderStatus.Pending || order.status == OrderStatus.Approved) {
                Button(
                    onClick = { showRejectOrderDialog = true },
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                ) {
                    Text(
                        text = stringResource(id = R.string.reject_order_label),
                        color = MaterialTheme.colors.onError
                    )
                }
            }

            if (order.status == OrderStatus.Approved) {
                Button(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    onClick = { showCompleteOrderDialog = true }
                ) {
                    Text(text = stringResource(id = R.string.finalize_order_label))
                }
            }
        }
    }

    if (showAcceptOrderDialog) {
        ConfirmDialog(
            title = stringResource(R.string.accept_order_label),
            content = stringResource(R.string.accept_order_question),
            onDismiss = { showAcceptOrderDialog = false },
            onConfirm = {
                showAcceptOrderDialog = false
                onOrderAccept()
            }
        )
    }

    if (showRejectOrderDialog) {
        ConfirmDialog(
            title = stringResource(R.string.reject_order_label),
            content = stringResource(R.string.reject_order_question),
            onDismiss = { showRejectOrderDialog = false },
            onConfirm = {
                showRejectOrderDialog = false
                onOrderReject()
            },
            isDanger = true
        )
    }

    if (showCompleteOrderDialog) {
        ConfirmDialog(
            title = stringResource(R.string.finalize_order_label),
            content = stringResource(R.string.finalize_order_question),
            onDismiss = { showCompleteOrderDialog = false },
            onConfirm = {
                showCompleteOrderDialog = false
                onOrderComplete()
            }
        )
    }
}