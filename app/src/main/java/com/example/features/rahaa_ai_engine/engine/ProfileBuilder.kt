package com.example.features.rahaa_ai_engine.engine

import com.example.data.local.JournalEntryEntity
import com.example.data.local.UserProfileEntity
import com.example.features.rahaa_ai_engine.domain.AIRecoveryProfile
import com.example.features.rahaa_ai_engine.domain.RecoveryContext

class ProfileBuilder {

  fun build(
    profile: UserProfileEntity?,
    journal: JournalEntryEntity?
  ): AIRecoveryProfile {
    val userName = profile?.name?.ifBlank { "همدرد عزیز" } ?: "همدرد عزیز"
    val habitList = if (!profile?.habitType.isNullOrBlank()) listOf(profile!!.habitType) else listOf("عادت ناپسند گذشته")
    val duration = profile?.durationText?.ifBlank { "مشخص نشده" } ?: "مشخص نشده"
    val triggers = profile?.triggersText?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() }
      ?: listOf("استرس", "تنهایی")

    var challenge = "ساخت عادت جدید و حفظ انضباط"
    val tools = mutableListOf<String>()

    val cravingLevel = journal?.urgeIntensity ?: 2
    val sleepQuality = journal?.sleepHours ?: 7

    if (triggers.any { it.contains("تنهایی") }) {
      challenge = "مدیریت احساس تنهایی و ارتباط با خود"
      tools.add("تمرین ارتباط سالم")
    }

    if (cravingLevel >= 7) {
      challenge = "کنترل و غلبه بر موج وسوسه شدید"
      tools.add("تنفس هنگام وسوسه (Urge Surfing)")
    }

    if (sleepQuality < 5) {
      tools.add("تمرین تنظیم و آرام‌سازی قبل از خواب")
    }

    if (tools.isEmpty()) {
      tools.add("مدیتیشن روزانه")
      tools.add("تمرین تنفس ۴-۴-۶")
    }

    return AIRecoveryProfile(
      userName = userName,
      habits = habitList,
      duration = duration,
      triggers = triggers,
      mainChallenge = challenge,
      supportStyle = "همدلانه، صمیمی و قدم به قدم بدون قضاوت",
      recommendedTools = tools
    )
  }

  fun buildFromContext(context: RecoveryContext, name: String = "همدرد عزیز"): AIRecoveryProfile {
    val habitList = if (context.habit.isNotBlank()) listOf(context.habit) else listOf("عادت ناپسند گذشته")
    val duration = context.duration.ifBlank { "مشخص نشده" }
    val triggers = if (context.triggers.isNotEmpty()) context.triggers else listOf("استرس", "تنهایی")

    var challenge = "ساخت عادت جدید"
    val tools = mutableListOf<String>()

    if (triggers.any { it.contains("تنهایی") }) {
      challenge = "مدیریت احساس تنهایی"
      tools.add("تمرین ارتباط سالم")
    }

    if (context.craving >= 7) {
      challenge = "کنترل و غلبه بر موج وسوسه"
      tools.add("تنفس هنگام وسوسه (Urge Surfing)")
    }

    if (context.sleep < 5) {
      tools.add("تنظیم خواب")
    }

    if (tools.isEmpty()) {
      tools.add("تمرین تنفس ۴-۴-۶")
    }

    return AIRecoveryProfile(
      userName = name,
      habits = habitList,
      duration = duration,
      triggers = triggers,
      mainChallenge = challenge,
      supportStyle = "همدلانه و قدم به قدم",
      recommendedTools = tools
    )
  }
}
