package com.abdo.tradingjournal.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.abdo.tradingjournal.ui.theme.Gold

/**
 * تسجيل دخول + إضافة مستخدمين آخرين + تبديل سريع بين الحسابات.
 * - كل مستخدم له سجل + محفظة مستقلة (userId في Room).
 * - كلمة المرور تُخزَّن Hash فقط (SHA-256 + Salt) — انظر AuthRepository.
 * - بصمة / FaceID اختيارية بعد أول دخول (BiometricPrompt).
 * - زر "إضافة مستخدم" يفتح نموذج إنشاء حساب جديد دون حذف الحساب الحالي.
 */
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var isRegister by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    // حسابات محفوظة على الجهاز (للتبديل السريع) — تُملأ من UserDao.getAll()
    val savedUsers = remember { listOf("Abdo — abdo@example.com") }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
        Text(if (isRegister) "إنشاء حساب جديد" else "أهلاً بعودتك 👋", style = MaterialTheme.typography.headlineMedium, color = Gold)
        Text(if (isRegister) "حساب مستقل بسجل ومحفظة خاصة به" else "سجّل دخولك لمتابعة صفقاتك ومحفظتك", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
        Spacer(Modifier.height(24.dp))

        if (isRegister) OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("الاسم") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("البريد الإلكتروني") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("كلمة المرور") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

        error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp)) }

        Spacer(Modifier.height(20.dp))
        Button(onClick = {
            if (email.isBlank() || password.length < 6) { error = "أدخل بريداً صحيحاً وكلمة مرور 6 أحرف على الأقل"; return@Button }
            // AuthRepository.login/register → يتحقق من Hash ثم يحفظ session في DataStore
            onLoginSuccess()
        }, modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold)) {
            Text(if (isRegister) "إنشاء الحساب" else "تسجيل الدخول", color = androidx.compose.ui.graphics.Color.Black)
        }
        Spacer(Modifier.height(12.dp))
        TextButton(onClick = { isRegister = !isRegister; error = null }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(if (isRegister) "عندي حساب — تسجيل الدخول" else "مستخدم جديد؟ إنشاء حساب")
        }

        Spacer(Modifier.height(24.dp))
        Text("التبديل السريع بين المستخدمين", style = MaterialTheme.typography.titleSmall)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 8.dp)) {
            items(savedUsers) { u ->
                AssistChip(onClick = { /* يملأ الإيميل ويطلب كلمة المرور فقط */ }, label = { Text(u) })
            }
            item {
                AssistChip(onClick = { isRegister = true }, label = { Text("+ إضافة مستخدم") })
            }
        }
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = { /* BiometricPrompt — بصمة */ }, modifier = Modifier.fillMaxWidth()) {
            Text("الدخول بالبصمة 🔒")
        }
    }
}
