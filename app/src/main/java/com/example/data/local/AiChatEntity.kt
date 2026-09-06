package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_chats")
data class AiChatEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val message: String,
  val sender: String, // "USER" or "AI"
  val timestamp: Long = System.currentTimeMillis(),
  val emotion: String? = null
)
