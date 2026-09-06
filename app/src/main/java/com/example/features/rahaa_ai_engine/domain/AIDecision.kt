package com.example.features.rahaa_ai_engine.domain

data class AIDecision(
  val level: RiskLevel,
  val message: String,
  val action: String,
  val exercise: String,
  val meditation: String
)
