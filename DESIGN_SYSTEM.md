# Rahaa Sho Design System Specification (سند دیزاین سیستم «رها شو»)

**نام دیزاین سیستم:** Rahaa Sho Design System  
**نسخه:** 1.0.0  
**هویت و سبک:** Modern Persian Luxury (طراحی فاخر ایرانی)  
**ارزش‌های بصری:** رهایی، آرامش، رشد، قدرت، اصالت  
**الهام‌بخش:** معماری باغ ایرانی، سرو ایرانی، کاشی فیروزه‌ای، طلوع خورشید، هندسه اسلامی  

---

## ۱. پالت رنگ‌ها و توکن‌های برند (Color System Tokens)

### الف) رنگ‌های اصلی برند (Brand Palette)

| نام توکن | کد Hex | معادل فارسی | کاربرد اصلی |
|---|---|---|---|
| `IranianTurquoise` | `#16B8B0` | فیروزه‌ای ایرانی | رنگ اصلی برند (Primary)، دکمه‌ها، آیکون‌های فعال، نوار پیشرفت |
| `IranianTurquoiseDark` | `#0F8781` | فیروزه‌ای تیره | حالت Press/Focus دکمه‌ها، متون روی پس‌زمینه فیروزه‌ای روشن |
| `IranianTurquoiseLight` | `#53E3DC` | فیروزه‌ای روشن | هایلایت‌ها در تم تاریک، کارت‌های شناور |
| `IranianTurquoiseContainerLight` | `#E0F7F6` | کانتینر فیروزه‌ای روشن | پس‌زمینه کارت‌های مأموریت، برچسب‌های ویژه |
| `PersianLapisBlue` | `#2457A6` | لاجوردی ایرانی | رنگ ثانویه (Secondary)، دکمه‌های دوم، کارت‌های آموزشی |
| `CypressGreen` | `#2E8B57` | سبز سرو | نشان‌های موفقیت، نشانگر روزهای پاکی، وضعیت تکمیل |
| `SoftSunGold` | `#D9A441` | طلایی خورشید | اکستنت، دستاوردها، اشتراک Premium، ستاره‌های امتیاز |
| `UrgeAlertRed` | `#DC2626` | قرمز اورژانس | دکمه وسوسه (Emergency Button)، هشدارهای سیستم |

### ب) رنگ‌های خنثی و تم‌ها (Neutral & Surface System)

#### تم روشن (Light Theme — Warm Persian Garden)
- **Background (`WarmWhiteBackground`):** `#FAF7F0` (سفید گرم)
- **Surface (`SurfaceWhite`):** `#FFFFFF` (سفید خالص)
- **Surface Variant (`SurfaceVariantLight`):** `#F1EFE8` (کرم روشن)
- **Text Primary (`OnBackgroundLight`):** `#1E293B` (سورمه‌ای تیره)
- **Text Secondary (`OnSurfaceSecondaryLight`):** `#64748B` (خاکستری متوسط)

#### تم تاریک (Dark Theme — Deep Persian Navy)
- **Background (`DeepPersianNavyBackground`):** `#101827` (سورمه‌ای عمیق شب)
- **Surface (`SurfaceDarkNavy`):** `#172554` (آبی تیره)
- **Surface Variant (`SurfaceVariantDarkNavy`):** `#1E293B` (سورمه‌ای خنثی)
- **Text Primary (`OnBackgroundDark`):** `#F8FAFC` (سفید مایل به آبی)
- **Text Secondary (`OnSurfaceSecondaryDark`):** `#94A3B8` (خاکستری روشن)

---

## ۲. تایپوگرافی (Typography System)

فونت رسمی دیزاین سیستم: **وزیرمتن (Vazirmatn)**

| سطح تایپوگرافی | وزن (Weight) | اندازه (Size) | ارتفاع خط (Line Height) |
|---|---|---|---|
| **Display Large** | Bold (700) | 32sp | 40sp |
| **Display Medium** | Bold (700) | 26sp | 34sp |
| **Title Large** | Bold (700) | 22sp | 30sp |
| **Title Medium** | SemiBold (600) | 18sp | 26sp |
| **Title Small** | Medium (500) | 16sp | 24sp |
| **Body Large** | Regular (400) | 16sp | 24sp |
| **Body Medium** | Regular (400) | 14sp | 22sp |
| **Label Large** | SemiBold (600) | 15sp | 20sp |
| **Label Medium** | Medium (500) | 12sp | 16sp |

---

## ۳. سیستم فواصل (Spacing System — 8pt Grid)

تمام المان‌های UI بر اساس شبکه استاندارد 4/8dp جانمایی می‌شوند:
- `SpaceXS` = **4.dp** (فواصل بسیار کوچک متون)
- `SpaceS` = **8.dp** (فاصله بین آیکون و متن)
- `SpaceM` = **12.dp** (پدینگ داخلی چیپ‌ها)
- `SpaceL` = **16.dp** (پدینگ استاندارد کارت‌ها و لبه‌ها)
- `SpaceXL` = **24.dp** (فاصله بین بخش‌های اصلی)
- `Space2XL` = **32.dp** (فاصله عنوان تا فرم)
- `Space3XL` = **48.dp** (حداقل اندازه هدف لمسی / Touch Target)

---

## ۴. سیستم انحنا و گوشه‌ها (Border Radius Tokens)

- `RadiusSmall` = **8.dp** (چیپ‌ها، دکمه‌های کوچک، بدنه چت)
- `RadiusMedium` = **16.dp** (فیلدهای ورودی، کارت‌های متنی)
- `RadiusLarge` = **24.dp** (دکمه‌های اصلی، کارت‌های داشبورد)
- `RadiusExtraLarge` = **28.dp** / **32.dp** (کارت‌های فاخر ایرانی، مدال‌ها)
- `RadiusFull` = **999.dp** (دکمه‌های دایره‌ای، آواتارها)

---

## ۵. سیستم سایه و عمق (Elevation System)

- **Level 0 (Flat):** 0.dp — بدون سایه
- **Level 1 (Subtle Card):** 1.dp الی 2.dp — کارت‌های لیست روزانه
- **Level 2 (Prominent Card):** 4.dp الی 6.dp — کارت پیشرفت اصلی، دکمه‌های اصلی
- **Level 3 (Modal / Emergency):** 8.dp الی 12.dp — دکمه اورژانس وسوسه، دیالوگ‌های شناور

---

## ۶. کتابخانه کامپوننت‌ها (Component Library Mapping)

| نام کامپوننت | مشخصات بصری | فایل پیاده‌سازی |
|---|---|---|
| `CustomButton` | ارتفاع 56dp، انحنای 24dp، سایه نرم، فونت SemiBold | `RahaaComponents.kt` |
| `EmergencyButton` | قرمز اورژانس، انیمیشن Pulse، انحنای 24dp | `RahaaComponents.kt` |
| `PersianCard` | انحنای 28dp، حاشیه 1dp مایل به فیروزه‌ای | `RahaaComponents.kt` |
| `ProgressCircle` | نوار دایره‌ای با انیمیشن Smooth Sweep | `RahaaComponents.kt` |
| `DailyMissionCard` | چک‌باکس تخصصی، تغییر رنگ پس‌زمینه در حالت تکمیل | `RahaaComponents.kt` |
| `AchievementBadge` | دایره طلایی، آیکون سرو/جوانه، برچسب دریافت | `RahaaComponents.kt` |
| `JournalCard` | حاشیه رنگی بر اساس حس (سبز/فیروزه‌ای/طلایی/قرمز) | `RahaaComponents.kt` |
| `ThemeSelector` | دکمه سوئیچ ۳ حالته (روشن / تاریک / سیستم) | `RahaaComponents.kt` |

---

## ۷. راهنمای فریم‌ورک‌ها (Flutter & Jetpack Compose Alignment)

### ساختار پوشه‌های Flutter پیشنهادی
```
lib/
 ├── core/
 │    ├── theme/
 │    │    ├── app_colors.dart
 │    │    ├── app_typography.dart
 │    │    └── app_theme.dart
 │    └── widgets/
 │         ├── custom_button.dart
 │         ├── persian_card.dart
 │         └── progress_circle.dart
```

### ساختار Jetpack Compose موجود
```
app/src/main/java/com/example/ui/
 ├── theme/
 │    ├── Color.kt
 │    ├── Type.kt
 │    └── Theme.kt
 └── components/
      └── RahaaComponents.kt
```

---
*سند رسمی دیزاین سیستم «رها شو» — آماده جهت استفاده در Figma، Flutter و Jetpack Compose.*
