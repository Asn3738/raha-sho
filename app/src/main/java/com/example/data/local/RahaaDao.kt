package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RahaaDao {

  // User Profile
  @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
  fun getUserProfileFlow(): Flow<UserProfileEntity?>

  @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
  suspend fun getUserProfileDirect(): UserProfileEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateUserProfile(profile: UserProfileEntity)

  // Daily Mission
  @Query("SELECT * FROM daily_missions WHERE dayNumber = :dayNumber LIMIT 1")
  fun getDailyMissionFlow(dayNumber: Int): Flow<DailyMissionEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateDailyMission(mission: DailyMissionEntity)

  // Daily Contents
  @Query("SELECT * FROM daily_contents ORDER BY dayNumber ASC")
  fun getAllDailyContentsFlow(): Flow<List<DailyContentEntity>>

  @Query("SELECT * FROM daily_contents WHERE dayNumber = :dayNumber LIMIT 1")
  fun getDailyContentFlow(dayNumber: Int): Flow<DailyContentEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAllDailyContents(contents: List<DailyContentEntity>)

  @Update
  suspend fun updateDailyContent(content: DailyContentEntity)

  // Journal Entries
  @Query("SELECT * FROM journal_entries ORDER BY timestamp DESC")
  fun getAllJournalEntriesFlow(): Flow<List<JournalEntryEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertJournalEntry(entry: JournalEntryEntity)

  @Query("DELETE FROM journal_entries WHERE id = :id")
  suspend fun deleteJournalEntry(id: Long)

  // Urge Logs
  @Query("SELECT * FROM urge_logs ORDER BY timestamp DESC")
  fun getAllUrgeLogsFlow(): Flow<List<UrgeLogEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUrgeLog(log: UrgeLogEntity)

  // Achievements
  @Query("SELECT * FROM achievements")
  fun getAllAchievementsFlow(): Flow<List<AchievementEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAllAchievements(achievements: List<AchievementEntity>)

  @Update
  suspend fun updateAchievement(achievement: AchievementEntity)

  // AI Chat Messages
  @Query("SELECT * FROM ai_chats ORDER BY timestamp ASC")
  fun getAllAiChatsFlow(): Flow<List<AiChatEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAiChat(chat: AiChatEntity)

  @Query("DELETE FROM ai_chats")
  suspend fun deleteAllAiChats()

  // Reset database
  @Query("DELETE FROM journal_entries")
  suspend fun deleteAllJournalEntries()

  @Query("DELETE FROM urge_logs")
  suspend fun deleteAllUrgeLogs()
}
