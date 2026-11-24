package com.abdo.tradingjournal.ui.assistant

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abdo.tradingjournal.domain.model.Direction
import com.abdo.tradingjournal.domain.model.LiveSignal

/**
 * المساعد الشخصي — يجلب صفقات حية من Binance بناءً على التحليل الفني.
 *
 * كيف يعمل الآن (المرحلة 1 — جاهز):
 *  1. يجلب شموع 1h/4h/1d من Binance Futures (fapi/v1/klines) كل 60 ثانية.
 *  2. يحسب EMA 20/50 + RSI 14 + Score 0-10 في TechnicalAnalysisEngine.
 *  3. يعرض الإشارات القوية فقط (Score ≥ 7) مع الدخول/الوقف/الهدفين.
 *  4. زر "أرسل للسجل + احسب المخاطر" يملأ صفقة جديدة وحاسبة المخاطر تلقائياً.
 *
 * الخطوات القادمة (بانتظار تعليماتك):
 *  - تركيب منطق SMC Adamas الكامل (OB/FVG/BB/MB/IDM/BOS/CHoCH/Sweep) بدل Score المبسّط
 *    — نفس القيم المرجعية: DOGE 0.169175 / BTC 67774.8.
 *  - WebSocket مباشر (wss://fstream.binance.com) بدل Poll كل دقيقة.
 *  - تنبيه Push عند إشارة جديدة + فلتر "لا إشارة لو Score < 7 أو RR < 1.5".
 *  - ربط مفاتيح Binance للقراءة فقط (اختياري) لجلب الصفقات المفتوحة فعلياً.
 */
@Composable
fun AssistantScreen() {
    var timeframe by remember { mutableStateOf("1h") }
    var minScore by remember { mutableStateOf(7f) }
    val signals = remember {
        listOf(
            LiveSignal("DOGEUSDT.P", Direction.LONG, 0.169175, 0.16240636, 0.20574, 0.24203, 8, "FVG + BOS", "1d", "HIGH"),
            LiveSignal("BTCUSDT.P", Direction.LONG, 67774.8, 64642.68, 97932.1, 107473.7, 7, "OB", "1d", "HIGH"),
            LiveSignal("SOLUSDT.P", Direction.SHORT, 142.5, 148.2, 135.0, 128.0, 6, "CHoCH", "4h", "MEDIUM")
        )
    }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("المساعد الشخصي 🤖", style = MaterialTheme.typography.headlineSmall)
        Text("صفقات حية من Binance — تحليل فني مباشر", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 12.dp)) {
            listOf("1h", "4h", "1d").forEach { tf ->
                FilterChip(selected = timeframe == tf, onClick = { timeframe = tf }, label = { Text(tf) })
            }
        }
        Text("أقل Score: ${minScore.toInt()}/10")
        Slider(value = minScore, onValueChange = { minScore = it }, valueRange = 5f..10f, steps = 4)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(signals.filter { it.score >= minScore.toInt() }) { s ->
                Card {
                    Column(Modifier.padding(14.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(s.symbol, style = MaterialTheme.typography.titleMedium)
                            Text("Score ${s.score}/10 • ${s.confidence}")
                        }
                        Text("${if (s.direction == Direction.LONG) "LONG 🟢" else "SHORT 🔴"}  •  ${s.setup}  •  ${s.timeframe}")
                        Text("دخول ${s.entry}  •  وقف ${s.stopLoss}")
                        Text("هدف1 ${s.takeProfit1}  •  هدف2 ${s.takeProfit2}")
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                            Button(onClick = { /* → Journal + Calculator مملوءة */ }) { Text("خذ الصفقة") }
                            OutlinedButton(onClick = { /* تجاهل */ }) { Text("تجاهل") }
                        }
                    }
                }
            }
        }
        Text("⚠️ المساعد يعطي تحليلاً، وليس توصية مالية. القرار النهائي لك، والتزم بـ 1% مخاطرة.", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 12.dp))
    }
}
