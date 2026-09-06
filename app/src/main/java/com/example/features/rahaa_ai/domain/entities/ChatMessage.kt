package com.example.features.rahaa_ai.domain.entities

import java.util.Date

/**
 * Domain entity representing a chat message in Yar-e Rahaa.
 */
data class ChatMessage(
  val id: String = "",
  val text: String,
  val isUser: Boolean,
  val timestamp: Long = System.currentTimeMillis()
) {
  val timeFormatted: String
    get() {
      val formatter = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
      return formatter.format(Date(timestamp))
    }
}
