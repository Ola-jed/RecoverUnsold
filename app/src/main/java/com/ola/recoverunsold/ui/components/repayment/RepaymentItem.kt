package com.ola.recoverunsold.ui.components.repayment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Repayment
import com.ola.recoverunsold.ui.components.app.InformationLine
import com.ola.recoverunsold.ui.theme.onSuccess
import com.ola.recoverunsold.ui.theme.onWarning
import com.ola.recoverunsold.ui.theme.success
import com.ola.recoverunsold.ui.theme.warning

@Composable
fun RepaymentItem(
    modifier: Modifier = Modifier,
    repayment: Repayment,
    onTap: () -> Unit = {}
) {
    val chipBgColor =
        if (repayment.done) MaterialTheme.colorScheme.success else MaterialTheme.colorScheme.warning
    val chipFgColor =
        if (repayment.done) MaterialTheme.colorScheme.onSuccess else MaterialTheme.colorScheme.onWarning
    val chipIcon = if (repayment.done) Icons.Filled.Done else Icons.Filled.Pending
    val chipText = if (repayment.done) R.string.repayment_done else R.string.repayment_pending

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Surface(modifier = Modifier.align(Alignment.End), color = chipBgColor) {
                Row(
                    modifier = Modifier.padding(horizontal = 7.5.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = chipIcon,
                        contentDescription = null,
                        tint = chipFgColor
                    )

                    Text(
                        modifier = Modifier.padding(start = 3.dp),
                        text = stringResource(id = chipText),
                        color = chipFgColor
                    )
                }
            }

            if (repayment.transactionId != null) {
                InformationLine(
                    text = stringResource(R.string.transaction_id) + " : " + repayment.transactionId,
                    icon = Icons.Default.ReceiptLong,
                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
                )
            }

            InformationLine(
                text = stringResource(R.string.total_amount, repayment.order!!.offer!!.price),
                icon = Icons.Default.PriceChange,
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
            )

            TextButton(onClick = onTap) {
                Text(stringResource(id = R.string.order_details))
            }
        }
    }
}