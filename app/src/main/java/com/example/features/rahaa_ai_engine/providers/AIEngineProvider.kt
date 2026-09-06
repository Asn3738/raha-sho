package com.example.features.rahaa_ai_engine.providers

import com.example.data.local.JournalEntryEntity
import com.example.data.local.UserProfileEntity
import com.example.features.rahaa_ai_engine.data.AIPromptBuilder
import com.example.features.rahaa_ai_engine.domain.AIDecision
import com.example.features.rahaa_ai_engine.domain.AIRecoveryProfile
import com.example.features.rahaa_ai_engine.domain.RecoveryContext
import com.example.features.rahaa_ai_engine.engine.ProfileBuilder
import com.example.features.rahaa_ai_engine.engine.RecoveryEngine
import com.example.features.rahaa_ai_engine.engine.SuggestionEngine

object AIEngineProvider {

  private val engine = RecoveryEngine()
  private val suggestionEngine = SuggestionEngine()
  private val profileBuilder = ProfileBuilder()

  fun buildContext(profile: UserProfileEntity?, journal: JournalEntryEntity?): RecoveryContext {
    val triggers = profile?.triggersText?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList()
    val cleanDaysCount = profile?.let {
      ((System.currentTimeMillis() - it.startDateTimestamp) / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(0)
    } ?: 0

    return RecoveryContext(
      habit = profile?.habitType ?: "",
      duration = profile?.durationText ?: "",
      triggers = triggers,
      cleanDays = cleanDaysCount,
      mood = journal?.mood ?: "خوب",
      craving = journal?.urgeIntensity ?: 2,
      sleep = journal?.sleepHours ?: 7,
      energy = journal?.energyLevel ?: 7,
      currentDay = profile?.currentDay ?: 1
    )
  }

  fun buildAIProfile(profile: UserProfileEntity?, journal: JournalEntryEntity?): AIRecoveryProfile {
    return profileBuilder.build(profile, journal)
  }

  fun process(context: RecoveryContext): AIDecision {
    return engine.process(context)
  }

  fun getSuggestions(context: RecoveryContext): List<String> {
    return suggestionEngine.generateSuggestions(context)
  }

  fun buildPrompt(context: RecoveryContext): String {
    return AIPromptBuilder.build(context)
  }

  fun buildPromptFromProfile(profile: AIRecoveryProfile, userMessage: String): String {
    return AIPromptBuilder.create(profile, userMessage)
  }
}

