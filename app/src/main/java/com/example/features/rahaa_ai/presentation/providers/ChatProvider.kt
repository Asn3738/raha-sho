package com.example.features.rahaa_ai.presentation.providers

import com.example.features.rahaa_ai.data.services.GeminiService
import com.example.features.rahaa_ai.domain.entities.ChatMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Chat Provider / State Holder managing AI Companion chat state.
 */
class ChatProvider(
  private val geminiService: GeminiService = GeminiService(),
  private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {

  private val _messages = MutableStateFlow<List<ChatMessage>>(
    listOf(
      ChatMessage(
        text = "سلام 🌿\nامروز کنار تو هستم. چه چیزی در قلبت می‌گذرد؟",
        isUser = false
      )
    )
  )
  val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

  private val _isThinking = MutableStateFlow(false)
  val isThinking: StateFlow<Boolean> = _isThinking.asStateFlow()

  fun sendMessage(userText: String) {
    if (userText.isBlank() || _isThinking.value) return

    val userMessage = ChatMessage(text = userText, isUser = true)
    _messages.value = _messages.value + userMessage
    _isThinking.value = true

    coroutineScope.launch {
      try {
        val aiReply = geminiService.sendMessage(userText)
        val aiMessage = ChatMessage(text = aiReply, isUser = false)
        _messages.value = _messages.value + aiMessage
      } catch (e: Exception) {
        val errorMessage = ChatMessage(
          text = "من کنار تو هستم 🌿 یک نفس عمیق بکش، تو در این مسیر تنها نیستی.",
          isUser = false
        )
        _messages.value = _messages.value + errorMessage
      } finally {
        _isThinking.value = false
      }
    }
  }

  fun triggerEmergencyUrge() {
    sendMessage("🆘 الان وسوسه دارم")
  }

  fun clearHistory() {
    _messages.value = listOf(
      ChatMessage(
        text = "سلام 🌿\nتاریخچه گفتگو پاکسازی شد. من آماده همدردی و همراهی مجدد با تو هستم.",
        isUser = false
      )
    )
  }
}
