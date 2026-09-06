package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

enum class ThemeMode {
  LIGHT,
  DARK,
  SYSTEM
}

private val DarkColorScheme =
  darkColorScheme(
    primary = IranianTurquoiseLight,
    onPrimary = Color.Black,
    primaryContainer = IranianTurquoiseContainerDark,
    onPrimaryContainer = IranianTurquoiseLight,
    secondary = PersianLapisLight,
    onSecondary = Color.White,
    tertiary = SoftSunGoldLight,
    onTertiary = Color.Black,
    background = DeepPersianNavyBackground,
    onBackground = OnBackgroundDark,
    surface = SurfaceDarkNavy,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDarkNavy,
    onSurfaceVariant = OnSurfaceSecondaryDark,
    error = UrgeAlertRed,
    onError = Color.White
  )

private val LightColorScheme =
  lightColorScheme(
    primary = IranianTurquoise,
    onPrimary = Color.White,
    primaryContainer = IranianTurquoiseContainerLight,
    onPrimaryContainer = IranianTurquoiseDark,
    secondary = PersianLapisBlue,
    onSecondary = Color.White,
    tertiary = SoftSunGold,
    onTertiary = Color.White,
    background = WarmWhiteBackground,
    onBackground = OnBackgroundLight,
    surface = SurfaceWhite,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceSecondaryLight,
    error = UrgeAlertRed,
    onError = Color.White
  )

@Composable
fun RahaaTheme(
  themeMode: ThemeMode = ThemeMode.SYSTEM,
  dynamicColor: Boolean = false, // Set to false to preserve our custom Persian aesthetic colors
  content: @Composable () -> Unit
) {
  val darkTheme = when (themeMode) {
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
    ThemeMode.SYSTEM -> isSystemInDarkTheme()
  }

  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
