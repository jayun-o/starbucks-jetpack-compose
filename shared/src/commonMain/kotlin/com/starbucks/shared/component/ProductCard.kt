package com.starbucks.shared.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.starbucks.shared.Alpha
import com.starbucks.shared.FontSize
import com.starbucks.shared.SurfaceLighter
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.domain.Product
import com.starbucks.shared.domain.ProductCategory

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    onClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceLighter)
            .width(240.dp)
            .clickable { onClick(product.id) }
            .padding(12.dp)
    ) {
        // --- Thumbnail ---
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(product.thumbnail)
                .crossfade(enable = true)
                .build(),
            contentDescription = product.title,
            modifier = Modifier
                .height(180.dp)
                .align(CenterHorizontally)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- Title ---
        Text(
            text = product.title,
            fontWeight = FontWeight.Bold,
            fontSize = FontSize.EXTRA_REGULAR,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = product.description,
            fontSize = FontSize.EXTRA_SMALL,
            color = TextPrimary.copy(Alpha.HALF),
            fontWeight = FontWeight.Medium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        // --- Size Options (เฉพาะ category ที่ไม่ใช่ FOOD) ---
        if (product.category != ProductCategory.FOOD && !product.sizes.isNullOrEmpty()) {
            Text(
                text = "Size & Price",
                fontSize = FontSize.SMALL,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                product.sizes.forEach { size ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = size.name,
                            fontSize = FontSize.SMALL,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${size.price} ฿",
                            fontSize = FontSize.SMALL,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.height(6.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Price",
                        fontSize = FontSize.SMALL,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${product.price} ฿",
                        fontSize = FontSize.SMALL,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
