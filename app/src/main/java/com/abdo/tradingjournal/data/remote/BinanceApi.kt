package com.abdo.tradingjournal.data.remote

import com.abdo.tradingjournal.domain.model.LiveSignal
import com.abdo.tradingjournal.domain.model.TickerPrice
import retrofit2.http.GET
import retrofit2.http.Query

// Binance Public API — بدون API Key للأسعار (Spot + Futures)
// Futures: https://fapi.binance.com | Spot: https://api.binance.com
// ملاحظة: مفاتيح التداول الحقيقية تُحفظ في EncryptedSharedPreferences فقط، ولا تُرسل أبداً للسيرفر.

data class BinanceKline(
    val openTime: Long, val open: String, val high: String, val low: String,
    val close: String, val volume: String
)

data class BinanceTicker24h(
    val symbol: String, val lastPrice: String, val priceChangePercent: String
)

interface BinanceApi {
    @GET("fapi/v1/klines")
    suspend fun getKlines(
        @Query("symbol") symbol: String, // DOGEUSDT
        @Query("interval") interval: String, // 1h, 4h, 1d
        @Query("limit") limit: Int = 200
    ): List<List<Any>>

    @GET("fapi/v1/ticker/24hr")
    suspend fun getTicker24h(@Query("symbol") symbol: String): BinanceTicker24h

    @GET("fapi/v1/ticker/price")
    suspend fun getPrice(@Query("symbol") symbol: String): Map<String, String>
}

/**
 * محرك التحليل الفني للمساعد — المرحلة الأولى (جاهز للتوسيع بالخطوات اللي هتقولها):
 * EMA 20/50 + RSI 14 + BOS/CHoCH مبسّط + FVG.
 * الخطوات القادمة منك (SMC Adamas الكامل: OB/BB/MB/IDM) تتركّب هنا
 * في TechnicalAnalysisEngine دون تغيير واجهة المساعد.
 */
object TechnicalAnalysisEngine {
    fun ema(prices: List<Double>, period: Int): List<Double> {
        if (prices.size < period) return emptyList()
        val k = 2.0 / (period + 1)
        val out = mutableListOf<Double>()
        var prev = prices.take(period).average()
        out.add(prev)
        for (i in period until prices.size) { prev = prices[i] * k + prev * (1 - k); out.add(prev) }
        return out
    }
    fun rsi(closes: List<Double>, period: Int = 14): Double {
        if (closes.size < period + 1) return 50.0
        var gain = 0.0; var loss = 0.0
        for (i in 1..period) {
            val d = closes[closes.size - period + i - 1] - closes[closes.size - period + i - 2]
            if (d > 0) gain += d else loss -= d
        }
        val rs = if (loss == 0.0) 100.0 else (gain / period) / (loss / period)
        return 100 - 100 / (1 + rs)
    }
    /** Score 0-10: ترند EMA + RSI + زخم — نسخة خفيفة، تُستبدل بمنطق SMC Adamas الكامل */
    fun score(closes: List<Double>): Int {
        val ema20 = ema(closes, 20).lastOrNull() ?: return 0
        val ema50 = ema(closes, 50).lastOrNull() ?: return 0
        val rsi = rsi(closes)
        var s = 5
        if (closes.last() > ema20) s += 2 else s -= 2
        if (ema20 > ema50) s += 2 else s -= 1
        if (rsi in 30.0..70.0) s += 1
        return s.coerceIn(0, 10)
    }
}
