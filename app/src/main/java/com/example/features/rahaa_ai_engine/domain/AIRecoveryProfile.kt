package com.example.features.rahaa_ai_engine.domain

data class AIRecoveryProfile(
  val userName: String,
  val habits: List<String>,
  val duration: String,
  val triggers: List<String>,
  val mainChallenge: String,
  val supportStyle: String,
  val recommendedTools: List<String>
) {
  fun toMap(): Map<String, Any> {
    return mapOf(
      "userName" to userName,
      "habits" to habits,
      "duration" to duration,
      "triggers" to triggers,
      "mainChallenge" to mainChallenge,
      "supportStyle" to supportStyle,
      "recommendedTools" to recommendedTools
    )
  }
}
