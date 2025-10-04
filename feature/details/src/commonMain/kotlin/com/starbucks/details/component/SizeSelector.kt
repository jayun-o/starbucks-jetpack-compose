package com.starbucks.details.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.starbucks.shared.BorderIdle
import com.starbucks.shared.BorderSecondary
import com.starbucks.shared.FontSize
import com.starbucks.shared.Resources
import com.starbucks.shared.SurfaceBrand
import com.starbucks.shared.SurfaceLighter
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.TextWhite
import com.starbucks.shared.domain.Size
import org.jetbrains.compose.resources.painterResource

@Composable
fun SizeSelector(
    size: Size,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier,
    onSelect: (Size) -> Unit
) {
    Column(
        modifier = modifier
            .clickable { onSelect(size) }
            .clip(RoundedCornerShape(12.dp))
    ) {
        Box(
            modifier = Modifier
                .height(130.dp)
                .width(95.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) SurfaceBrand else SurfaceLighter)
                .border(
                    width = 1.dp,
                    color = if (isSelected) BorderSecondary else BorderIdle,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(all = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                when (size.name) {
                    "Short" -> Icon(
                        painter = if (isSelected) painterResource(Resources.Icon.ShortSizeSelected) else painterResource(Resources.Icon.ShortSize),
                        contentDescription = "Short icon",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(48.dp)
                    )
                    "Tall" -> Icon(
                        painter = if (isSelected) painterResource(Resources.Icon.TallSizeSelected) else painterResource(Resources.Icon.TallSize),
                        contentDescription = "Tall icon",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(48.dp)
                    )
                    "Grande" -> Icon(
                        painter = if (isSelected) painterResource(Resources.Icon.GrandeSizeSelected) else painterResource(Resources.Icon.GrandeSize),
                        contentDescription = "Grande icon",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(48.dp)
                    )
                    "Venti" -> Icon(
                        painter = if (isSelected) painterResource(Resources.Icon.VentiSizeSelected) else painterResource(Resources.Icon.VentiSize),
                        contentDescription = "Venti icon",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Text(
                    text = size.name,
                    fontSize = FontSize.SMALL,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) TextWhite else TextPrimary
                )
                Text(
                    text = "฿ ${size.price}",
                    fontSize = FontSize.SMALL,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) TextWhite else TextPrimary
                )
            }
        }
    }
}

