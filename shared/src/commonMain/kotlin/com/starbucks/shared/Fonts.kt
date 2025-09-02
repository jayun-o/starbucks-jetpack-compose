package com.starbucks.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import starbucks.shared.generated.resources.Res
import starbucks.shared.generated.resources.montserrat_regular
import starbucks.shared.generated.resources.raleway_medium

@Composable
fun MontserratFontFamily() = FontFamily(
    Font(Res.font.montserrat_regular)
)

@Composable
fun RaleWayFontFamily() = FontFamily(
    Font(Res.font.raleway_medium)
)

object FontSize {
    val EXTRA_SMALL = 10.sp
    val SMALL = 12.sp
    val REGULAR = 14.sp
    val EXTRA_REGULAR = 16.sp
    val MEDIUM = 18.sp
    val EXTRA_MEDIUM = 20.sp
    val LARGE = 30.sp
    val EXRTA_LARGE = 40.sp
}