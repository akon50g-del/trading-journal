package com.abdo.tradingjournal.domain.usecase

import kotlin.math.abs

/**
 * حاسبة إدارة المخاطر الآلية — قاعدة عبدو: 1% للصفقة، 3% يومياً كحد أقصى.
 * نفس معادلات حاسبة منصة الويب (تم التحقق منها يدوياً هناك).
 */
object RiskCalculator {

    data class RiskResult(
        val riskAmountUsd: Double,
        val positionSizeCoins: Double,
        val positionSizeUsd: Double,
        val riskReward1: Double,
        val riskReward2: Double,
        val isAllowed: Boolean,      // false لو تجاوز حد 3% اليومي
        val warning: String?
    )

    fun calculate(
        capitalUsd: Double,
        entry: Double,
        stopLoss: Double,
        takeProfit1: Double,
        takeProfit2: Double,
        riskPercent: Double = 1.0,
        dailyRiskUsedPercent: Double = 0.0
    ): RiskResult {
        val riskAmount = capitalUsd * riskPercent / 100.0
        val riskPerCoin = abs(entry - stopLoss)
        val sizeCoins = if (riskPerCoin > 0) riskAmount / riskPerCoin else 0.0
        val sizeUsd = sizeCoins * entry
        val rr1 = if (riskPerCoin > 0) abs(takeProfit1 - entry) / riskPerCoin else 0.0
        val rr2 = if (riskPerCoin > 0) abs(takeProfit2 - entry) / riskPerCoin else 0.0
        val allowed = (dailyRiskUsedPercent + riskPercent) <= 3.0
        val warning = when {
            !allowed -> "تجاوزت حد المخاطرة اليومي 3% — لا تدخل هذه الصفقة"
            rr1 < 1.5 -> "RR ضعيف (أقل من 1.5) — راجع الصفقة"
            sizeUsd > capitalUsd * 0.5 -> "حجم الصفقة أكبر من 50% من رأس المال — قلّل الرافعة"
            else -> null
        }
        return RiskResult(riskAmount, sizeCoins, sizeUsd, rr1, rr2, allowed, warning)
    }

    /** حجم الصفقة من عدد R المستهدف — مثال: تريد ربح 2R بهدف 0.24203 */
    fun sizeForTargetR(capitalUsd: Double, riskPercent: Double, entry: Double, stopLoss: Double): Double {
        val risk = capitalUsd * riskPercent / 100.0
        val dist = abs(entry - stopLoss)
        return if (dist > 0) risk / dist else 0.0
    }
}
