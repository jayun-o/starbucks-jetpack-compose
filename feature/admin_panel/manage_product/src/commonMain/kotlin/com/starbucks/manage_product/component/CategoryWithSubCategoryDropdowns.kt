package com.starbucks.manage_product.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.starbucks.shared.domain.ProductCategory
import com.starbucks.shared.domain.SubCategory

@Composable
fun CategoryWithSubCategoryDropdowns(
    selectedCategory: ProductCategory,
    selectedSubCategory: SubCategory?,
    onCategorySelected: (ProductCategory) -> Unit,
    onSubCategorySelected: (SubCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    var category by remember { mutableStateOf(selectedCategory) }
    var subCategory by remember { mutableStateOf(selectedSubCategory) }

    Column(modifier = modifier) {
        // Category Dropdown
        CategoriesDropdown(
            category = category,
            onCategorySelected = {
                category = it
                onCategorySelected(it)
                // reset subcategory เมื่อเปลี่ยน category
                subCategory = null
                onSubCategorySelected(null)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // SubCategory Dropdown
        SubCategoriesDropdown(
            category = category,
            selectedSubCategory = subCategory,
            onSubCategorySelected = {
                subCategory = it
                onSubCategorySelected(it)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
