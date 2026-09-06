package com.example.features.profile.domain.entities

import java.util.concurrent.TimeUnit

/**
 * Domain entity representing the user's complete profile and recovery pathway in Rahaa Sho.
 */
data class UserProfile(
  val uid: String = "guest_user",
  val name: String = "کاربر عزیز",
  val email: String = "",
  val age: Int = 25,
  val startDateTimestamp: Long = System.currentTimeMillis(),
  val goal: String = "ترک کامل", // ترک کامل, کاهش مصرف, بازسازی زندگی
  val substances: List<String> = emptyList(),
  val behaviors: List<String> = emptyList(),
  val durationText: String = "۶ ماه تا ۲ سال",
  val intensityText: String = "تقریباً هر روز",
  val triggers: List<String> = emptyList(),
  val triggersText: String = "",
  val previousAttempt: String = "خیر",
  val relapseReason: String = "",
  val motivationText: String = "سلامتی، آرامش ذهن و خانواده",
  val currentDay: Int = 1,
  val totalPoints: Int = 1250,
  val isLoggedIn: Boolean = false,
  val isSetupCompleted: Boolean = false,
  val showSpiritualContent: Boolean = true,
  val aiCompanionEnabled: Boolean = true,
  val notificationsEnabled: Boolean = true,
  val darkModeEnabled: Boolean = false
) {
  val cleanDays: Int
    get() {
      val diffMillis = System.currentTimeMillis() - startDateTimestamp
      val days = TimeUnit.MILLISECONDS.toDays(diffMillis).toInt()
      return if (days >= 0) days else 0
    }

  fun toJson(): Map<String, Any> {
    return mapOf(
      "uid" to uid,
      "name" to name,
      "email" to email,
      "age" to age,
      "startDateTimestamp" to startDateTimestamp,
      "goal" to goal,
      "substances" to substances,
      "behaviors" to behaviors,
      "durationText" to durationText,
      "intensityText" to intensityText,
      "triggers" to triggers,
      "previousAttempt" to previousAttempt,
      "relapseReason" to relapseReason,
      "motivationText" to motivationText,
      "currentDay" to currentDay,
      "totalPoints" to totalPoints,
      "isLoggedIn" to isLoggedIn,
      "isSetupCompleted" to isSetupCompleted,
      "showSpiritualContent" to showSpiritualContent,
      "aiCompanionEnabled" to aiCompanionEnabled,
      "notificationsEnabled" to notificationsEnabled,
      "darkModeEnabled" to darkModeEnabled
    )
  }

  companion object {
    fun fromJson(json: Map<String, Any?>): UserProfile {
      @Suppress("UNCHECKED_CAST")
      val substancesList = json["substances"] as? List<String> ?: emptyList()
      @Suppress("UNCHECKED_CAST")
      val behaviorsList = json["behaviors"] as? List<String> ?: emptyList()
      @Suppress("UNCHECKED_CAST")
      val triggersList = json["triggers"] as? List<String> ?: emptyList()

      return UserProfile(
        uid = json["uid"] as? String ?: "guest_user",
        name = json["name"] as? String ?: "کاربر عزیز",
        email = json["email"] as? String ?: "",
        age = (json["age"] as? Number)?.toInt() ?: 25,
        startDateTimestamp = (json["startDateTimestamp"] as? Number)?.toLong() ?: System.currentTimeMillis(),
        goal = json["goal"] as? String ?: "ترک کامل",
        substances = substancesList,
        behaviors = behaviorsList,
        durationText = json["durationText"] as? String ?: "۶ ماه تا ۲ سال",
        intensityText = json["intensityText"] as? String ?: "تقریباً هر روز",
        triggers = triggersList,
        previousAttempt = json["previousAttempt"] as? String ?: "خیر",
        relapseReason = json["relapseReason"] as? String ?: "",
        motivationText = json["motivationText"] as? String ?: "سلامتی و آینده روشن",
        currentDay = (json["currentDay"] as? Number)?.toInt() ?: 1,
        totalPoints = (json["totalPoints"] as? Number)?.toInt() ?: 1250,
        isLoggedIn = json["isLoggedIn"] as? Boolean ?: false,
        isSetupCompleted = json["isSetupCompleted"] as? Boolean ?: false,
        showSpiritualContent = json["showSpiritualContent"] as? Boolean ?: true,
        aiCompanionEnabled = json["aiCompanionEnabled"] as? Boolean ?: true,
        notificationsEnabled = json["notificationsEnabled"] as? Boolean ?: true,
        darkModeEnabled = json["darkModeEnabled"] as? Boolean ?: false
      )
    }
  }
}
