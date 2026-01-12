package com.starbucks.order.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.starbucks.shared.FontSize
import com.starbucks.shared.RaleWayFontFamily
import com.starbucks.shared.Surface
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.domain.CartItem
import com.starbucks.shared.domain.Order
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun OrderHistoryCard(
    order: Order
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Order #${order.id.take(8)}",
                    fontSize = FontSize.MEDIUM,
                    fontFamily = RaleWayFontFamily(),
                    color = TextPrimary
                )

                Text(
                    text = formatOrderDate(order.createdAt),
                    fontSize = FontSize.SMALL,
                    color = TextPrimary.copy(alpha = 0.6f)
                )
            }

            Divider()

            order.items.forEach { item ->
                OrderItemRow(item)
            }

            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "รวม ${order.items.size} รายการ",
                    fontSize = FontSize.SMALL,
                    color = TextPrimary
                )

                Text(
                    text = "฿${order.totalAmount.toInt()}",
                    fontSize = FontSize.MEDIUM,
                    fontFamily = RaleWayFontFamily(),
                    color = TextPrimary
                )
            }
        }
    }
}


@Composable
fun OrderItemRow(item: CartItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = buildString {
                    append(item.productCartItemDetail?.joinToString())
                    item.size?.let { append(" • $it") }
                },
                fontSize = FontSize.SMALL,
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (item.quantity > 1) {
                Text(
                    text = "จำนวน ${item.quantity}",
                    fontSize = FontSize.EXTRA_SMALL,
                    color = TextPrimary.copy(alpha = 0.6f)
                )
            }
        }
        Text(
            text = "฿${item.totalPrice.toInt()}",
            fontSize = FontSize.SMALL,
            color = TextPrimary,
            textAlign = TextAlign.End,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

fun formatOrderDate(timestamp: Long): String {
    val dateTime = Instant
        .fromEpochMilliseconds(timestamp)
        .toLocalDateTime(TimeZone.currentSystemDefault())

    val day = dateTime.dayOfMonth.toString().padStart(2, '0')
    val month = dateTime.month.name.take(3)
    val year = dateTime.year
    val hour = dateTime.hour.toString().padStart(2, '0')
    val minute = dateTime.minute.toString().padStart(2, '0')

    return "$day $month $year • $hour:$minute"
}
