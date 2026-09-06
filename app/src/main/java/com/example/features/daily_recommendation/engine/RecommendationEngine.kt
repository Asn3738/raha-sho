package com.example.features.daily_recommendation.engine

import com.example.features.daily_recommendation.domain.DailyRecommendation
import com.example.features.rahaa_ai_engine.domain.RecoveryContext

class RecommendationEngine {

  fun generate(context: RecoveryContext): DailyRecommendation {
    var meditation = "مدیتیشن تنفس آرام ۵ دقیقه‌ای"
    var exercise = "پیاده‌روی آرام ۱۰ دقیقه"
    var lesson = "شناخت افکار و مدیریت احساسات"
    var action = "یک کار کوچک مفید انجام بده"
    var motivation = "هر قدم کوچک، تو را قوی‌تر و آزادتر می‌کند"
    var reason = "بر اساس وضعیت عمومی امروز تو"

    if (context.sleep < 4) {
      meditation = "آرام‌سازی کامل بدن قبل از خواب"
      exercise = "حرکات کششی سبک و نرمش گردن"
      reason = "خواب کم نیاز به مراقبت و آرامش بیشتر دارد"
      motivation = "امروز بدن تو خسته است؛ استراحت کافی اولویت اصلی است"
    }

    if (context.craving >= 7) {
      meditation = "مدیریت و سوار شدن بر موج وسوسه (Urge Surfing)"
      exercise = "تغییر سریع محیط، پیاده‌روی کوتاه یا آب سرد"
      action = "۱۰ دقیقه تأخیر ایجاد کن و آب خنک بنوش"
      reason = "سطح وسوسه امروز بالاتر است؛ تمرکز روی پیشگیری هوشمندانه"
      motivation = "وسوسه فقط یک موج کوتاه‌مدت است و فروکش می‌کند"
    } else if (context.energy < 5) {
      exercise = "۳ دقیقه حرکات ساده و کششی بدن"
      action = "فقط یک قدم بسیار کوچک برداشتن"
      reason = "سطح انرژی پایین است؛ گام‌های کوچک بردار"
    } else if (context.cleanDays >= 10) {
      motivation = "شما در حال تثبیت عادات جدید در روز ${context.cleanDays} پاکی هستید 🌱"
      reason = "پیشرفت عالی در روزهای پاکی و تثبیت انضباط"
    }

    return DailyRecommendation(
      spiritual = "امروز را با توکل، امید و عهد جدید آغاز کن.",
      meditation = meditation,
      exercise = exercise,
      lesson = lesson,
      action = action,
      motivation = motivation,
      reason = reason
    )
  }
}
