package com.starbucks.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starbucks.categories.component.CardCategoryItem
import com.starbucks.categories.component.FeatureCard
import com.starbucks.shared.SurfaceBrand
import com.starbucks.shared.TextBrand
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.White
import com.starbucks.shared.domain.BeverageSubCategory
import com.starbucks.shared.domain.FoodSubCategory
import com.starbucks.shared.domain.ProductCategory
import kotlinx.coroutines.launch

@Composable
fun CategoryScreen(
    onNavigateToAllProducts: (ProductCategory) -> Unit,
    navigateToCategorySearch: (String, String) -> Unit,
) {
    val tabs = listOf("Featured", "Beverages", "Food")
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabIndexToItemIndex = listOf(0, 2, 4)

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val visibleIndices = visibleItems.map { it.index }

                selectedTabIndex = when {
                    visibleIndices.contains(0) -> 0 // Featured
                    visibleIndices.contains(2) -> 1 // Beverages
                    visibleIndices.contains(4) -> 2 // Food
                    else -> selectedTabIndex
                }
            }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            contentColor = White,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(3.dp),
                    color = SurfaceBrand
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                val selected = selectedTabIndex == index
                Tab(
                    selected = selected,
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(tabIndexToItemIndex[index])
                        }
                    },
                    text = {
                        Text(
                            title,
                            color = if (selected) SurfaceBrand else TextPrimary,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Featured
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Featured",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FeatureCard(
                        title = "Featured Beverage",
                        modifier = Modifier.weight(1f),
                    )
                    FeatureCard(
                        title = "Featured Food",
                        modifier = Modifier.weight(1f),
                    )
                }
            }


            // All Beverages
            item {
                SectionHeader("Beverages") {
                    onNavigateToAllProducts(ProductCategory.BEVERAGE)
                }
            }
            item {
                BeverageGrid(navigateToCategorySearch)
            }

            // All Food
            item {
                SectionHeader("Food") {
                    onNavigateToAllProducts(ProductCategory.FOOD)
                }
            }
            item {
                FoodGrid(navigateToCategorySearch)
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, onSeeAll: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            "See all",
            color = TextBrand,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onSeeAll() }
        )
    }
}

@Composable
fun BeverageGrid(navigateToCategorySearch: (String, String) -> Unit) {
    val beverageSubs = BeverageSubCategory.entries
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 1200.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
    ) {
        items(beverageSubs) { sub ->
            CardCategoryItem(
                sub.title,
                sub.imageUrl,
                onClick = {
                    navigateToCategorySearch(
                        ProductCategory.BEVERAGE.name,
                        sub.name
                    )
                }
            )
        }
    }
}

@Composable
fun FoodGrid(navigateToCategorySearch: (String, String) -> Unit) {
    val foodSubs = FoodSubCategory.entries
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 1200.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
    ) {
        items(foodSubs) { sub ->
            CardCategoryItem(
                sub.title,
                sub.imageUrl,
                onClick = {
                    navigateToCategorySearch(
                        ProductCategory.FOOD.name,
                        sub.name
                    )
                }
            )
        }
    }
}