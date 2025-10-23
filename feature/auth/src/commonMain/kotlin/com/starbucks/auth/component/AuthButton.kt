package com.starbucks.auth.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.starbucks.shared.FontSize
import com.starbucks.shared.SurfaceBrand
import com.starbucks.shared.TextWhite
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AuthButton(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    primaryText: String = "Sign in",
    secondaryText: String = "Signing in...",
    icon: DrawableResource,
    shape: Shape = RoundedCornerShape(size = 6.dp),
    backgroundColor: Color = SurfaceBrand,
    progressIndicatorColor: Color = TextWhite,
    contentColor: Color = TextWhite,
    onClick: () -> Unit
) {
    var buttonText by remember { mutableStateOf(primaryText) }
    LaunchedEffect(loading) {
        buttonText = if (loading) secondaryText else primaryText
    }

    Surface(
        modifier = modifier
            .clip(shape)
            .clickable(enabled = !loading) { onClick() },
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 14.dp)
                .animateContentSize(
                    animationSpec = tween(durationMillis = 200)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = loading,
            ) { loadingState ->
                if (!loadingState) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(icon),
                        contentDescription = primaryText,
                        tint = contentColor
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = progressIndicatorColor,
                        strokeWidth = 2.dp
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = buttonText,
                color = contentColor,
                fontSize = FontSize.REGULAR
            )
        }
    }
}