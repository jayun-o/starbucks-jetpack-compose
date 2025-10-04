package com.starbucks.details.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starbucks.shared.Alpha
import com.starbucks.shared.FontSize
import com.starbucks.shared.MontserratFontFamily
import com.starbucks.shared.Resources
import com.starbucks.shared.TextPrimary
import org.jetbrains.compose.resources.painterResource

@Composable
fun Stepper(
    value: Int,
    onValueChange: (Int, Double) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    minValue: Int = 0,
    maxValue: Int = 5,
    label: String = "Shot(s)",
    pricePerUnit: Double = 20.0
) {
    val totalPrice = value * pricePerUnit

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = placeholder ?: "",
                fontSize = FontSize.REGULAR,
                color = TextPrimary.copy(alpha = Alpha.HALF),
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFontFamily()
            )
            Text(
                text = "฿ ${totalPrice}",
                fontSize = FontSize.REGULAR,
                color = TextPrimary.copy(alpha = Alpha.HALF),
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFontFamily()
            )
        }
        Row(
            modifier = modifier
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Minus Button
            IconButton(
                onClick = {
                    if (value > minValue) {
                        val newValue = value - 1
                        onValueChange(newValue, newValue * pricePerUnit)
                    }
                }
            ) {
                Box(
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = Color(0xFF006241),
                        shape = CircleShape
                    )
                ){
                    Icon(
                        painter = painterResource(Resources.Icon.Minus),
                        contentDescription = "Decrease",
                        tint = Color(0xFF006241)
                    )
                }
            }

            Text(
                text = "$value $label",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary.copy(Alpha.HALF)
            )

            // Plus Button
            IconButton(
                onClick = {
                    if (value < maxValue) {
                        val newValue = value + 1
                        onValueChange(newValue, newValue * pricePerUnit)
                    }
                }
            ) {
                Box(
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = Color(0xFF006241),
                        shape = CircleShape
                    )
                ){
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = Color(0xFF006241)
                    )
                }
            }
        }
    }
}
