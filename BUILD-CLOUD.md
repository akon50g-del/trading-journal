# بناء الـ APK بدون Android Studio — طريقتان

البناء المحلي على جهاز Muse محظور حالياً (Gradle يحتاج اتصال TCP محلي مرفوض).
هاتان الطريقتان تبنيان نفس المشروع بدون أي إعداد إضافي.

## الطريقة 1 — GitHub Actions (سحابي، الأفضل لو ما عندك كمبيوتر)

1. اعمل حساب مجاني على https://github.com إن لم يكن عندك.
2. اعمل Repository جديد باسم `trading-journal` (Public أو Private).
3. ارفع كل ملفات مجلد `android-trading-journal` إلى الريبو
   (من صفحة الريبو: Add file → Upload files، أو من الكمبيوتر بـ git).
4. من الريبو افتح تبويب **Actions** ← اختر **Build APK** ← **Run workflow**.
5. انتظر 5–10 دقائق حتى تظهر علامة ✅ خضراء.
6. افتح آخر تشغيل (Run) ← انزل لأسفل لقسم **Artifacts** ← حمّل
   `trading-journal-debug` ← بداخله ملف `app-debug.apk`.
7. انقل الـ APK لموبايلك وثبّته (فعّل "تثبيت من مصادر غير معروفة" عند الطلب).

الملف المسؤول عن البناء: `.github/workflows/build-apk.yml` — موجود أصلاً في المشروع.

## الطريقة 2 — Android Studio على كمبيوتر

1. نزّل Android Studio من https://developer.android.com/studio وثبّته.
2. افتح مجلد `android-trading-journal` في Android Studio.
3. انتظر مزامنة Gradle (أول مرة تحتاج إنترنت).
4. من القائمة: Build → Build App Bundle(s) / APK(s) → Build APK(s).
5. الملف يظهر في: `app/build/outputs/apk/debug/app-debug.apk`
   — انقله للموبايل وثبّته.

## ملاحظات

- الـ APK هنا نسخة Debug جاهزة للتثبيت والتجربة، وليست نسخة متجر (Release).
- لا توجد أي مفاتيح أو كلمات مرور في المشروع.
- لو واجهت خطأ في البناء السحابي، انسخ رسالة الخطأ من تبويب Actions وأرسلها، وسأصلحها.
