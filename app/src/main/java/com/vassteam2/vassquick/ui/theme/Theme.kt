package com.vassteam2.vassquick.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ColorScheme = darkColorScheme(
    primary = Blue40,
    onPrimary = White,
    background = Black,
    onBackground = White,
    secondary = Grey20,
    onSecondary = White,
    onSecondaryContainer = Grey60,
    error = Red60,
    tertiary = Grey40,
    surface = Blue20,
    onSurface = Blue80,
    inverseSurface = Blue80,
    inverseOnSurface = Blue20,
    errorContainer = Red90,
    onErrorContainer = Red40
)
@Composable
fun VassQuickTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = ColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}