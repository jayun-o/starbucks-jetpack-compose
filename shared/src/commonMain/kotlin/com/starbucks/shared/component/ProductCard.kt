package com.starbucks.shared.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.starbucks.shared.*
import com.starbucks.shared.domain.Product
import com.starbucks.shared.domain.ProductCategory
@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    onClick: (String) -> Unit
) {
    var titleMaxLines by remember { mutableStateOf(1) }
    var selectedSize by remember { mutableStateOf(product.sizes?.firstOrNull()) }

    val sizeOrder = listOf("Short", "Tall", "Grande", "Venti", "Trenta")

    val sortedSizes = remember(product.sizes) {
        product.sizes?.sortedWith(compareBy { size ->
            sizeOrder.indexOf(size.name).takeIf { it >= 0 } ?: Int.MAX_VALUE
        })
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceLighter)
            .width(250.dp)
            .height(470.dp)
            .clickable { onClick(product.id) }
            .padding(12.dp)
    ) {

        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(product.thumbnail)
                    .crossfade(true)
                    .build(),
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(CircleShape)
            )

            if (product.isDiscounted) {
                Text(
                    text = "SALE -${product.discounted}%",
                    fontSize = FontSize.SMALL,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier
                        .background(Red, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = product.title,
                fontWeight = FontWeight.Bold,
                fontSize = FontSize.REGULAR,
                textAlign = TextAlign.Center,
                maxLines = titleMaxLines,
                overflow = TextOverflow.Visible,
                onTextLayout = { layout ->
                    titleMaxLines = if (layout.lineCount > 1) 2 else 1
                },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = product.description,
                fontSize = FontSize.EXTRA_SMALL,
                color = TextPrimary.copy(Alpha.HALF),
                fontWeight = FontWeight.Medium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (product.category != ProductCategory.FOOD && !sortedSizes.isNullOrEmpty()) {
            Text(
                text = "Size Option",
                fontSize = FontSize.SMALL,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                sortedSizes.forEach { size ->
                    val isSelected = size == selectedSize
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) ButtonPrimary else Surface)
                            .clickable { selectedSize = size }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = size.name,
                            color = if (isSelected) TextWhite else TextPrimary,
                            fontSize = FontSize.SMALL,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

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
                    text = "${selectedSize?.price ?: product.price} ฿",
                    fontSize = FontSize.SMALL,
                    fontWeight = FontWeight.Bold
                )
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
