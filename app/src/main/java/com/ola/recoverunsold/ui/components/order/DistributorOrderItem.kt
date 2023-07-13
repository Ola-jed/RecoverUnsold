package com.ola.recoverunsold.ui.components.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Order
import com.ola.recoverunsold.utils.misc.backgroundColor
import com.ola.recoverunsold.utils.misc.foregroundColor
import com.ola.recoverunsold.utils.misc.formatDate
import com.ola.recoverunsold.utils.misc.formatDateTime
import com.ola.recoverunsold.utils.misc.formatWithoutTrailingZeros
import com.ola.recoverunsold.utils.misc.internationalizedValueSingular
import com.ola.recoverunsold.utils.misc.toIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistributorOrderItem(
    modifier: Modifier = Modifier,
    order: Order,
    onMoreInformationRequest: () -> Unit
) {
    val offer = order.offer!!

    Card(
        modifier = modifier.padding(horizontal = 10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
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
                            color = order.status.foregroundColor(),
                            style = MaterialTheme.typography.bodyMedium
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

            OrderInformationLine(
                text = "${stringResource(id = R.string.customer)} : ${order.customer!!.username}",
                icon = Icons.Default.Person,
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
            )
        }
    }
}