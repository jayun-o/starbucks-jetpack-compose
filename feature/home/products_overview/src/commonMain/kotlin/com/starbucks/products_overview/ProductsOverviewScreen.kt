package com.starbucks.products_overview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.starbucks.products_overview.component.MainProductCard
import com.starbucks.shared.Alpha
import com.starbucks.shared.Resources
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.component.InfoCard
import com.starbucks.shared.component.LoadingCard
import com.starbucks.shared.component.ProductCardItem
import com.starbucks.shared.util.DisplayResult
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductsOverviewScreen(
    navigateToDetails: (String) -> Unit
) {
    val viewModel = koinViewModel<ProductsOverviewViewModel>()
    val products by viewModel.products.collectAsState()
    val listState = rememberLazyListState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        val cardHeight = screenHeight * 0.4f
        val horizontalPadding = screenWidth * 0.04f

        products.DisplayResult(
            onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
            onSuccess = { productList ->
                AnimatedContent(targetState = productList.distinctBy { it.id }) { products ->
                    if (products.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 12.dp)
                        ) {
                            // ✅ LazyRow ปรับขนาดการ์ดตามหน้าจอ
                            LazyRow(
                                state = listState,
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                itemsIndexed(
                                    items = products
                                        .filter { it.isNew == true }
                                        .sortedBy { it.createdAt }
                                        .take(6),
                                    key = { _, item -> item.id }
                                ) { index, product ->
                                    val centeredIndex by remember { derivedStateOf {
                                        val layoutInfo = listState.layoutInfo
                                        val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset / 2
                                        layoutInfo.visibleItemsInfo.minByOrNull { item ->
                                            val itemCenter = item.offset + item.size / 2
                                            kotlin.math.abs(itemCenter - viewportCenter)
                                        }?.index
                                    }}

                                    val isLarge = index == centeredIndex
                                    val animatedScale by animateFloatAsState(
                                        targetValue = if (isLarge) 1f else 0.85f,
                                        animationSpec = tween(300)
                                    )

                                    MainProductCard(
                                        modifier = Modifier
                                            .scale(animatedScale)
                                            .height(cardHeight)
                                            .width(screenWidth * 0.7f),
                                        product = product,
                                        isLarge = isLarge,
                                        onClick = { navigateToDetails(it) }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(screenHeight * 0.03f))

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .alpha(Alpha.HALF),
                                text = "Discounted Products",
                                fontSize = with(LocalDensity.current) { (screenWidth * 0.045f).toSp() },
                                color = TextPrimary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(screenHeight * 0.02f))

                            // ✅ ใช้ Adaptive Grid
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = screenWidth / 2.5f),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = horizontalPadding),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                items(
                                    items = products.filter { it.isDiscounted == true }
                                        .sortedBy { it.createdAt }
                                        .take(6),
                                    key = { item -> item.id }
                                ) { product ->
                                    ProductCardItem(
                                        product = product,
                                        onClick = { navigateToDetails(it) }
                                    )
                                }
                            }
                        }
                    } else {
                        InfoCard(
                            image = Resources.Image.error,
                            title = "Nothing here",
                            subtitle = "Empty product list."
                        )
                    }
                }
            },
            onError = { message ->
                InfoCard(
                    image = Resources.Image.error,
                    title = "Oops!",
                    subtitle = message
                )
            }
        )
    }
}

