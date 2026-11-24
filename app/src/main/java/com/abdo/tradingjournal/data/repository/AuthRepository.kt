package com.abdo.tradingjournal.data.repository

import java.security.MessageDigest

/**
 * الأمان:
 * - كلمة المرور لا تُحفظ أبداً كنص — SHA-256(salt + password).
 * - الجلسة في DataStore (مشفرة عبر EncryptedSharedPreferences).
 * - مفاتيح Binance (لو أضفتها لاحقاً) في EncryptedSharedPreferences فقط.
 */
object AuthRepository {
    fun hashPassword(password: String, salt: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest((salt + password).toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
    fun isStrong(password: String): Boolean =
        password.length >= 8 && password.any { it.isDigit() } && password.any { it.isLetter() }
}
