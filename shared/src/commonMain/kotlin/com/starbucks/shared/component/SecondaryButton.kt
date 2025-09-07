package com.starbucks.shared.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.starbucks.shared.Alpha
import com.starbucks.shared.ButtonDisabled
import com.starbucks.shared.ButtonSecondary
import com.starbucks.shared.FontSize
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.TextWhite
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: DrawableResource? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
    containerColor: Color = Color.Transparent, // Outlined จะไม่มี background
    contentColor: Color = ButtonSecondary,     // สีข้อความ & ไอคอน
    borderColor: Color = ButtonSecondary,      // ✅ เพิ่มสี border
    disabledContainerColor: Color = Color.Transparent,
    disabledContentColor: Color = TextPrimary.copy(alpha = Alpha.DISABLED),
    disabledBorderColor: Color = ButtonDisabled, // ✅ border ตอน disable
) {
    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(size = 4.dp),
        border = BorderStroke(
            1.dp,
            if (enabled) borderColor else disabledBorderColor
        ), // ✅ border เปลี่ยนตามสถานะ
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = if (enabled) contentColor else disabledContentColor,
            disabledContentColor = disabledContentColor
        ),
        contentPadding = PaddingValues(all = 12.dp)
    ) {
        if (icon != null) {
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(icon),
                contentDescription = "Button icon"
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            text = text,
            fontSize = FontSize.REGULAR,
            fontWeight = FontWeight.Medium
        )
    }
}
