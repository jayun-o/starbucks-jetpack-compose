package com.starbucks.shared.component.maps

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import com.starbucks.shared.Resources
import org.jetbrains.compose.resources.painterResource
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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

@Composable
fun MapTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    enabled: Boolean = true,
    error: Boolean = false,
    expanded: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text
    )
) {
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
        TextField(
            modifier = modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(size = 6.dp)
                )
                .clip(RoundedCornerShape(size = 6.dp)),
            enabled = enabled,
            value = value,
            onValueChange = onValueChange,
            placeholder = if (placeholder != null){
                {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            modifier = Modifier.alpha(Alpha.DISABLED),
                            text = placeholder,
                            fontSize = FontSize.REGULAR
                        )
                        Icon(
                            modifier = Modifier.alpha(Alpha.DISABLED),
                            painter = painterResource(Resources.Icon.MapPin),
                            contentDescription = null,
                        )
                    }
                }
            } else null,
            singleLine = !expanded,
            shape = RoundedCornerShape(size = 6.dp),
            keyboardOptions = keyboardOptions,
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
                selectionColors = TextSelectionColors(
                    handleColor = IconSecondary,
                    backgroundColor = Color.Unspecified
                )
            )
        )
    }
}



