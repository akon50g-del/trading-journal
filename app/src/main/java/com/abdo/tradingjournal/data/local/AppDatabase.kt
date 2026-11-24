package com.abdo.tradingjournal.data.local

import androidx.room.*
import com.abdo.tradingjournal.domain.model.*

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY lastLoginAt DESC") suspend fun getAll(): List<User>
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1") suspend fun getByEmail(email: String): User?
    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insert(user: User)
    @Query("UPDATE users SET lastLoginAt = :ts WHERE id = :id") suspend fun touchLogin(id: String, ts: Long)
    @Delete suspend fun delete(user: User)
}

@Dao
interface TradeDao {
    @Query("SELECT * FROM trades WHERE userId = :userId ORDER BY openedAt DESC") suspend fun getByUser(userId: String): List<Trade>
    @Query("SELECT * FROM trades WHERE userId = :userId AND status = 'OPEN'") suspend fun getOpen(userId: String): List<Trade>
    @Insert suspend fun insert(trade: Trade)
    @Update suspend fun update(trade: Trade)
    @Delete suspend fun delete(trade: Trade)
    @Query("SELECT COUNT(*) FROM trades WHERE userId = :userId AND result = 'WIN'") suspend fun wins(userId: String): Int
    @Query("SELECT COUNT(*) FROM trades WHERE userId = :userId AND result = 'LOSS'") suspend fun losses(userId: String): Int
    @Query("SELECT COALESCE(SUM(pnlUsd),0) FROM trades WHERE userId = :userId AND status = 'CLOSED'") suspend fun totalPnl(userId: String): Double
}

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallets WHERE userId = :userId LIMIT 1") suspend fun getWallet(userId: String): Wallet?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun save(wallet: Wallet)
    @Query("SELECT * FROM wallet_transactions WHERE userId = :userId ORDER BY createdAt DESC LIMIT 50") suspend fun history(userId: String): List<WalletTransaction>
    @Insert suspend fun addTx(tx: WalletTransaction)
}

@Database(entities = [User::class, Trade::class, Wallet::class, WalletTransaction::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tradeDao(): TradeDao
    abstract fun walletDao(): WalletDao
}
