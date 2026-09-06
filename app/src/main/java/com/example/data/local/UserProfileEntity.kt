package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
  @PrimaryKey val id: Int = 1,
  val name: String = "کاربر عزیز",
  val email: String = "",
  val authMethod: String = "GUEST", // EMAIL, MOBILE, GOOGLE, GUEST
  val isLoggedIn: Boolean = false,
  val age: Int = 25,
  val habitType: String = "ترک عادتهای مخرب",
  val changeReason: String = "ارتقای کیفیت زندگی و بازگشت به خود واقعی",
  val durationText: String = "۶ ماه تا ۲ سال",
  val intensityText: String = "تقریباً هر روز",
  val triggersText: String = "تنهایی، استرس",
  val previousAttempt: String = "خیر",
  val relapseReason: String = "",
  val motivationText: String = "سلامتی، آرامش ذهن و خانواده",
  val startDateTimestamp: Long = System.currentTimeMillis(),
  val currentDay: Int = 1,
  val totalPoints: Int = 150,
  val isSetupCompleted: Boolean = false,
  val notificationsEnabled: Boolean = true,
  val themeModeName: String = "SYSTEM",
  val isPremium: Boolean = false,
  val subscriptionPlan: String = "رایگان 🌿",
  val subscriptionExpiry: String = ""
)

val UserProfileEntity?.isAdmin: Boolean
  get() {
    if (this == null) return false
    val clean = this.name.trim().lowercase()
    return clean == "الله اکبر" || clean.contains("الله اکبر")
  }
