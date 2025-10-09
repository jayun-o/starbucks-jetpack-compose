package com.starbucks.categories.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.starbucks.shared.BorderIdle
import com.starbucks.shared.SurfaceDarker
import com.starbucks.shared.SurfaceLighter
import com.starbucks.shared.domain.ProductCategory

@Composable
fun FeatureCard(
    title: String,
    modifier: Modifier = Modifier,
) {
    val category = if (title == "Featured Beverage") {
        ProductCategory.BEVERAGE
    } else {
        ProductCategory.FOOD
    }

    println("title: $title category: $category")

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
                val imageUrl = if (category == ProductCategory.BEVERAGE) {
                    "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FEXCLUSIVE.jpg?alt=media&token=9ab1b1bd-db5b-4153-b06b-09f3dc98108b"
                } else {
                    "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FEXCLUSIVE_FOOD.jpg?alt=media&token=08501637-814c-4960-bae8-8b71085178cd"
                }

                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = title,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                title,
                fontWeight = FontWeight.Medium
            )
        }
    }
}