package com.starbucks.shared.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.starbucks.shared.Alpha
import com.starbucks.shared.ButtonPrimary
import com.starbucks.shared.FontSize
import com.starbucks.shared.IconPrimary
import com.starbucks.shared.MontserratFontFamily
import com.starbucks.shared.Red
import com.starbucks.shared.Resources
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.domain.Size

@Composable
fun SizeOptionsSection(
    sizes: List<Size>?,
    onSizesChanged: (List<Size>) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "SIZE",
            fontSize = FontSize.REGULAR,
            color = TextPrimary.copy(alpha = Alpha.HALF),
            fontWeight = FontWeight.Bold,
            fontFamily = MontserratFontFamily()
        )

        (sizes ?: emptyList()).forEachIndexed { index, size ->
            Column(modifier = Modifier.fillMaxWidth()) {
                CustomTextField(
                    value = size.name,
                    onValueChange = { newValue ->
                        val newSizes = sizes!!.toMutableList()
                        newSizes[index] = newSizes[index].copy(name = newValue)
                        onSizesChanged(newSizes)
                    },
                    placeholder = "Size Name"
                )

                CustomTextField(
                    value = size.price.toString(),
                    onValueChange = { newValue ->
                        val parsed = newValue.toDoubleOrNull() ?: 0.0
                        val newSizes = sizes!!.toMutableList()
                        newSizes[index] = newSizes[index].copy(price = parsed)
                        onSizesChanged(newSizes)
                    },
                    placeholder = "Price",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                SecondaryButton(
                    onClick = {
                        val newSizes = sizes!!.toMutableList().also { it.removeAt(index) }
                        onSizesChanged(newSizes)
                    },
                    text = "Delete size",
                    icon = Resources.Icon.Delete,
                    borderColor = Red,
                    contentColor = IconPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )
            }
        }

        SecondaryButton(
            onClick = {
                val newSizes = (sizes ?: emptyList()) + Size(name = "", price = 0.0)
                onSizesChanged(newSizes)
            },
            text = "Add size option",
            icon = Resources.Icon.Plus,
            borderColor = ButtonPrimary,
            contentColor = IconPrimary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
