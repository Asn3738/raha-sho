package com.example.features.rahaa_ai_engine.engine

import com.example.features.rahaa_ai_engine.domain.RecoveryContext

class SuggestionEngine {

  fun generateSuggestions(context: RecoveryContext): List<String> {
    val suggestions = mutableListOf<String>()

    if (context.craving >= 8) {
      suggestions.add("۱. از محیط محرک و خلوت فاصله بگیر")
      suggestions.add("۲. ۵ نفس آرام و عمیق بکش (دم ۴، بازدم ۶)")
      suggestions.add("۳. یک لیوان آب خنک بنوش")
    } else if (context.sleep < 4) {
      suggestions.add("امروز بدن تو خسته است.")
      suggestions.add("اولویت امروز: استراحت، آب کافی و فشار کاری کمتر.")
    } else if (context.energy < 4 && context.mood == "سخت") {
      suggestions.add("امروز فقط یک کار کوچک انجام بده.")
      suggestions.add("کوچک‌ترین قدم هم پیشرفت است.")
    } else {
      suggestions.add("مسیر ۳۰ روزه را با انگیزه ادامه بده.")
      suggestions.add("تمرین روزانه شکرگزاری و ورزش را مرور کن.")
    }

    return suggestions
  }
}
