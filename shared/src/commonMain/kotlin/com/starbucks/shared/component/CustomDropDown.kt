package com.starbucks.shared.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.starbucks.shared.Alpha
import com.starbucks.shared.BorderError
import com.starbucks.shared.BorderIdle
import com.starbucks.shared.FontSize
import com.starbucks.shared.IconSecondary
import com.starbucks.shared.MontserratFontFamily
import com.starbucks.shared.SurfaceDarker
import com.starbucks.shared.SurfaceLighter
import com.starbucks.shared.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDown(
    modifier: Modifier = Modifier,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    placeholder: String? = null,
    enabled: Boolean = true,
    error: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = if (error) BorderError else BorderIdle
    )

    Column {
        Text(
            text = placeholder ?: "",
            fontSize = FontSize.REGULAR,
            color = TextPrimary.copy(alpha = Alpha.HALF),
            fontWeight = FontWeight.Bold,
            fontFamily = MontserratFontFamily()
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                modifier = modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(size = 6.dp)
                    )
                    .clip(RoundedCornerShape(size = 6.dp)),
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                enabled = enabled,
                placeholder = if (placeholder != null) {
                    {
                        Text(
                            modifier = Modifier.alpha(Alpha.DISABLED),
                            text = placeholder,
                            fontSize = FontSize.REGULAR
                        )
                    }
                } else null,
                singleLine = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                keyboardOptions = KeyboardOptions.Default,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = SurfaceLighter,
                    focusedContainerColor = SurfaceLighter,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    disabledTextColor = TextPrimary.copy(alpha = Alpha.DISABLED),
                    focusedPlaceholderColor = TextPrimary.copy(alpha = Alpha.HALF),
                    unfocusedPlaceholderColor = TextPrimary.copy(alpha = Alpha.HALF),
                    disabledPlaceholderColor = TextPrimary.copy(alpha = Alpha.DISABLED),
                    disabledContainerColor = SurfaceDarker,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    selectionColors = TextSelectionColors(
                        handleColor = IconSecondary,
                        backgroundColor = Color.Unspecified
                    )
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            onOptionSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
