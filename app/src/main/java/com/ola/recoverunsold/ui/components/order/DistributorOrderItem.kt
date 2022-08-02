package com.ola.recoverunsold.ui.components.order

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Order
import com.ola.recoverunsold.models.OrderStatus
import com.ola.recoverunsold.ui.components.app.ConfirmDialog
import com.ola.recoverunsold.utils.misc.formatDate
import com.ola.recoverunsold.utils.misc.formatDateTime
import com.ola.recoverunsold.utils.misc.formatWithoutTrailingZeros
import com.ola.recoverunsold.utils.misc.internationalizedValue
import com.ola.recoverunsold.utils.misc.toIcon

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
    val customer = order.customer!!
    var isExpanded by remember { mutableStateOf(false) }
    var showAcceptOrderDialog by remember { mutableStateOf(false) }
    var showRejectOrderDialog by remember { mutableStateOf(false) }
    var showCompleteOrderDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .clickable { onMoreInformationRequest() },
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = order.status.toIcon(), contentDescription = null)
                Text(text = order.status.internationalizedValue())
            }

            Text(
                text = stringResource(
                    R.string.total_amount,
                    offer.price.formatWithoutTrailingZeros()
                ),
                modifier = Modifier.padding(top = 10.dp),
                fontWeight = FontWeight.Bold,
                fontSize =  17.sp
            )

            Text(
                text = stringResource(R.string.ordered_on, order.createdAt.formatDateTime()),
                modifier = Modifier.padding(top = 10.dp)
            )

            Text(
                text = "${stringResource(id = R.string.to_be_picked_up_on)} : ${order.withdrawalDate.formatDateTime()}",
                modifier = Modifier.padding(top = 10.dp),
                fontWeight = FontWeight.SemiBold
            )

            IconButton(
                modifier = Modifier.align(Alignment.End),
                onClick = { isExpanded = !isExpanded }
            ) {
                Icon(
                    imageVector = if (isExpanded) {
                        Icons.Default.ExpandLess
                    } else {
                        Icons.Default.ExpandMore
                    },
                    contentDescription = null
                )
            }

            if (isExpanded) {
                Text(
                    text = stringResource(id = R.string.customer),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(text = customer.username)
                Text(text = customer.email)
                Text(text = "${stringResource(id = R.string.member_since_label)} ${customer.createdAt.formatDate()}")
            }

            if (order.status == OrderStatus.Pending || order.status == OrderStatus.Rejected) {
                Button(
                    onClick = { showAcceptOrderDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.accept_order_label))
                }
            }

            if (order.status == OrderStatus.Pending || order.status == OrderStatus.Approved) {
                Button(
                    onClick = { showRejectOrderDialog = true },
                    modifier = Modifier.fillMaxWidth(),
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
                    onClick = { showCompleteOrderDialog = true },
                    modifier = Modifier.fillMaxWidth()
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