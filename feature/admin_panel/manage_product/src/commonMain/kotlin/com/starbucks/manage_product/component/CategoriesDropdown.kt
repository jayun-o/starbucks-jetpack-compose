package com.starbucks.manage_product.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.starbucks.shared.Alpha
import com.starbucks.shared.BorderError
import com.starbucks.shared.BorderIdle
import com.starbucks.shared.FontSize
import com.starbucks.shared.MontserratFontFamily
import com.starbucks.shared.SurfaceDarker
import com.starbucks.shared.SurfaceLighter
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.domain.ProductCategory
import com.starbucks.shared.domain.displayName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesDropdown(
    category: ProductCategory,
    onCategorySelected: (ProductCategory) -> Unit,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    ){
    val selectedCategory by remember(category) { mutableStateOf(category) }
    var expanded by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(
        targetValue = if (error) BorderError else BorderIdle
    )

    Column {
        Text(
            text = "Category",
            fontSize = FontSize.REGULAR,
            color = TextPrimary.copy(alpha = Alpha.HALF),
            fontWeight = FontWeight.Bold,
            fontFamily = MontserratFontFamily()
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = modifier
        ) {
            // TextField ที่แสดง Category ที่เลือก
            TextField(
                value = selectedCategory.displayName(),
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(size = 6.dp)
                    )
                    .clip(RoundedCornerShape(size = 6.dp)),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = SurfaceLighter,
                    focusedContainerColor = SurfaceLighter,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    disabledTextColor = TextPrimary.copy(alpha = Alpha.DISABLED),
                    focusedPlaceholderColor = TextPrimary.copy(alpha  = Alpha.HALF),
                    unfocusedPlaceholderColor = TextPrimary.copy(alpha = Alpha.HALF),
                    disabledPlaceholderColor = TextPrimary.copy(alpha = Alpha.DISABLED),
                    disabledContainerColor = SurfaceDarker,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                )
            )

            // Dropdown list
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(SurfaceLighter)
            ) {
                ProductCategory.entries.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.displayName())},
                        onClick = {
                            onCategorySelected(category)
                            expanded = false
                        },
                        modifier = Modifier.background(SurfaceLighter)
                    )
                }
            }
        }
    }
}