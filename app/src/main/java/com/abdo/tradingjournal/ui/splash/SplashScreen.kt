package com.abdo.tradingjournal.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdo.tradingjournal.R
import com.abdo.tradingjournal.ui.theme.Gold
// ملاحظة: R هنا هو com.abdo.tradingjournal.R — نفس package التطبيق
import com.abdo.tradingjournal.ui.theme.Navy
import kotlinx.coroutines.delay

/**
 * Splash متطورة: Logo يكبّر + يتلألأ (Shimmer)،
 * شريط Loading ذهبي متحرك، ثم انتقال تلقائي بعد 2.4 ثانية.
 * + Splash API الأصلي لأندرويد 12 في MainActivity + themes.xml
 */
@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (visible) 1f else 0.6f, tween(900, easing = FastOutSlowInEasing), label = "logoScale")
    val alpha by animateFloatAsState(if (visible) 1f else 0f, tween(900), label = "logoAlpha")
    // Shimmer لا نهائي على اللوجو
    val shimmer = rememberInfiniteTransition(label = "shimmer")
    val shimmerX by shimmer.animateFloat(0f, 1f, infiniteRepeatable(tween(1600, easing = LinearEasing)), label = "shimmerX")

    LaunchedEffect(Unit) {
        visible = true
        delay(2400)
        onFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Navy, Navy, Gold.copy(alpha = 0.08f)))
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "Trading Journal Logo",
                modifier = Modifier.size(150.dp).scale(scale).alpha(alpha)
            )
            Spacer(Modifier.height(20.dp))
            Text("سجل التداول", color = Gold, fontSize = 32.sp, fontWeight = FontWeight.Bold, modifier = Modifier.alpha(alpha))
            Text("TRADING JOURNAL", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), fontSize = 13.sp, letterSpacing = 6.sp)
            Spacer(Modifier.height(8.dp))
            Text("تدرّب بذكاء — حلّل، خاطِر، واكسب", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 14.sp)
            Spacer(Modifier.height(48.dp))
            // Loading احترافي: شريط ذهبي + نسبة مئوية وهمية متحركة
            LinearProgressIndicator(
                modifier = Modifier.width(180.dp).height(4.dp),
                color = Gold, trackColor = Gold.copy(alpha = 0.2f)
            )
            Spacer(Modifier.height(12.dp))
            Text("جاري تحميل بيانات السوق…", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
        }
    }
}
