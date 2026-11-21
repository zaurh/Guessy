package com.zaurh.guessy.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// The design is a bright gradient in both system themes, so a single palette is used.
private val GuessyColors = lightColors(
    primary = Sky,
    primaryVariant = MaleBlue,
    secondary = FemalePink,
    background = Mint,
    surface = Color.White,
    error = ErrorRed,
    onPrimary = Color.White,
    onBackground = Ink,
    onSurface = Ink
)

@Composable
fun GuessyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = GuessyColors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
