package com.starbucks.shared.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(product.thumbnail)
                    .crossfade(true)
                    .build(),
                contentDescription = product.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Title ---
        Text(
            text = product.title,
            fontWeight = FontWeight.Bold,
            fontSize = FontSize.REGULAR,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        // --- Description ---
        Text(
            text = product.description,
            fontSize = FontSize.EXTRA_SMALL,
            color = TextPrimary.copy(Alpha.HALF),
            fontWeight = FontWeight.Medium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- Size & Price Section ---
        if (product.category != ProductCategory.FOOD && !product.sizes.isNullOrEmpty()) {
            Text(
                text = "Size & Price",
                fontSize = FontSize.SMALL,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                product.sizes.forEach { size ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
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
