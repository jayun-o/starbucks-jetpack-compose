package com.starbucks.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.starbucks.shared.BorderIdle
import com.starbucks.shared.SurfaceBrand
import com.starbucks.shared.SurfaceDarker
import com.starbucks.shared.SurfaceLighter
import com.starbucks.shared.TextBrand
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.White
import com.starbucks.shared.domain.*
import kotlinx.coroutines.launch

@Composable
fun CategoriesScreen(
    onNavigateToSection: (String) -> Unit,
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
                SectionHeader("Featured") { onNavigateToSection("featured") }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FeatureCard(title = "Featured Beverage", modifier = Modifier.weight(1f))
                    FeatureCard(title = "Featured Food", modifier = Modifier.weight(1f))
                }
            }

            // All Beverages
            item {
                SectionHeader("Beverages")
                { onNavigateToSection("all_beverages") }
            }
            item {
                BeverageGrid(onNavigateToSection)
            }

            // All Food
            item {
                SectionHeader("Food")
                { onNavigateToSection("all_food") }
            }
            item {
                FoodGrid(onNavigateToSection)
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
fun FeatureCard(
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceLighter)
                .border(
                    width = 1.dp,
                    color = BorderIdle,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = SurfaceDarker
            ) {

            }
            Spacer(Modifier.height(12.dp))
            Text(
                title,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun BeverageGrid(
    onNavigateToSection: (String) -> Unit
) {
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
                sub.title, sub.imageUrl
            ) {
                onNavigateToSection(sub.id)
            }
        }
    }
}

@Composable
fun FoodGrid(onNavigateToSection: (String) -> Unit) {
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
            CardCategoryItem(sub.title, sub.imageUrl) {

                onNavigateToSection(sub.id)
            }
        }
    }
}

@Composable
fun CardCategoryItem(
    name: String,
    imageUrl: String?,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceLighter)
                .border(
                    width = 1.dp,
                    color = BorderIdle,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imageUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = name,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
            } else {
                Surface(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                ) {

                }
            }
            Spacer(Modifier.width(16.dp))
            Text(name, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
