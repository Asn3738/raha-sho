package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
  @PrimaryKey val badgeId: String, // e.g. "sprout", "cypress", "willpower", "freedom"
  val titleFa: String,
  val descriptionFa: String,
  val iconType: String,
  val requiredDays: Int,
  val isUnlocked: Boolean = false,
  val unlockedDate: String = ""
)
