package com.example.data.repository

import com.example.data.local.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class RahaaRepository(private val dao: RahaaDao) {

  private val aiService = com.example.data.ai.AIService()

  val userProfile: Flow<UserProfileEntity?> = dao.getUserProfileFlow()
  val allDailyContents: Flow<List<DailyContentEntity>> = dao.getAllDailyContentsFlow()
  val allJournalEntries: Flow<List<JournalEntryEntity>> = dao.getAllJournalEntriesFlow()
  val allUrgeLogs: Flow<List<UrgeLogEntity>> = dao.getAllUrgeLogsFlow()
  val allAchievements: Flow<List<AchievementEntity>> = dao.getAllAchievementsFlow()
  val allAiChats: Flow<List<AiChatEntity>> = dao.getAllAiChatsFlow()

  fun getDailyMission(dayNumber: Int): Flow<DailyMissionEntity?> = dao.getDailyMissionFlow(dayNumber)
  fun getDailyContent(dayNumber: Int): Flow<DailyContentEntity?> = dao.getDailyContentFlow(dayNumber)

  suspend fun initializeSeedDataIfNeeded() {
    // Seed User Profile
    val profile = dao.getUserProfileDirect()
    if (profile == null) {
      dao.insertOrUpdateUserProfile(UserProfileEntity())
    }

    // Seed Achievements
    val achievements = dao.getAllAchievementsFlow().firstOrNull()
    if (achievements.isNullOrEmpty()) {
      dao.insertAllAchievements(
        listOf(
          AchievementEntity(
            badgeId = "sprout",
            titleFa = "🌱 جوانه دوباره",
            descriptionFa = "تصمیم بزرگ برای شروع مسیر رهایی و بازگشت به خود واقعی.",
            iconType = "sprout",
            requiredDays = 1,
            isUnlocked = true,
            unlockedDate = "امروز"
          ),
          AchievementEntity(
            badgeId = "cypress",
            titleFa = "🌿 سرو استوار",
            descriptionFa = "پایداری و عبور موفق از ۷ روز اول مسیر.",
            iconType = "cypress",
            requiredDays = 7,
            isUnlocked = false
          ),
          AchievementEntity(
            badgeId = "willpower",
            titleFa = "🔥 قدرت اراده",
            descriptionFa = "عبور از نیمه راه (۱۵ روز) و غلبه بر وسوسه‌ها.",
            iconType = "willpower",
            requiredDays = 15,
            isUnlocked = false
          ),
          AchievementEntity(
            badgeId = "freedom",
            titleFa = "🕊 پرواز رهایی",
            descriptionFa = "تکمیل کامل دوره ۳۰ روزه و تولد دوباره.",
            iconType = "freedom",
            requiredDays = 30,
            isUnlocked = false
          )
        )
      )
    }

    // Seed 30 Days of Content
    val contents = dao.getAllDailyContentsFlow().firstOrNull()
    if (contents.isNullOrEmpty() || contents.firstOrNull()?.quote1Text.isNullOrBlank()) {
      val seedContents = RecoveryJourneyData.get30DaysRecoveryData()
      dao.insertAllDailyContents(seedContents)
    }

    // Seed Daily Mission for day 1 if null
    val mission1 = dao.getDailyMissionFlow(1).firstOrNull()
    if (mission1 == null) {
      dao.insertOrUpdateDailyMission(DailyMissionEntity(dayNumber = 1))
    }
  }

  suspend fun updateUserProfile(
    name: String,
    age: Int,
    habitType: String,
    changeReason: String,
    durationText: String = "",
    intensityText: String = "",
    triggersText: String = "",
    previousAttempt: String = "",
    relapseReason: String = "",
    motivationText: String = "",
    isSetupCompleted: Boolean = true
  ) {
    val current = dao.getUserProfileDirect() ?: UserProfileEntity()
    dao.insertOrUpdateUserProfile(
      current.copy(
        name = name,
        age = age,
        habitType = habitType,
        changeReason = changeReason,
        durationText = durationText.ifBlank { current.durationText },
        intensityText = intensityText.ifBlank { current.intensityText },
        triggersText = triggersText.ifBlank { current.triggersText },
        previousAttempt = previousAttempt.ifBlank { current.previousAttempt },
        relapseReason = relapseReason.ifBlank { current.relapseReason },
        motivationText = motivationText.ifBlank { current.motivationText },
        isSetupCompleted = isSetupCompleted
      )
    )
  }

  suspend fun loginUser(email: String, authMethod: String = "EMAIL", displayName: String = "") {
    val current = dao.getUserProfileDirect() ?: UserProfileEntity()
    val nameToUse = if (displayName.isNotBlank()) displayName else if (current.name.isNotBlank() && current.name != "کاربر عزیز") current.name else email.substringBefore("@")
    dao.insertOrUpdateUserProfile(
      current.copy(
        email = email,
        name = nameToUse,
        authMethod = authMethod,
        isLoggedIn = true
      )
    )
  }

  suspend fun registerUser(email: String, name: String, authMethod: String = "EMAIL") {
    val current = dao.getUserProfileDirect() ?: UserProfileEntity()
    dao.insertOrUpdateUserProfile(
      current.copy(
        email = email,
        name = if (name.isNotBlank()) name else email.substringBefore("@"),
        authMethod = authMethod,
        isLoggedIn = true
      )
    )
  }

  suspend fun logoutUser() {
    val current = dao.getUserProfileDirect() ?: UserProfileEntity()
    dao.insertOrUpdateUserProfile(
      current.copy(
        isLoggedIn = false
      )
    )
  }

  suspend fun deleteUserAccount() {
    dao.deleteAllJournalEntries()
    dao.deleteAllUrgeLogs()
    dao.deleteAllAiChats()
    dao.insertOrUpdateUserProfile(UserProfileEntity(isLoggedIn = false, isSetupCompleted = false))
    initializeSeedDataIfNeeded()
  }

  suspend fun updateThemeMode(modeName: String) {
    val current = dao.getUserProfileDirect() ?: UserProfileEntity()
    dao.insertOrUpdateUserProfile(current.copy(themeModeName = modeName))
  }

  suspend fun updateNotifications(enabled: Boolean) {
    val current = dao.getUserProfileDirect() ?: UserProfileEntity()
    dao.insertOrUpdateUserProfile(current.copy(notificationsEnabled = enabled))
  }

  suspend fun updatePremiumSubscription(isPremium: Boolean, planName: String, expiryDate: String) {
    val current = dao.getUserProfileDirect() ?: UserProfileEntity()
    dao.insertOrUpdateUserProfile(
      current.copy(
        isPremium = isPremium,
        subscriptionPlan = planName,
        subscriptionExpiry = expiryDate
      )
    )
  }

  suspend fun updateDailyMission(mission: DailyMissionEntity) {
    dao.insertOrUpdateDailyMission(mission)
  }

  suspend fun completeDailyContent(dayNumber: Int, reflection: String) {
    val content = dao.getDailyContentFlow(dayNumber).firstOrNull()
    if (content != null) {
      dao.updateDailyContent(content.copy(isCompleted = true, userReflection = reflection))
      
      // Update profile points and current day
      val profile = dao.getUserProfileDirect() ?: UserProfileEntity()
      val newPoints = profile.totalPoints + content.pointsAwarded
      val nextDay = if (profile.currentDay < 30) profile.currentDay + 1 else 30
      dao.insertOrUpdateUserProfile(
        profile.copy(
          totalPoints = newPoints,
          currentDay = nextDay
        )
      )

      // Check badges
      checkAndUpdateBadges(nextDay)
    }
  }

  private suspend fun checkAndUpdateBadges(currentDay: Int) {
    val achievements = dao.getAllAchievementsFlow().firstOrNull() ?: return
    for (badge in achievements) {
      if (!badge.isUnlocked && currentDay >= badge.requiredDays) {
        dao.updateAchievement(
          badge.copy(
            isUnlocked = true,
            unlockedDate = "روز $currentDay"
          )
        )
      }
    }
  }

  suspend fun addJournalEntry(
    mood: String,
    urgeIntensity: Int,
    noteText: String,
    successText: String,
    dateString: String,
    sleepHours: Int = 7,
    energyLevel: Int = 7
  ) {
    dao.insertJournalEntry(
      JournalEntryEntity(
        dateString = dateString,
        mood = mood,
        urgeIntensity = urgeIntensity,
        noteText = noteText,
        successText = successText,
        sleepHours = sleepHours,
        energyLevel = energyLevel
      )
    )
  }

  suspend fun deleteJournalEntry(id: Long) {
    dao.deleteJournalEntry(id)
  }

  suspend fun logUrgeEmergency(durationSeconds: Int, passedSuccessfully: Boolean, note: String) {
    val dateString = "امروز"
    dao.insertUrgeLog(
      UrgeLogEntity(
        dateString = dateString,
        durationSeconds = durationSeconds,
        passedSuccessfully = passedSuccessfully,
        note = note
      )
    )
  }

  suspend fun resetAllData() {
    dao.deleteAllJournalEntries()
    dao.deleteAllUrgeLogs()
    dao.deleteAllAiChats()
    dao.insertOrUpdateUserProfile(UserProfileEntity())
    initializeSeedDataIfNeeded()
  }

  // --- AI Companion Functions ---

  suspend fun seedInitialAiGreetingIfNeeded() {
    val chats = dao.getAllAiChatsFlow().firstOrNull()
    if (chats.isNullOrEmpty()) {
      dao.insertAiChat(
        AiChatEntity(
          message = "سلام دوست من 🌱 من «یار رها» هستم، همراه هوشمند تو در این مسیر. چطور می‌تونم امروز کمکت کنم؟ می‌تونی از احساساتت بگی یا برای مدیریت وسوسه با من صحبت کنی.",
          sender = "AI"
        )
      )
    }
  }

  suspend fun sendAiChatMessage(userPrompt: String): String {
    // 1. Save User Message
    val userChat = AiChatEntity(message = userPrompt, sender = "USER")
    dao.insertAiChat(userChat)

    // 2. Get history, profile, and latest daily status
    val history = dao.getAllAiChatsFlow().firstOrNull() ?: emptyList()
    val profile = dao.getUserProfileDirect()
    val latestStatus = dao.getAllJournalEntriesFlow().firstOrNull()?.firstOrNull()

    // 3. Call AIService with today's status context
    val aiReply = aiService.generateChatResponse(userPrompt, history, profile, latestStatus)

    // 4. Save AI Reply
    val aiChat = AiChatEntity(message = aiReply, sender = "AI")
    dao.insertAiChat(aiChat)

    return aiReply
  }

  suspend fun analyzeJournalEmotion(journalNote: String): com.example.data.ai.EmotionAnalysisResult {
    return aiService.analyzeEmotion(journalNote)
  }

  suspend fun clearAiChatHistory() {
    dao.deleteAllAiChats()
    seedInitialAiGreetingIfNeeded()
  }
}
