package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.*
import com.example.data.repository.RahaaRepository
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class AppScreen {
  SPLASH,
  SPIRITUAL_INTRO,
  WELCOME,
  AUTH,
  PROFILE_SETUP,
  HOME,
  JOURNEY,
  DAILY_CONTENT,
  URGE_EMERGENCY,
  JOURNAL,
  CALM,
  ACHIEVEMENTS,
  USER_PROFILE,
  SETTINGS,
  AI_CHAT,
  ABOUT_APP,
  CREATOR,
  PREMIUM
}

class RahaaViewModel(application: Application) : AndroidViewModel(application) {

  private val database = RahaaDatabase.getDatabase(application)
  private val repository = RahaaRepository(database.rahaaDao())

  // Navigation State
  private val _currentScreen = MutableStateFlow(AppScreen.SPLASH)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  // Selected Day for Daily Content detail
  private val _selectedDayNumber = MutableStateFlow(1)
  val selectedDayNumber: StateFlow<Int> = _selectedDayNumber.asStateFlow()

  // Database Flows
  val userProfile = repository.userProfile.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    null
  )

  val allDailyContents = repository.allDailyContents.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  val allJournalEntries = repository.allJournalEntries.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  val allUrgeLogs = repository.allUrgeLogs.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  val allAchievements = repository.allAchievements.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  // Current Day Mission State
  val currentDayMission: StateFlow<DailyMissionEntity?> = userProfile.flatMapLatest { profile ->
    val dayNum = profile?.currentDay ?: 1
    repository.getDailyMission(dayNum)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  // Currently selected Daily Content
  val selectedDailyContent: StateFlow<DailyContentEntity?> = selectedDayNumber.flatMapLatest { dayNum ->
    repository.getDailyContent(dayNum)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  // Theme Mode
  val themeMode: StateFlow<ThemeMode> = userProfile.map { profile ->
    when (profile?.themeModeName) {
      "LIGHT" -> ThemeMode.LIGHT
      "DARK" -> ThemeMode.DARK
      else -> ThemeMode.SYSTEM
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeMode.SYSTEM)

  val allAiChats = repository.allAiChats.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  private val _isAiThinking = MutableStateFlow(false)
  val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

  private val _aiEmotionAnalysis = MutableStateFlow<com.example.data.ai.EmotionAnalysisResult?>(null)
  val aiEmotionAnalysis: StateFlow<com.example.data.ai.EmotionAnalysisResult?> = _aiEmotionAnalysis.asStateFlow()

  // Today's Check-in Status (🌱 وضعیت امروز)
  private val _todayMood = MutableStateFlow("خوب")
  val todayMood: StateFlow<String> = _todayMood.asStateFlow()

  private val _todayCraving = MutableStateFlow("کم")
  val todayCraving: StateFlow<String> = _todayCraving.asStateFlow()

  private val _todaySleep = MutableStateFlow("7 ساعت")
  val todaySleep: StateFlow<String> = _todaySleep.asStateFlow()

  private val _todayEnergy = MutableStateFlow("متوسط")
  val todayEnergy: StateFlow<String> = _todayEnergy.asStateFlow()

  fun updateTodayCheckInStatus(mood: String, craving: String, sleep: String, energy: String) {
    _todayMood.value = mood
    _todayCraving.value = craving
    _todaySleep.value = sleep
    _todayEnergy.value = energy

    viewModelScope.launch {
      val urgeInt = when {
        craving.contains("زیاد") || craving.contains("شدید") -> 8
        craving.contains("متوسط") -> 5
        else -> 2
      }
      val dateStr = SimpleDateFormat("yyyy/MM/dd - HH:mm", Locale.getDefault()).format(Date())
      repository.addJournalEntry(
        mood = mood,
        urgeIntensity = urgeInt,
        noteText = "ثبت وضعیت روزانه - خواب: $sleep | انرژی: $energy | وسوسه: $craving",
        successText = "وضعیت امروز با موفقیت پایش شد 🌱",
        dateString = dateStr
      )
    }
  }

  init {
    viewModelScope.launch {
      repository.initializeSeedDataIfNeeded()
      repository.seedInitialAiGreetingIfNeeded()
    }
  }

  // Auth Message State
  private val _authMessage = MutableStateFlow<String?>(null)
  val authMessage: StateFlow<String?> = _authMessage.asStateFlow()

  fun clearAuthMessage() {
    _authMessage.value = null
  }

  fun navigateTo(screen: AppScreen) {
    val isPublicRoute = screen in listOf(
      AppScreen.SPLASH,
      AppScreen.SPIRITUAL_INTRO,
      AppScreen.WELCOME,
      AppScreen.AUTH,
      AppScreen.PROFILE_SETUP
    )

    val profile = userProfile.value
    val isAuthenticated = profile?.isLoggedIn == true || profile?.isSetupCompleted == true

    if (!isPublicRoute && !isAuthenticated) {
      _authMessage.value = "🔒 برای شروع مسیر رهایی ابتدا حساب خود را بسازید."
      _currentScreen.value = AppScreen.AUTH
      return
    }

    _currentScreen.value = screen
  }

  fun loginUser(email: String, authMethod: String = "EMAIL", displayName: String = "") {
    viewModelScope.launch {
      repository.loginUser(email, authMethod, displayName)
      _authMessage.value = "ورود با موفقیت انجام شد 🌿"
      val isSetup = userProfile.value?.isSetupCompleted == true
      if (isSetup) {
        _currentScreen.value = AppScreen.HOME
      } else {
        _currentScreen.value = AppScreen.PROFILE_SETUP
      }
    }
  }

  fun registerUser(email: String, name: String, authMethod: String = "EMAIL") {
    viewModelScope.launch {
      repository.registerUser(email, name, authMethod)
      _authMessage.value = "ثبت‌نام با موفقیت انجام شد 🌿"
      _currentScreen.value = AppScreen.PROFILE_SETUP
    }
  }

  fun logoutUser() {
    viewModelScope.launch {
      repository.logoutUser()
      _authMessage.value = "از حساب کاربری خارج شدید."
      _currentScreen.value = AppScreen.WELCOME
    }
  }

  fun deleteUserAccount() {
    viewModelScope.launch {
      repository.deleteUserAccount()
      _authMessage.value = "حساب کاربری و تمامی اطلاعات با موفقیت حذف شدند."
      _currentScreen.value = AppScreen.WELCOME
    }
  }

  fun openDailyContent(dayNumber: Int) {
    _selectedDayNumber.value = dayNumber
    _currentScreen.value = AppScreen.DAILY_CONTENT
  }

  fun updateMissionItem(
    water: Boolean? = null,
    walk: Boolean? = null,
    breathing: Boolean? = null,
    journal: Boolean? = null,
    exercise: Boolean? = null
  ) {
    viewModelScope.launch {
      val dayNum = userProfile.value?.currentDay ?: 1
      val currentMission = currentDayMission.value ?: DailyMissionEntity(dayNumber = dayNum)
      val updated = currentMission.copy(
        isWaterDone = water ?: currentMission.isWaterDone,
        isWalkDone = walk ?: currentMission.isWalkDone,
        isBreathingDone = breathing ?: currentMission.isBreathingDone,
        isJournalDone = journal ?: currentMission.isJournalDone,
        isDailyExerciseDone = exercise ?: currentMission.isDailyExerciseDone,
        lastUpdatedTimestamp = System.currentTimeMillis()
      )
      repository.updateDailyMission(updated)
    }
  }

  fun saveProfileSetup(
    name: String,
    age: Int,
    habitType: String,
    changeReason: String,
    durationText: String = "",
    intensityText: String = "",
    triggersText: String = "",
    previousAttempt: String = "",
    relapseReason: String = "",
    motivationText: String = ""
  ) {
    viewModelScope.launch {
      repository.updateUserProfile(
        name = name.ifBlank { "همدرد عزیز" },
        age = if (age > 0) age else 25,
        habitType = habitType,
        changeReason = changeReason.ifBlank { "بازگشت به خود واقعی و شروع ۳۰ روزه زندگی جدید" },
        durationText = durationText,
        intensityText = intensityText,
        triggersText = triggersText,
        previousAttempt = previousAttempt,
        relapseReason = relapseReason,
        motivationText = motivationText,
        isSetupCompleted = true
      )
      _currentScreen.value = AppScreen.HOME
    }
  }

  fun completeCurrentDayContent(reflectionText: String) {
    viewModelScope.launch {
      val dayNum = selectedDayNumber.value
      repository.completeDailyContent(dayNum, reflectionText)
      _currentScreen.value = AppScreen.HOME
    }
  }

  fun addJournalEntry(
    mood: String,
    urgeIntensity: Int,
    noteText: String,
    successText: String = "",
    sleepHours: Int = 7,
    energyLevel: Int = 7
  ) {
    viewModelScope.launch {
      val dateStr = SimpleDateFormat("yyyy/MM/dd - HH:mm", Locale.getDefault()).format(Date())
      repository.addJournalEntry(
        mood = mood,
        urgeIntensity = urgeIntensity,
        noteText = noteText,
        successText = successText,
        dateString = dateStr,
        sleepHours = sleepHours,
        energyLevel = energyLevel
      )
    }
  }

  fun deleteJournalEntry(id: Long) {
    viewModelScope.launch {
      repository.deleteJournalEntry(id)
    }
  }

  fun logUrgeEmergency(durationSeconds: Int, passedSuccessfully: Boolean, note: String) {
    viewModelScope.launch {
      repository.logUrgeEmergency(
        durationSeconds = durationSeconds,
        passedSuccessfully = passedSuccessfully,
        note = note
      )
    }
  }

  fun updateThemeMode(mode: ThemeMode) {
    viewModelScope.launch {
      repository.updateThemeMode(mode.name)
    }
  }

  fun updateNotifications(enabled: Boolean) {
    viewModelScope.launch {
      repository.updateNotifications(enabled)
    }
  }

  fun activatePremiumSubscription(planName: String, durationDays: Int) {
    viewModelScope.launch {
      val calendar = Calendar.getInstance()
      calendar.add(Calendar.DAY_OF_YEAR, durationDays)
      val expiryStr = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(calendar.time)
      repository.updatePremiumSubscription(
        isPremium = true,
        planName = planName,
        expiryDate = expiryStr
      )
    }
  }

  fun resetAllData() {
    viewModelScope.launch {
      repository.resetAllData()
      _currentScreen.value = AppScreen.WELCOME
    }
  }

  // --- AI Companion Methods ---

  fun sendAiChatMessage(userPrompt: String) {
    if (userPrompt.isBlank() || _isAiThinking.value) return
    viewModelScope.launch {
      _isAiThinking.value = true
      try {
        repository.sendAiChatMessage(userPrompt)
      } finally {
        _isAiThinking.value = false
      }
    }
  }

  fun analyzeJournalEmotion(journalNote: String) {
    if (journalNote.isBlank()) return
    viewModelScope.launch {
      val result = repository.analyzeJournalEmotion(journalNote)
      _aiEmotionAnalysis.value = result
    }
  }

  fun clearAiChatHistory() {
    viewModelScope.launch {
      repository.clearAiChatHistory()
    }
  }
}
