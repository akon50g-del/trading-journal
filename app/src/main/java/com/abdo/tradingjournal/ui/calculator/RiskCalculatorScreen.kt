package com.abdo.tradingjournal.ui.calculator

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abdo.tradingjournal.domain.usecase.RiskCalculator

/**
 * حاسبة إدارة المخاطر الآلية:
 * - تدخل رأس المال + الدخول + الوقف + الهدفين + نسبة المخاطرة (افتراضي 1%)
 * - تحسب فوراً: مبلغ المخاطرة $ / حجم الصفقة (عملات و $) / RR1 / RR2
 * - تمنع الصفقة لو تجاوزت 3% يومياً، وتحذّر لو RR < 1.5 أو الحجم > 50% من رأس المال
 * - زر "أرسل للسجل" ينشئ الصفقة بحجم محسوب تلقائياً
 */
@Composable
fun RiskCalculatorScreen() {
    var capital by remember { mutableStateOf("1250") }
    var entry by remember { mutableStateOf("0.169175") }
    var sl by remember { mutableStateOf("0.16240636") }
    var tp1 by remember { mutableStateOf("0.20574") }
    var tp2 by remember { mutableStateOf("0.24203") }
    var riskPct by remember { mutableStateOf(1f) }

    val result = remember(capital, entry, sl, tp1, tp2, riskPct) {
        runCatching {
            RiskCalculator.calculate(
                capital.toDouble(), entry.toDouble(), sl.toDouble(),
                tp1.toDouble(), tp2.toDouble(), riskPct.toDouble()
            )
        }.getOrNull()
    }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("حاسبة إدارة المخاطر 🧮", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(value = capital, onValueChange = { capital = it }, label = { Text("رأس المال ($)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = entry, onValueChange = { entry = it }, label = { Text("سعر الدخول") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = sl, onValueChange = { sl = it }, label = { Text("وقف الخسارة") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = tp1, onValueChange = { tp1 = it }, label = { Text("هدف 1 (TP1)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = tp2, onValueChange = { tp2 = it }, label = { Text("هدف 2 (TP2)") }, modifier = Modifier.fillMaxWidth())
        Text("نسبة المخاطرة: ${riskPct.toInt()}%  (الحد اليومي 3%)")
        Slider(value = riskPct, onValueChange = { riskPct = it }, valueRange = 0.5f..3f, steps = 4)

        result?.let { r ->
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("مبلغ المخاطرة: $${"%.2f".format(r.riskAmountUsd)}")
                    Text("حجم الصفقة: ${"%.2f".format(r.positionSizeCoins)} عملة  •  $${"%.2f".format(r.positionSizeUsd)}")
                    Text("RR1: ${"%.2f".format(r.riskReward1)}  •  RR2: ${"%.2f".format(r.riskReward2)}")
                    Text(if (r.isAllowed) "✅ مسموح — ضمن حد 3% اليومي" else "⛔ مرفوض — تجاوزت الحد اليومي", color = if (r.isAllowed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                    r.warning?.let { Text("⚠️ $it", color = MaterialTheme.colorScheme.error) }
                }
            }
            Button(onClick = { /* إنشاء صفقة في السجل بالحجم المحسوب */ }, enabled = r.isAllowed, modifier = Modifier.fillMaxWidth()) {
                Text("أرسل الصفقة للسجل بالحجم المحسوب")
            }
        }
    }
}
