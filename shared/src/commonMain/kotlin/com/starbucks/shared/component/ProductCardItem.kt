package com.starbucks.shared.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.starbucks.shared.FontSize
import com.starbucks.shared.Red
import com.starbucks.shared.SurfaceLighter
import com.starbucks.shared.TextWhite
import com.starbucks.shared.domain.Product

@Composable
fun ProductCardItem(
    modifier: Modifier = Modifier,
    product: Product,
    onClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceLighter)
            .fillMaxWidth()
            .clickable { onClick(product.id) }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- Thumbnail + Discount Tag ---
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(product.thumbnail)
                    .crossfade(enable = true)
                    .build(),
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(CircleShape)
            )

            if (product.isDiscounted) {
                Text(
                    text = "SALE -${product.discounted} %",
                    fontSize = FontSize.SMALL,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier
                        .background(
                            color = Red,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // --- Title ---
        Text(
            text = product.title,
            fontWeight = FontWeight.Medium,
            fontSize = FontSize.REGULAR,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}