package com.example.features.profile.data.repositories

import com.example.data.local.RahaaDao
import com.example.data.local.UserProfileEntity
import com.example.features.profile.domain.entities.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Repository handling user profile and recovery path data storage and sync.
 */
class ProfileRepository(
  private val dao: RahaaDao
) {

  fun getUserProfileFlow(): Flow<UserProfile> {
    return dao.getUserProfileFlow().map { entity: UserProfileEntity? ->
      if (entity == null) {
        UserProfile()
      } else {
        UserProfile(
          uid = if (entity.email.isNotBlank()) entity.email else "guest_${entity.id}",
          name = entity.name,
          email = entity.email,
          age = entity.age,
          startDateTimestamp = entity.startDateTimestamp,
          goal = entity.changeReason,
          substances = if (entity.habitType.isNotBlank()) listOf(entity.habitType) else emptyList(),
          behaviors = emptyList(),
          durationText = entity.durationText,
          intensityText = entity.intensityText,
          triggers = if (entity.triggersText.isNotBlank()) entity.triggersText.split(", ") else emptyList(),
          previousAttempt = entity.previousAttempt,
          relapseReason = entity.relapseReason,
          motivationText = entity.motivationText,
          currentDay = entity.currentDay,
          totalPoints = entity.totalPoints,
          isLoggedIn = entity.isLoggedIn,
          isSetupCompleted = entity.isSetupCompleted,
          notificationsEnabled = entity.notificationsEnabled,
          darkModeEnabled = entity.themeModeName == "DARK"
        )
      }
    }
  }

  suspend fun saveProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
    val entity = UserProfileEntity(
      id = 1,
      name = profile.name,
      email = profile.email,
      authMethod = if (profile.isLoggedIn) "AUTHENTICATED" else "GUEST",
      isLoggedIn = profile.isLoggedIn,
      age = profile.age,
      habitType = profile.substances.joinToString(", ").ifBlank { profile.goal },
      changeReason = profile.goal,
      durationText = profile.durationText,
      intensityText = profile.intensityText,
      triggersText = profile.triggers.joinToString(", "),
      previousAttempt = profile.previousAttempt,
      relapseReason = profile.relapseReason,
      motivationText = profile.motivationText,
      startDateTimestamp = profile.startDateTimestamp,
      currentDay = profile.currentDay,
      totalPoints = profile.totalPoints,
      isSetupCompleted = profile.isSetupCompleted,
      notificationsEnabled = profile.notificationsEnabled,
      themeModeName = if (profile.darkModeEnabled) "DARK" else "LIGHT"
    )
    dao.insertOrUpdateUserProfile(entity)
  }

  suspend fun updateStartDate(timestamp: Long) = withContext(Dispatchers.IO) {
    val current = dao.getUserProfileDirect() ?: UserProfileEntity()
    dao.insertOrUpdateUserProfile(current.copy(startDateTimestamp = timestamp))
  }

  suspend fun updateGoal(goal: String) = withContext(Dispatchers.IO) {
    val current = dao.getUserProfileDirect() ?: UserProfileEntity()
    dao.insertOrUpdateUserProfile(current.copy(changeReason = goal))
  }

  suspend fun updateSubstancesAndBehaviors(substances: List<String>, behaviors: List<String>) = withContext(Dispatchers.IO) {
    val current = dao.getUserProfileDirect() ?: UserProfileEntity()
    val combined = (substances + behaviors).joinToString(", ")
    dao.insertOrUpdateUserProfile(current.copy(habitType = combined))
  }
}
