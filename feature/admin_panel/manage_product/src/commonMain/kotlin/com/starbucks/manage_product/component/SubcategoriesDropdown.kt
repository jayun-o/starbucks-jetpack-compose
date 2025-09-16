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
import com.starbucks.shared.domain.SubCategory
import com.starbucks.shared.domain.getSubCategoriesFor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubCategoriesDropdown(
    category: ProductCategory,
    selectedSubCategory: SubCategory?, // ค่าเลือกจากภายนอก
    onSubCategorySelected: (SubCategory) -> Unit,
    modifier: Modifier = Modifier,
    error: Boolean = false,
) {
    val subCategories = remember(category) { getSubCategoriesFor(category) }
    var expanded by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(
        targetValue = if (error) BorderError else BorderIdle
    )

    // ✅ ให้ dropdown sync state กับ selectedSubCategory เสมอ
    val selected = selectedSubCategory

    Column {
        Text(
            text = "Subcategory",
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
            TextField(
                value = selected?.title ?: "Select Subcategory",
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
                    focusedPlaceholderColor = TextPrimary.copy(alpha = Alpha.HALF),
                    unfocusedPlaceholderColor = TextPrimary.copy(alpha = Alpha.HALF),
                    disabledPlaceholderColor = TextPrimary.copy(alpha = Alpha.DISABLED),
                    disabledContainerColor = SurfaceDarker,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                subCategories.forEach { sub ->
                    DropdownMenuItem(
                        text = { Text(sub.title) },
                        onClick = {
                            onSubCategorySelected(sub) // ✅ ให้ parent จัดการ state
                            expanded = false
                        },
                        modifier = Modifier.background(SurfaceLighter)
                    )
                }
            }
        }
    }
}
