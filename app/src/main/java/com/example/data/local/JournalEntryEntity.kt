package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val dateString: String, // e.g. "۱۴۰۳/۰۵/۱۰" or ISO
  val timestamp: Long = System.currentTimeMillis(),
  val mood: String, // "خیلی خوب", "خوب", "متوسط", "سخت"
  val urgeIntensity: Int, // 0 to 10
  val noteText: String,
  val successText: String,
  val sleepHours: Int = 7, // 0 to 10
  val energyLevel: Int = 7 // 0 to 10
)
