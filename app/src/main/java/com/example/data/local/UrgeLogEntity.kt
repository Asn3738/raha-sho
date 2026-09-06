package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "urge_logs")
data class UrgeLogEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val timestamp: Long = System.currentTimeMillis(),
  val dateString: String,
  val durationSeconds: Int,
  val passedSuccessfully: Boolean,
  val note: String = ""
)
