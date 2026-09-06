package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
  entities = [
    UserProfileEntity::class,
    DailyMissionEntity::class,
    JournalEntryEntity::class,
    UrgeLogEntity::class,
    AchievementEntity::class,
    DailyContentEntity::class,
    AiChatEntity::class
  ],
  version = 7,
  exportSchema = false
)
abstract class RahaaDatabase : RoomDatabase() {

  abstract fun rahaaDao(): RahaaDao

  companion object {
    @Volatile
    private var INSTANCE: RahaaDatabase? = null

    private fun dropAndRecreateTables(db: SupportSQLiteDatabase) {
      db.execSQL("DROP TABLE IF EXISTS `user_profile`")
      db.execSQL("DROP TABLE IF EXISTS `daily_missions`")
      db.execSQL("DROP TABLE IF EXISTS `journal_entries`")
      db.execSQL("DROP TABLE IF EXISTS `urge_logs`")
      db.execSQL("DROP TABLE IF EXISTS `achievements`")
      db.execSQL("DROP TABLE IF EXISTS `daily_contents`")
      db.execSQL("DROP TABLE IF EXISTS `ai_chats`")

      db.execSQL(
        """
        CREATE TABLE IF NOT EXISTS `user_profile` (
            `id` INTEGER NOT NULL PRIMARY KEY,
            `name` TEXT NOT NULL,
            `email` TEXT NOT NULL DEFAULT '',
            `authMethod` TEXT NOT NULL DEFAULT 'GUEST',
            `isLoggedIn` INTEGER NOT NULL DEFAULT 0,
            `age` INTEGER NOT NULL,
            `habitType` TEXT NOT NULL,
            `changeReason` TEXT NOT NULL,
            `durationText` TEXT NOT NULL DEFAULT '',
            `intensityText` TEXT NOT NULL DEFAULT '',
            `triggersText` TEXT NOT NULL DEFAULT '',
            `previousAttempt` TEXT NOT NULL DEFAULT '',
            `relapseReason` TEXT NOT NULL DEFAULT '',
            `motivationText` TEXT NOT NULL DEFAULT '',
            `startDateTimestamp` INTEGER NOT NULL,
            `currentDay` INTEGER NOT NULL,
            `totalPoints` INTEGER NOT NULL,
            `isSetupCompleted` INTEGER NOT NULL,
            `notificationsEnabled` INTEGER NOT NULL,
            `themeModeName` TEXT NOT NULL
        )
        """.trimIndent()
      )

      db.execSQL(
        """
        CREATE TABLE IF NOT EXISTS `daily_missions` (
            `dayNumber` INTEGER NOT NULL PRIMARY KEY,
            `isWaterDone` INTEGER NOT NULL,
            `isWalkDone` INTEGER NOT NULL,
            `isBreathingDone` INTEGER NOT NULL,
            `isJournalDone` INTEGER NOT NULL,
            `isDailyExerciseDone` INTEGER NOT NULL,
            `lastUpdatedTimestamp` INTEGER NOT NULL
        )
        """.trimIndent()
      )

      db.execSQL(
        """
        CREATE TABLE IF NOT EXISTS `journal_entries` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            `dateString` TEXT NOT NULL,
            `timestamp` INTEGER NOT NULL,
            `mood` TEXT NOT NULL,
            `urgeIntensity` INTEGER NOT NULL,
            `noteText` TEXT NOT NULL,
            `successText` TEXT NOT NULL,
            `sleepHours` INTEGER NOT NULL DEFAULT 7,
            `energyLevel` INTEGER NOT NULL DEFAULT 7
        )
        """.trimIndent()
      )

      db.execSQL(
        """
        CREATE TABLE IF NOT EXISTS `urge_logs` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            `timestamp` INTEGER NOT NULL,
            `dateString` TEXT NOT NULL,
            `durationSeconds` INTEGER NOT NULL,
            `passedSuccessfully` INTEGER NOT NULL,
            `note` TEXT NOT NULL
        )
        """.trimIndent()
      )

      db.execSQL(
        """
        CREATE TABLE IF NOT EXISTS `achievements` (
            `badgeId` TEXT NOT NULL PRIMARY KEY,
            `titleFa` TEXT NOT NULL,
            `descriptionFa` TEXT NOT NULL,
            `iconType` TEXT NOT NULL,
            `requiredDays` INTEGER NOT NULL,
            `isUnlocked` INTEGER NOT NULL,
            `unlockedDate` TEXT NOT NULL
        )
        """.trimIndent()
      )

      db.execSQL(
        """
        CREATE TABLE IF NOT EXISTS `daily_contents` (
            `dayNumber` INTEGER NOT NULL PRIMARY KEY,
            `phaseNumber` INTEGER NOT NULL,
            `phaseName` TEXT NOT NULL,
            `titleFa` TEXT NOT NULL,
            `subtitleFa` TEXT NOT NULL,
            `educationalText` TEXT NOT NULL,
            `exerciseText` TEXT NOT NULL,
            `isCompleted` INTEGER NOT NULL,
            `userReflection` TEXT NOT NULL,
            `pointsAwarded` INTEGER NOT NULL,
            `quote1Text` TEXT NOT NULL,
            `quote1Meaning` TEXT NOT NULL,
            `quote2Text` TEXT NOT NULL,
            `quote2Meaning` TEXT NOT NULL,
            `quote3Text` TEXT NOT NULL,
            `quote3Meaning` TEXT NOT NULL,
            `storyTitle` TEXT NOT NULL,
            `storyContent` TEXT NOT NULL,
            `storyMainMessage` TEXT NOT NULL,
            `articleTitle` TEXT NOT NULL,
            `articleKeyTakeaway` TEXT NOT NULL,
            `poemText` TEXT NOT NULL,
            `poemMeaning` TEXT NOT NULL,
            `supportMessage` TEXT NOT NULL
        )
        """.trimIndent()
      )

      db.execSQL(
        """
        CREATE TABLE IF NOT EXISTS `ai_chats` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            `message` TEXT NOT NULL,
            `sender` TEXT NOT NULL,
            `timestamp` INTEGER NOT NULL,
            `emotion` TEXT
        )
        """.trimIndent()
      )
    }

    private val MIGRATION_1_5 = object : Migration(1, 5) {
      override fun migrate(db: SupportSQLiteDatabase) = dropAndRecreateTables(db)
    }
    private val MIGRATION_2_5 = object : Migration(2, 5) {
      override fun migrate(db: SupportSQLiteDatabase) = dropAndRecreateTables(db)
    }
    private val MIGRATION_3_5 = object : Migration(3, 5) {
      override fun migrate(db: SupportSQLiteDatabase) = dropAndRecreateTables(db)
    }
    private val MIGRATION_4_5 = object : Migration(4, 5) {
      override fun migrate(db: SupportSQLiteDatabase) = dropAndRecreateTables(db)
    }

    fun getDatabase(context: Context): RahaaDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          RahaaDatabase::class.java,
          "rahaa_sho_app_db"
        )
          .addMigrations(MIGRATION_1_5, MIGRATION_2_5, MIGRATION_3_5, MIGRATION_4_5)
          .fallbackToDestructiveMigration(true)
          .fallbackToDestructiveMigrationOnDowngrade(true)
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}


