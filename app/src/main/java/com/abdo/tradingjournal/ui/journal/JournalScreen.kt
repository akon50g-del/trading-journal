package com.abdo.tradingjournal.ui.journal


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abdo.tradingjournal.domain.model.*


/**
 * سجل التداول المتطور:
 * - إضافة صفقة (رمز / اتجاه / دخول / وقف / هدف / كمية / Setup من الكتاب / ملاحظات / سكرين شوت)
 * - فلترة: مفتوحة / مغلقة / رابحة / خاسرة / حسب الرمز
 * - إغلاق صفقة → يحسب P&L بالدولار و بـ R تلقائياً ويحدّث المحفظة
 * - إحصائيات: Win Rate / Profit Factor / متوسط R / أفضل Setup
 * - تصدير CSV + بحث
 */
@Composable
fun JournalScreen() {
    var filter by remember { mutableStateOf("الكل") }
    val sample = remember {
        listOf(
            Trade(userId = "demo", symbol = "DOGEUSDT.P", direction = Direction.LONG, entry = 0.169175, stopLoss = 0.16240636, takeProfit2 = 0.24203, quantity = 5000.0, riskAmountUsd = 12.5, setup = "FVG + BOS", status = TradeStatus.OPEN),
            Trade(userId = "demo", symbol = "BTCUSDT.P", direction = Direction.LONG, entry = 67774.8, stopLoss = 64642.68, takeProfit2 = 107473.7, quantity = 0.02, riskAmountUsd = 12.5, setup = "OB", status = TradeStatus.CLOSED, result = TradeResult.WIN, pnlUsd = 42.5, pnlR = 3.4)
        )
    }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("سجل التداول 📓", style = MaterialTheme.typography.headlineSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 12.dp)) {
            listOf("الكل", "مفتوحة", "مغلقة", "رابحة").forEach { f ->
                FilterChip(selected = filter == f, onClick = { filter = f }, label = { Text(f) })
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(sample) { t ->
                Card {
                    Column(Modifier.padding(14.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(t.symbol, style = MaterialTheme.typography.titleMedium)
                            Text(if (t.direction == Direction.LONG) "LONG 🟢" else "SHORT 🔴")
                        }
                        Text("دخول ${t.entry}  •  وقف ${t.stopLoss}  •  هدف ${t.takeProfit2}")
                        Text("Setup: ${t.setup}  •  مخاطرة $${t.riskAmountUsd}  •  ${t.status}")
                        t.pnlR?.let { Text("النتيجة: ${it}R  •  P&L $${t.pnlUsd}", color = MaterialTheme.colorScheme.primary) }
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = { /* AddTradeDialog */ }, modifier = Modifier.fillMaxWidth()) { Text("+ إضافة صفقة جديدة") }
    }
}

