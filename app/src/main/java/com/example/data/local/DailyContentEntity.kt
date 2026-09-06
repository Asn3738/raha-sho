package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_contents")
data class DailyContentEntity(
  @PrimaryKey val dayNumber: Int,
  val phaseNumber: Int, // 1 to 4
  val phaseName: String, // "شروع دوباره", "قدرت ذهن", "ساخت شخصیت جدید", "زندگی جدید"
  val titleFa: String,
  val subtitleFa: String,
  val educationalText: String,
  val exerciseText: String,
  val isCompleted: Boolean = false,
  val userReflection: String = "",
  val pointsAwarded: Int = 50,

  // --- 6 Rich Sections for 30-Day Recovery Journey ---
  // Section 1: 🌱 پیام‌های طلوع (Sunrise Quotes)
  val quote1Text: String = "",
  val quote1Meaning: String = "",
  val quote2Text: String = "",
  val quote2Meaning: String = "",
  val quote3Text: String = "",
  val quote3Meaning: String = "",

  // Section 2: 📖 داستان امروز (Today's Story)
  val storyTitle: String = "",
  val storyContent: String = "",
  val storyMainMessage: String = "",

  // Section 3: 🧠 مقاله کوتاه (Short Article)
  val articleTitle: String = "",
  val articleKeyTakeaway: String = "",

  // Section 4: 🎵 شعر امروز (Today's Poem)
  val poemText: String = "",
  val poemMeaning: String = "",

  // Section 6: 🤝 پیام همراه رها (Raha Companion Message)
  val supportMessage: String = ""
)

