# سجل التداول | Trading Journal — Android (Kotlin)

تطبيق أندرويد احترافي بلغة **Kotlin + Jetpack Compose**، يستهدف **Android 12 فما فوق (minSdk 31، targetSdk 35)**.

صاحب المشروع: **Abdo** — نفس قواعد التداول المعتمدة عنده: **1% مخاطرة للصفقة، 3% حد يومي**، والنتيجة تُقاس بـ **R** مثل مؤشر SMC Adamas.

## ما تم بناؤه

### 1. شاشة ترحيب Splash متطورة
- `MainActivity` يستخدم **SplashScreen API الأصلي لأندرويد 12+** + `themes.xml`.
- `ui/splash/SplashScreen.kt`: لوجو يكبّر ويتلألأ (Scale + Fade + Shimmer)، شريط Loading ذهبي متحرك، انتقال تلقائي بعد 2.4 ثانية.
- اللوجو: `res/drawable/ic_logo.xml` — Vector احترافي (حرف A على شكل شمعة صاعدة + شموع خضراء)، هوية ذهبي/كحلي مثل Binance.

### 2. تسجيل الدخول + مستخدمين آخرين
- `ui/auth/LoginScreen.kt`: دخول / إنشاء حساب / **تبديل سريع بين المستخدمين** / زر `+ إضافة مستخدم` / دخول بالبصمة.
- كل مستخدم له سجل + محفظة مستقلة (`userId` في Room).
- كلمة المرور **Hash فقط** (SHA-256 + Salt) في `data/repository/AuthRepository.kt` — لا تُحفظ كنص أبداً.

### 3. سجل تداول متطور — `ui/journal/JournalScreen.kt`
- إضافة صفقة: رمز / LONG-SHORT / دخول / وقف / هدف1-هدف2 / كمية / Setup من الكتاب (OB/FVG/BOS/Sweep) / ملاحظات / سكرين شوت.
- فلترة (مفتوحة/مغلقة/رابحة)، إغلاق صفقة → P&L بالدولار **و بـ R** تلقائياً + تحديث المحفظة.
- إحصائيات: Win Rate / Profit Factor / متوسط R / أفضل Setup — وتصدير CSV (في ViewModel).

### 4. المساعد الشخصي — صفقات حية من Binance — `ui/assistant/AssistantScreen.kt`
- يجلب شموع 1h/4h/1d من **Binance Futures Public API** (`fapi/v1/klines`) كل 60 ثانية — بدون API Key للأسعار.
- `data/remote/BinanceApi.kt` + `TechnicalAnalysisEngine`: EMA 20/50 + RSI 14 + Score 0-10. يعرض فقط Score ≥ 7 مع الدخول/الوقف/الهدفين.
- زر "خذ الصفقة" يملأ السجل + حاسبة المخاطر تلقائياً.
- **بانتظار خطواتك القادمة**: تركيب منطق SMC Adamas الكامل (OB/FVG/BB/MB/IDM) بدل Score المبسّط، و WebSocket مباشر، و Push عند إشارة جديدة. مكان التركيب جاهز وموثّق في الملف نفسه.

### 5. حاسبة إدارة المخاطر الآلية — `ui/calculator/` + `domain/usecase/RiskCalculator.kt`
- نفس معادلات حاسبة منصة الويب: مبلغ المخاطرة $ / حجم الصفقة (عملات و $) / RR1 / RR2.
- **تمنع** الصفقة لو تجاوزت 3% يومياً، وتحذّر لو RR < 1.5 أو الحجم > 50% من رأس المال.

### 6. محفظة التداول — `ui/wallet/WalletScreen.kt`
- رأس المال + الرصيد + المودَع/المسحوب/الربح التراكمي، **إيداع/سحب** مع سجل معاملات.
- متابعة الصفقات المفتوحة بسعر Binance الحي، ومنحنى رأس المال + تنبيه Drawdown 10%.
- الرصيد **سجل محاسبي داخل التطبيق فقط** — لا يتصل بأي بنك.

## تحسينات إضافية من عندي
- **Dark/Light Mode** تلقائي + هوية Gold/Navy في `ui/theme/Theme.kt`.
- **Offline أولاً**: كل شيء في Room، والأسعار فقط من الإنترنت.
- **Hilt DI** في `di/AppModule.kt`، و **Navigation Compose** في `ui/navigation/AppNavGraph.kt`.
- Bottom Navigation بخمسة أقسام، وتنبيه دائم بقاعدة 1%/3%.
- إخلاء مسؤولية في شاشة المساعد: التحليل ليس توصية مالية.

## هيكل المشروع
```
android-trading-journal/
├── settings.gradle.kts / build.gradle.kts / gradle.properties
└── app/
    ├── build.gradle.kts   (Compose, Room, Retrofit, Hilt, Lottie, MPAndroidChart)
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/abdo/tradingjournal/
        │   ├── MainActivity.kt / TradingJournalApp.kt
        │   ├── di/AppModule.kt
        │   ├── domain/model/Models.kt / domain/usecase/RiskCalculator.kt
        │   ├── data/local/AppDatabase.kt / data/remote/BinanceApi.kt / data/repository/AuthRepository.kt
        │   └── ui/{splash,auth,dashboard,journal,assistant,calculator,wallet,theme,navigation}/
        └── res/drawable/ic_logo.xml / res/values/{strings,colors,themes}.xml
```

## طريقة التشغيل (Android Studio)
1. افتح مجلد `android-trading-journal` في Android Studio (Hedgehog أو أحدث).
2. انتظر مزامنة Gradle — يحتاج إنترنت أول مرة لتحميل المكتبات.
3. شغّل على محاكي أو جهاز حقيقي **Android 12+** (API 31+).
4. أنشئ حساباً من شاشة الدخول (أي بريد + كلمة مرور 6+ أحرف للنسخة التجريبية)، ثم جرّب: Dashboard → المساعد → خذ إشارة → حاسبة المخاطر → السجل → المحفظة.

> ملاحظة: لم أضع أي مفاتيح Binance أو كلمات مرور في الكود. مفاتيح التداول الحقيقية — لو طلبتها لاحقاً — تُدخل في التطبيق نفسه وتُحفظ في EncryptedSharedPreferences فقط.

## الخطوة القادمة منك
أخبرني بخطوات المساعد اللي وعدت بها (قواعد SMC Adamas الكاملة؟ رموز معينة؟ تنبيهات Push؟)، وسأركّبها في `TechnicalAnalysisEngine` + `AssistantScreen` مباشرة.
