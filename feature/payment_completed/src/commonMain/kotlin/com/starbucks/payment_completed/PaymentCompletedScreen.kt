package com.starbucks.payment_completed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.starbucks.shared.Resources
import com.starbucks.shared.Surface
import com.starbucks.shared.component.InfoCard
import com.starbucks.shared.component.PrimaryButton

@Composable
fun PaymentCompletedScreen(
    isSuccess: Boolean?,
    error: String?,
    navigateBack: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
            .systemBarsPadding()
            .padding(24.dp)
    ){
        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.Center
        ){
            InfoCard(
                title = if (isSuccess != null) "Success" else "Oops!",
                subtitle = if (isSuccess != null) "Your purchase is on the way."
                else error ?: "Unknown error.",
                image = if (isSuccess != null) Resources.Image.Checkmark else Resources.Image.Cat
            )
        }
        PrimaryButton(
            text = "Go back",
            icon = Resources.Icon.RightArrow,
            onClick = navigateBack
        )
    }
}