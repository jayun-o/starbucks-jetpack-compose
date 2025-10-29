package com.starbucks.cart

import ContentWithMessageBar
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.starbucks.cart.component.CartItemCard
import com.starbucks.shared.FontSize
import com.starbucks.shared.Resources
import com.starbucks.shared.Surface
import com.starbucks.shared.SurfaceError
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.component.InfoCard
import com.starbucks.shared.component.LoadingCard
import com.starbucks.shared.component.PrimaryButton
import com.starbucks.shared.util.DisplayResult
import com.starbucks.shared.util.RequestState
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@Composable
fun CartScreen(
    navigateToCheckout: (Double) -> Unit
) {
    val messageBarState = rememberMessageBarState()
    val viewModel = koinViewModel<CartViewModel>()
    val cartItemsWithProducts by viewModel.cartItemsWithProducts.collectAsState(RequestState.Loading)
    val cartTotal by viewModel.cartTotal.collectAsState(0.0)

    ContentWithMessageBar(
        contentBackgroundColor = Surface,
        messageBarState = messageBarState,
        errorMaxLines = 2
    ) {
        cartItemsWithProducts.DisplayResult(
            onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
            onSuccess = { data ->
                if (data.isNotEmpty()) {
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 120.dp, start = 12.dp, end = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(data, key = { it.first.id }) { pair ->
                                CartItemCard(
                                    cartItem = pair.first,
                                    product = pair.second,
                                    onMinusClick = { quantity ->
                                        viewModel.updateCartItemQuantity(
                                            id = pair.first.id,
                                            quantity = quantity,
                                            onSuccess = {},
                                            onError = { messageBarState.addError(it) }
                                        )
                                    },
                                    onPlusClick = { quantity ->
                                        viewModel.updateCartItemQuantity(
                                            id = pair.first.id,
                                            quantity = quantity,
                                            onSuccess = {},
                                            onError = { messageBarState.addError(it) }
                                        )
                                    },
                                    onDeleteClick = {
                                        viewModel.deleteCardItem(
                                            id = pair.first.id,
                                            onSuccess = {},
                                            onError = { messageBarState.addError(it) }
                                        )
                                    }
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(vertical = 16.dp, horizontal = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Total: ฿${cartTotal}",
                                fontSize = FontSize.MEDIUM,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )

                            PrimaryButton(
                                icon = Resources.Icon.Checkmark,
                                text = "Checkout",
                                enabled = cartTotal > 0,
                                onClick = {
                                    navigateToCheckout(cartTotal)
                                }
                            )
                        }
                    }
                } else {
                    InfoCard(
                        image = Resources.Image.ShoppingCart,
                        title = "Empty Cart",
                        subtitle = "Check some of our products."
                    )
                }
            },
            onError = { message ->
                InfoCard(
                    image = Resources.Image.error,
                    title = "Oops!",
                    subtitle = message
                )
            },
            transitionSpec = fadeIn() togetherWith fadeOut()
        )
    }
}