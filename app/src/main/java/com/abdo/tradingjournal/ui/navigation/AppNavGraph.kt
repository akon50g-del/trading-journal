package com.abdo.tradingjournal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abdo.tradingjournal.ui.auth.LoginScreen
import com.abdo.tradingjournal.ui.dashboard.DashboardScreen
import com.abdo.tradingjournal.ui.splash.SplashScreen

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val JOURNAL = "journal"
    const val ASSISTANT = "assistant"
    const val CALCULATOR = "calculator"
    const val WALLET = "wallet"
}

@Composable
fun AppNavGraph() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) { SplashScreen(onFinished = { nav.navigate(Routes.LOGIN) { popUpTo(Routes.SPLASH) { inclusive = true } } }) }
        composable(Routes.LOGIN) { LoginScreen(onLoginSuccess = { nav.navigate(Routes.DASHBOARD) { popUpTo(Routes.LOGIN) { inclusive = true } } }) }
        composable(Routes.DASHBOARD) { DashboardScreen(navController = nav) }
        // Journal / Assistant / Calculator / Wallet تُفتح من Dashboard عبر BottomNav
    }
}
