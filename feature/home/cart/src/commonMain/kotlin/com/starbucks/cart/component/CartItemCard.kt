package com.starbucks.cart.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.starbucks.shared.BorderIdle
import com.starbucks.shared.FontSize
import com.starbucks.shared.IconPrimary
import com.starbucks.shared.MontserratFontFamily
import com.starbucks.shared.Resources
import com.starbucks.shared.Surface
import com.starbucks.shared.SurfaceLighter
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.component.QuantityCounter
import com.starbucks.shared.domain.CartItem
import com.starbucks.shared.domain.Product
import com.starbucks.shared.domain.QuatityCounterSize
import org.jetbrains.compose.resources.painterResource

@Composable
fun CartItemCard(
    modifier: Modifier = Modifier,
    product: Product,
    cartItem: CartItem,
    onMinusClick: (Int) -> Unit,
    onPlusClick: (Int) -> Unit,
    onDeleteClick: () -> Unit,
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceLighter)
    ){
        AsyncImage(
            modifier = Modifier
                .width(120.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(size = 12.dp))
                .border(
                    width = 1.dp,
                    color = BorderIdle,
                    shape = RoundedCornerShape(size = 12.dp)
                ),
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(product.thumbnail)
                .crossfade(enable = true)
                .build(),
            contentDescription = "Product thumbnail image",
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = product.title,
                    fontFamily = MontserratFontFamily(),
                    fontSize = FontSize.MEDIUM,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(size = 6.dp))
                        .background(Surface)
                        .border(
                            width = 1.dp,
                            color = BorderIdle,
                            shape = RoundedCornerShape(size = 6.dp)
                        )
                        .clickable { onDeleteClick() }
                        .padding(all = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(Resources.Icon.Delete),
                        contentDescription = "Delete icon",
                        tint = IconPrimary
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                        .padding(top = 4.dp),
                ) {
                    Text(
                        text = "${cartItem.totalPrice}",
                        fontFamily = MontserratFontFamily(),
                        fontSize = FontSize.MEDIUM,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                QuantityCounter(
                    size = QuatityCounterSize.Small,
                    value = cartItem.quantity,
                    onMinusClick = onMinusClick,
                    onPlusClick = onPlusClick
                )
            }
            if (!cartItem.productCartItemDetail.isNullOrEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    cartItem.productCartItemDetail?.forEach { detail ->
                        Text(
                            text = "• $detail",
                            fontSize = FontSize.SMALL,
                            color = TextPrimary,
                            fontFamily = MontserratFontFamily(),
                            fontWeight = FontWeight.Normal,
                        )
                    }
                }
            }
        }
    }
}