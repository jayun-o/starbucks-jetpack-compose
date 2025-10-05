package com.starbucks.details.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.starbucks.details.DetailsViewModel

@Composable
fun ResetButton(viewModel: DetailsViewModel) {
    Button(
        onClick = { viewModel.clearSelections() },
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Reset")
    }
}