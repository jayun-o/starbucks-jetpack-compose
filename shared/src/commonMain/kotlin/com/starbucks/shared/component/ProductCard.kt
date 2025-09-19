package com.starbucks.shared.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.starbucks.shared.Alpha
import com.starbucks.shared.FontSize
import com.starbucks.shared.RaleWayFontFamily
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
    Column (
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
            .background(SurfaceLighter)
            .clickable { onClick(product.id) }
            .padding(12.dp)
    ){
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(product.thumbnail)
                    .crossfade(enable = true)
                    .build(),
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Text(
                text = "NEW",
                color = Color.White,
                modifier = Modifier
                    .background(Color(0xFF00704A), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .align(Alignment.TopStart)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(Alpha.HALF),
            text = product.description,
            fontSize = FontSize.REGULAR,
            color = TextPrimary,
            fontFamily = RaleWayFontFamily(),
            fontWeight = FontWeight.Medium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedContent(targetState = product.category) { category ->
            if (category == ProductCategory.BEVERAGE) {
                Spacer(modifier = Modifier.weight(1f))
            } else {
                Row {
                    product.sizes?.forEach { size ->
                        Text(size.name, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${size.price.toInt()} Baht")
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }
        }
    }
}