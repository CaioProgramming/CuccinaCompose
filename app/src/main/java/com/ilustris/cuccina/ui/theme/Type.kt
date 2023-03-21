package com.ilustris.cuccina.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ilustris.cuccina.R

// Set of Material typography styles to start with
val mavenPro = FontFamily(Font(R.font.nunito_black))
val dosis = FontFamily(Font(R.font.dosis_variable_font_wght))
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = dosis,
        fontWeight = FontWeight.W800,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = dosis,
        fontWeight = FontWeight.W700,
        fontSize = 14.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = mavenPro,
        fontWeight = FontWeight.W700,
        fontSize = 22.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = mavenPro,
        fontWeight = FontWeight.Black,
        fontSize = 28.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)