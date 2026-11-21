package com.zaurh.guessy.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.zaurh.guessy.R

@OptIn(ExperimentalTextApi::class)
private fun fredoka(weight: FontWeight) = Font(
    resId = R.font.fredoka,
    weight = weight,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight))
)

/** Rounded, friendly typeface used across the app (variable font, OFL licensed). */
val Fredoka = FontFamily(
    fredoka(FontWeight.Normal),
    fredoka(FontWeight.Medium),
    fredoka(FontWeight.SemiBold),
    fredoka(FontWeight.Bold)
)

val Typography = Typography(defaultFontFamily = Fredoka)
