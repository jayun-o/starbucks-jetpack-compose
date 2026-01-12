package com.starbucks.order

import ContentWithMessageBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.starbucks.order.component.OrderHistoryCard
import com.starbucks.shared.FontSize
import com.starbucks.shared.IconPrimary
import com.starbucks.shared.LanguageManager
import com.starbucks.shared.LocalizedStrings
import com.starbucks.shared.RaleWayFontFamily
import com.starbucks.shared.Resources
import com.starbucks.shared.Surface
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.domain.Order
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    navigateBack: () -> Unit
) {
    val viewModel = koinViewModel<OrderViewModel>()

    val messageBarState = rememberMessageBarState()
    val currentLanguage by LanguageManager.language.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.loadOrderHistory()
    }

    Scaffold (
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = LocalizedStrings.get("order",
                            currentLanguage
                        ),
                        fontFamily = RaleWayFontFamily(),
                        fontSize = FontSize.EXTRA_MEDIUM,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack){
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back Arrow icon",
                            tint = IconPrimary
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Surface,
                        scrolledContainerColor = Surface,
                        navigationIconContentColor = IconPrimary,
                        titleContentColor = TextPrimary,
                        actionIconContentColor = IconPrimary
                    ),
            )
        }
    ){ padding ->
        ContentWithMessageBar(
            contentBackgroundColor = Surface,
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            messageBarState = messageBarState,
            errorMaxLines = 2
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when {
                    viewModel.loading -> {
                        CircularProgressIndicator()
                        Text("กำลังโหลดคำสั่งซื้อ...")
                    }
                    viewModel.error != null -> {
                        Text("เกิดข้อผิดพลาด: ${viewModel.error}")
                    }
                    viewModel.orders.isEmpty() -> {
                        Text("ยังไม่มีคำสั่งซื้อ")
                    }
                    else -> {
                        OrderHistoryList(viewModel.orders)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderHistoryList(
    orders: List<Order>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        orders.forEach { order ->
            OrderHistoryCard(order = order)
        }
    }
}

