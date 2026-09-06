package com.example.features.rahaa_ai_engine.engine

import com.example.features.rahaa_ai_engine.domain.AIDecision
import com.example.features.rahaa_ai_engine.domain.RecoveryContext
import com.example.features.rahaa_ai_engine.domain.RiskLevel

class RecoveryEngine(
  private val analyzer: RiskAnalyzer = RiskAnalyzer()
) {

  fun process(context: RecoveryContext): AIDecision {
    val risk = analyzer.analyze(context)

    return when (risk) {
      RiskLevel.EMERGENCY -> AIDecision(
        level = risk,
        message = """
          من کنار تو هستم.
          الان لازم نیست با آینده بجنگی؛
          فقط همین چند دقیقه را مدیریت کنیم.
        """.trimIndent(),
        action = "از محیط محرک فاصله بگیر",
        exercise = "تنفس ۴-۷-۸",
        meditation = "آرام‌سازی فوری ۳ دقیقه‌ای"
      )

      RiskLevel.HIGH -> AIDecision(
        level = risk,
        message = """
          وسوسه یک موج است.
          می‌آید و می‌رود.
          بیا این موج را با هم رد کنیم.
        """.trimIndent(),
        action = "یک فعالیت جایگزین انجام بده",
        exercise = "پیاده‌روی کوتاه",
        meditation = "تمرکز روی تنفس"
      )

      RiskLevel.MEDIUM -> AIDecision(
        level = risk,
        message = """
          امروز مراقبت از خودت مهم است.
          یک قدم کوچک بردار.
        """.trimIndent(),
        action = "تمرین روزانه را انجام بده",
        exercise = "حرکات کششی",
        meditation = "مدیتیشن آرام"
      )

      RiskLevel.LOW -> AIDecision(
        level = risk,
        message = """
          عالی پیش می‌روی.
          امروز روی ساختن زندگی جدید تمرکز کن.
        """.trimIndent(),
        action = "ادامه مسیر ۳۰ روزه",
        exercise = "ورزش سبک",
        meditation = "شکرگزاری"
      )
    }
  }
}
