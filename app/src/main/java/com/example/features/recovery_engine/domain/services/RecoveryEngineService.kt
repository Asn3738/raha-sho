package com.example.features.recovery_engine.domain.services

import com.example.data.local.UserProfileEntity
import com.example.features.profile.domain.entities.UserProfile
import com.example.features.recovery_engine.domain.entities.RecoveryStrategy

/**
 * Intelligent Engine analyzing user recovery profile to produce customized guidance,
 * daily exercises, therapy priorities, and rich prompt context for Yar-e Rahaa AI.
 */
object RecoveryEngineService {

  fun generateStrategy(entity: UserProfileEntity): RecoveryStrategy {
    val profile = UserProfile(
      name = entity.name,
      age = entity.age,
      substances = if (entity.habitType.isNotBlank()) listOf(entity.habitType) else emptyList(),
      durationText = entity.durationText,
      intensityText = entity.intensityText,
      triggersText = entity.triggersText,
      triggers = if (entity.triggersText.isNotBlank()) entity.triggersText.split(", ") else emptyList(),
      previousAttempt = entity.previousAttempt,
      relapseReason = entity.relapseReason,
      goal = entity.changeReason,
      motivationText = entity.motivationText,
      currentDay = entity.currentDay
    )
    return generateStrategy(profile)
  }

  fun generateStrategy(profile: UserProfile): RecoveryStrategy {
    val habits = profile.substances.ifEmpty { listOf(profile.goal) }
    val duration = profile.durationText
    val intensity = profile.intensityText
    val triggers = profile.triggers
    val triggersText = profile.triggersText
    val goal = profile.goal
    val age = profile.age
    val motivation = profile.motivationText

    val priorities = mutableListOf<String>()
    val exercises = mutableListOf<String>()
    val tips = mutableListOf<String>()

    var focus = "شروع تغییر و ساخت زندگی پاک"

    // 1. Duration Analysis
    val isLongTerm = duration.contains("۵") || duration.contains("بیشتر") || duration.contains("چند سال")
    if (isLongTerm) {
      priorities.add("بازسازی عمیق سبک زندگی و اصلاح ساعت زیستی")
      priorities.add("پیشگیری هوشمند از بازگشت و لغزش‌های تدریجی")
      exercises.add("تنظیم دقیق چرخه خواب و بیداری روزانه")
      exercises.add("ثبت کارنامه پاکی و پاداش‌های کوچک به خود")
      tips.add("درگیری‌های طولانی‌مدت نیاز به صبوری دارند؛ مغز شما قدم به قدم در حال ترمیم گیرنده‌های دوپامین است.")
    } else {
      priorities.add("شناخت دقیق محرک‌های اولیه و قطع الگوی عادت")
      exercises.add("تمرین قطع لحظه‌ای وسوسه و تکنیک موج‌سواری (Urge Surfing)")
      tips.add("چون مدت درگیری کمتر بوده، با بازسازی سریع عادات سالم، نتایج پاکی را بسیار زود حس خواهید کرد.")
    }

    // 2. Intensity Analysis
    val isDaily = intensity.contains("روز") || intensity.contains("شدید") || intensity.contains("هر روز")
    if (isDaily) {
      priorities.add("مدیریت فوری وسوسه و کنترل ولع (Craving Control)")
      exercises.add("تمرین توقف ۵ دقیقه‌ای (۵-Minute Pause)")
      exercises.add("استفاده از دکمه اورژانس وسوسه در لحظات بحرانی")
      tips.add("در الگوی مصرف روزانه، ۵ دقیقه اول هجوم وسوسه بحرانی‌ترین زمان است؛ تکنیک توقف و تنفس عمیق را اجرا کنید.")
    } else {
      priorities.add("شناسایی موقعیت‌های پرخطر هفتگی و پایان هفته")
      exercises.add("برنامه‌ریزی شفاف برای ساعات فراغت و روزهای تعطیل")
    }

    // 3. Trigger Analysis
    if (triggersText.contains("تنهایی") || triggers.contains("تنهایی")) {
      priorities.add("ساخت شبکه حمایت اجتماعی و ارتباط با افراد امن")
      exercises.add("ارتباط روزانه یا گفتگو با یک دوست یا همدرد امن")
      tips.add("تنهایی بزرگ‌ترین سوخت وسوسه است؛ حضور در جمع‌های سالم و صحبت با یار رها مانع انزوا می‌شود.")
    }

    if (triggersText.contains("استرس") || triggers.contains("استرس")) {
      priorities.add("آرام‌سازی جسمانی و کنترل هیجانات شدید (Stress Resilience)")
      exercises.add("تمرین تنفس مربعی ۴-۴-۴-۴ و ریلکسیشن عضلانی")
      tips.add("استرس، سیستم دفاعی ذهن را تضعیف می‌کند؛ با تنفس عمیق ۵ دقیقه‌ای، سطح کورتیزول را کاهش دهید.")
    }

    if (triggersText.contains("غم") || triggersText.contains("افکار") || triggersText.contains("ناامیدی")) {
      priorities.add("مدیریت خلق و نوشتن احساسات ناخوشایند")
      exercises.add("نوشتن ۵ دقیقه احساسات روزانه در دفترچه خاطرات رها شو")
    }

    if (triggersText.contains("بی‌خوابی") || triggersText.contains("خواب")) {
      priorities.add("بهداشت خواب و آرام‌سازی قبل از خواب")
      exercises.add("استفاده از بخش صوت‌های آرامش‌بخش (Calm) نیم ساعت قبل خواب")
    }

    // 4. Goal Analysis
    if (goal.contains("پاکی کامل") || goal.contains("ترک کامل")) {
      focus = "پاکی کامل و شروع فصل جدید زندگی"
    } else if (goal.contains("کنترل") || goal.contains("کاهش")) {
      focus = "مدیریت رفتار و تسلط بر خود"
    } else {
      focus = "بازسازی کیفیت زندگی و رشد فردی"
    }

    // Default exercises fallback
    if (exercises.isEmpty()) {
      exercises.add("۱۰ دقیقه پیاده‌روی یا ورزش سبک روزانه")
      exercises.add("۳ بار تنفس عمیق شکمی")
    }

    // AI Context Prompt formatting
    val aiContextPrompt = """
      شما «یار رها»، همراه درمانی و هوشمند کاربر در اپلیکیشن «رها شو» هستید.
      
      پروفایل روانشناختی و مسیر پاکی کاربر:
      - نام کاربر: ${profile.name} (سن: $age)
      - نوع عادات / مواد: ${habits.joinToString("، ")}
      - مدت زمان درگیری: $duration
      - الگوی مصرف / شدت: $intensity
      - تحریک‌کننده‌های اصلی (Triggers): ${if (triggersText.isNotBlank()) triggersText else "مشخص نشده"}
      - تجربه قبلی ترک: ${profile.previousAttempt} ${if (profile.relapseReason.isNotBlank()) " (علت بازگشت: ${profile.relapseReason})" else ""}
      - انگیزه و هدف اصلی: $motivation / $goal
      - روزهای پاکی فعلی: ${profile.cleanDays} روز
      
      راهنمای گفتگو و رفتار یار رها:
      ۱. با نام ${profile.name} به گرمی و بدون قضاوت صحبت کن.
      ۲. در پاسخ‌ها دائماً به انگیزه‌های او ($motivation) و محرک‌های حساسش اشاره کن.
      ۳. اگر کاربر احساس وسوسه داشت، تمرین‌های اختصاصی او (مانند ${exercises.take(2).joinToString(" یا ")}) را پیشنهاد بده.
      ۴. لحن شما امیدبخش، علم‌محور، همدلانه و متمرکز بر عمل باشد.
    """.trimIndent()

    return RecoveryStrategy(
      focus = focus,
      priorities = priorities.distinct(),
      dailyExercises = exercises.distinct(),
      personalizedTips = tips.distinct(),
      aiContextPrompt = aiContextPrompt
    )
  }
}
