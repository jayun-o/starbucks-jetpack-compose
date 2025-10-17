package com.starbucks.details.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.starbucks.details.DetailsViewModel
import com.starbucks.shared.ButtonPrimary

@Composable
fun ResetButton(viewModel: DetailsViewModel) {
    OutlinedButton(
        onClick = { viewModel.clearSelections() },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(size = 4.dp),
        border = BorderStroke(
            1.dp,
            ButtonPrimary
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = ButtonPrimary,
        ),
        contentPadding = PaddingValues(all = 12.dp)
    ) {
        Text(
            text = "Reset"
        )
    }
}