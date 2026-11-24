package com.abdo.tradingjournal.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// هوية بصرية: ذهبي على كحلي داكن — Trading / Premium
val Gold = Color(0xFFF0B90B)          // لون Binance الذهبي
val GoldDark = Color(0xFFC99400)
val Navy = Color(0xFF0B0E11)
val NavyCard = Color(0xFF1E2329)
val NavySurface = Color(0xFF181A20)
val ProfitGreen = Color(0xFF0ECB81)
val LossRed = Color(0xFFF6465D)
val TextPrimary = Color(0xFFEAECEF)
val TextSecondary = Color(0xFF848E9C)

private val DarkColors = darkColorScheme(
    primary = Gold, secondary = GoldDark, tertiary = ProfitGreen,
    background = Navy, surface = NavySurface, onBackground = TextPrimary,
    onSurface = TextPrimary, error = LossRed
)
private val LightColors = lightColorScheme(
    primary = GoldDark, secondary = Gold, tertiary = ProfitGreen,
    background = Color(0xFFF5F5F5), surface = Color.White,
    onBackground = Color(0xFF1E2329), onSurface = Color(0xFF1E2329), error = LossRed
)

@Composable
fun TradingJournalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography(),
        content = content
    )
}
