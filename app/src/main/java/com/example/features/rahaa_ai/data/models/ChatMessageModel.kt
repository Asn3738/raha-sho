package com.example.features.rahaa_ai.data.models

import com.example.features.rahaa_ai.domain.entities.ChatMessage

/**
 * Data Model DTO for Firebase Firestore or local database mapping.
 */
data class ChatMessageModel(
  val id: String = "",
  val text: String = "",
  val isUser: Boolean = false,
  val timestamp: Long = System.currentTimeMillis()
) {
  fun toDomain(): ChatMessage {
    return ChatMessage(
      id = id,
      text = text,
      isUser = isUser,
      timestamp = timestamp
    )
  }

  fun toJson(): Map<String, Any> {
    return mapOf(
      "id" to id,
      "text" to text,
      "isUser" to isUser,
      "timestamp" to timestamp
    )
  }

  companion object {
    fun fromJson(json: Map<String, Any?>): ChatMessageModel {
      return ChatMessageModel(
        id = json["id"] as? String ?: "",
        text = json["text"] as? String ?: "",
        isUser = json["isUser"] as? Boolean ?: false,
        timestamp = (json["timestamp"] as? Number)?.toLong() ?: System.currentTimeMillis()
      )
    }

    fun fromDomain(domain: ChatMessage): ChatMessageModel {
      return ChatMessageModel(
        id = domain.id,
        text = domain.text,
        isUser = domain.isUser,
        timestamp = domain.timestamp
      )
    }
  }
}
