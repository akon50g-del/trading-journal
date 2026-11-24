package com.abdo.tradingjournal.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// ---------- المستخدمون (متعدد) ----------
@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val passwordHash: String, // SHA-256 + Salt — لا تُحفظ كلمة المرور نصاً أبداً
    val avatarColor: Long = 0xFFF0B90B,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis()
)

// ---------- الصفقة (سجل التداول) ----------
enum class Direction { LONG, SHORT }
enum class TradeStatus { OPEN, CLOSED, PENDING }
enum class TradeResult { WIN, LOSS, BREAKEVEN, RUNNING }

@Entity(tableName = "trades")
data class Trade(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String,
    val symbol: String,              // BTCUSDT.P / DOGEUSDT.P
    val direction: Direction,
    val entry: Double,
    val stopLoss: Double,
    val takeProfit1: Double? = null,
    val takeProfit2: Double? = null,
    val quantity: Double,
    val riskPercent: Double = 1.0,   // قاعدة عبدو: 1% للصفقة، 3% يومياً
    val riskAmountUsd: Double,
    val status: TradeStatus = TradeStatus.OPEN,
    val result: TradeResult = TradeResult.RUNNING,
    val pnlUsd: Double? = null,
    val pnlR: Double? = null,        // النتيجة بـ R — نفس وحدة مؤشر SMC Adamas
    val setup: String = "",          // OB / FVG / BOS / Sweep — من الكتاب
    val timeframe: String = "1h",
    val notes: String = "",
    val screenshotPath: String? = null,
    val openedAt: Long = System.currentTimeMillis(),
    val closedAt: Long? = null
)

// ---------- المحفظة ----------
@Entity(tableName = "wallet_transactions")
data class WalletTransaction(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String,
    val type: String, // DEPOSIT / WITHDRAWAL / TRADE_PNL / FEE
    val amountUsd: Double, // موجب = إيداع/ربح، سالب = سحب/خسارة
    val balanceAfter: Double,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "wallets")
data class Wallet(
    @PrimaryKey val userId: String,
    val balanceUsd: Double = 0.0,
    val initialCapitalUsd: Double = 0.0,
    val totalDeposited: Double = 0.0,
    val totalWithdrawn: Double = 0.0
)

// ---------- إشارة المساعد (Binance Live) ----------
data class LiveSignal(
    val symbol: String,
    val direction: Direction,
    val entry: Double,
    val stopLoss: Double,
    val takeProfit1: Double,
    val takeProfit2: Double,
    val score: Int,          // 0-10 — نفس فكرة Score في SMC Adamas
    val setup: String,       // FVG / OB / BOS+CHoCH
    val timeframe: String,
    val confidence: String,  // HIGH / MEDIUM / LOW
    val createdAt: Long = System.currentTimeMillis()
)

data class TickerPrice(val symbol: String, val price: Double, val change24h: Double)
