package com.abdo.tradingjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.abdo.tradingjournal.ui.navigation.AppNavGraph
import com.abdo.tradingjournal.ui.theme.TradingJournalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Splash API الأصلي لأندرويد 12+ — يشتغل قبل Compose
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            TradingJournalTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavGraph()
                }
            }
        }
    }
}
