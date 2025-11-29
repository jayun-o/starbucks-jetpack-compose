package com.starbucks.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.starbucks.shared.domain.Order
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun OrderScreen(
    navigateBack: () -> Unit
) {
    val viewModel = koinViewModel<OrderViewModel>()
    LaunchedEffect(Unit) {
        viewModel.loadOrderHistory()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            viewModel.loading -> {
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

@Composable
fun OrderHistoryList(orders: List<Order>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        orders.forEach { order ->
            Text("Order ID: ${order.id}")
            Text("Order Date: ${order.createdAt}")
            Text("Order: ${order.items}" )
            Text("จำนวนสินค้า: ${order.items.size}")
            Text("ราคารวม: ${order.totalAmount}")
            Text("-----------------------------------")
        }
    }
}


