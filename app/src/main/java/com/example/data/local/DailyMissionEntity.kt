package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_missions")
data class DailyMissionEntity(
  @PrimaryKey val dayNumber: Int,
  val isWaterDone: Boolean = false,
  val isWalkDone: Boolean = false,
  val isBreathingDone: Boolean = false,
  val isJournalDone: Boolean = false,
  val isDailyExerciseDone: Boolean = false,
  val lastUpdatedTimestamp: Long = System.currentTimeMillis()
)
