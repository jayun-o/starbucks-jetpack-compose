package com.starbucks.cart.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.starbucks.shared.IconWhite
import com.starbucks.shared.MontserratFontFamily
import com.starbucks.shared.Red
import com.starbucks.shared.Resources
import com.starbucks.shared.SurfaceLighter
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.component.QuantityCounter
import com.starbucks.shared.domain.CartItem
import com.starbucks.shared.domain.Product
import com.starbucks.shared.domain.QuatityCounterSize
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartItemCard(
    modifier: Modifier = Modifier,
    product: Product,
    cartItem: CartItem,
    onMinusClick: (Int) -> Unit,
    onPlusClick: (Int) -> Unit,
    onDeleteClick: () -> Unit,
) {
    var show by remember { mutableStateOf(true) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                show = false
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(show) {
        if (!show) {
            delay(300)
            onDeleteClick()
        }
    }

    AnimatedVisibility(
        visible = show,
        exit = shrinkVertically(
            animationSpec = tween(300),
            shrinkTowards = Alignment.Top
        ) + fadeOut(animationSpec = tween(300))
    ) {
        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Red)
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        painter = painterResource(Resources.Icon.Delete),
                        contentDescription = "Delete",
                        tint = IconWhite,
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            enableDismissFromStartToEnd = false,
            modifier = modifier
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceLighter)
            ) {
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
                    Text(
                        text = product.title,
                        fontFamily = MontserratFontFamily(),
                        fontSize = FontSize.MEDIUM,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 4.dp),
                        ) {
                            Text(
                                text = "฿${cartItem.price}",
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
    }
}