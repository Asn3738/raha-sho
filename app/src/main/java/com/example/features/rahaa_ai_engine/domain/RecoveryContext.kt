package com.example.features.rahaa_ai_engine.domain

data class RecoveryContext(
  val habit: String = "",
  val duration: String = "",
  val triggers: List<String> = emptyList(),
  val cleanDays: Int = 0,
  val mood: String = "خوب",
  val craving: Int = 2,
  val sleep: Int = 7,
  val energy: Int = 7,
  val currentDay: Int = 1
)
