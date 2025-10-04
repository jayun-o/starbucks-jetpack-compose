package com.starbucks.shared.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.starbucks.shared.FontSize
import com.starbucks.shared.Resources
import com.starbucks.shared.SurfaceBrand
import com.starbucks.shared.SurfaceLighter
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.TextWhite
import com.starbucks.shared.domain.QuatityCounterSize
import org.jetbrains.compose.resources.painterResource

@Composable
fun QuantityCounter(
    modifier: Modifier = Modifier,
    size: QuatityCounterSize,
    value: String,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(size.spacing),
    ) {
        // Minus Button
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(SurfaceBrand)
                .clickable { onMinusClick() }
                .padding(size.padding),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Resources.Icon.Minus),
                contentDescription = "Minus Icon",
                tint = TextWhite
            )
        }

        // Value Box (white background)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(TextWhite)
                .padding(horizontal = 12.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontSize = FontSize.MEDIUM,
                fontWeight = FontWeight.Medium,
                color = SurfaceBrand // สีเขียว
            )
        }

        // Plus Button
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(SurfaceBrand)
                .clickable { onPlusClick() }
                .padding(size.padding),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Resources.Icon.Plus),
                contentDescription = "Plus Icon",
                tint = TextWhite
            )
        }
    }
}
