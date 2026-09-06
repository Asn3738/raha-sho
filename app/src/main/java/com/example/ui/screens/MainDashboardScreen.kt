package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.isAdmin
import com.example.ui.AppScreen
import com.example.ui.RahaaViewModel
import com.example.ui.components.*
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAmber
import com.example.features.daily_recommendation.presentation.widgets.DailyRecommendationCard
import com.example.features.daily_recommendation.engine.RecommendationEngine
import com.example.features.rahaa_ai_engine.domain.RecoveryContext

@Composable
fun MainDashboardScreen(
  viewModel: RahaaViewModel
) {
  val userProfile by viewModel.userProfile.collectAsState()
  val currentMission by viewModel.currentDayMission.collectAsState()

  val todayMood by viewModel.todayMood.collectAsState()
  val todayCraving by viewModel.todayCraving.collectAsState()
  val todaySleep by viewModel.todaySleep.collectAsState()
  val todayEnergy by viewModel.todayEnergy.collectAsState()

  var showLogStatusDialog by remember { mutableStateOf(false) }

  val userName = userProfile?.name ?: "علیرضا"
  val currentDay = userProfile?.currentDay ?: 1
  val cleanDays = remember(userProfile?.startDateTimestamp) {
    val start = userProfile?.startDateTimestamp ?: System.currentTimeMillis()
    java.util.concurrent.TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - start).toInt().coerceAtLeast(0)
  }

  val missionCount = listOf(
    currentMission?.isWaterDone == true,
    currentMission?.isWalkDone == true,
    currentMission?.isBreathingDone == true,
    currentMission?.isJournalDone == true,
    currentMission?.isDailyExerciseDone == true
  ).count { it }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 18.dp)
        .verticalScroll(rememberScrollState())
    ) {
      Spacer(modifier = Modifier.height(16.dp))

      // Top Header App Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "مسیر رهایی و انضباط",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "سلام $userName 🌱",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .border(2.dp, Color.White, CircleShape)
            .clickable { viewModel.navigateTo(AppScreen.USER_PROFILE) },
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (userName.isNotBlank()) userName.take(1) else "ع",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
        }
      }

      val isAdmin = userProfile.isAdmin
      if (isAdmin) {
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = GoldAmber.copy(alpha = 0.2f),
          border = BorderStroke(1.dp, GoldAmber),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = GoldAmber)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "👑 دسترسی ویژه ادمین (الله اکبر) - قفل تمامی روزها و پرداخت‌ها باز است",
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Today's Status Overview (🌱 وضعیت امروز)
      TodayStatusOverviewCard(
        cleanDays = cleanDays,
        mood = todayMood,
        craving = todayCraving,
        sleep = todaySleep,
        energy = todayEnergy,
        onLogStatusClick = { showLogStatusDialog = true }
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Rahaa Plus Subscription Promo Banner (🌟 رها شو پلاس)
      Surface(
        onClick = { viewModel.navigateTo(AppScreen.PREMIUM) },
        shape = RoundedCornerShape(18.dp),
        color = GoldAmber.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, GoldAmber.copy(alpha = 0.6f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = GoldAmber, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = if (userProfile?.isPremium == true || userProfile.isAdmin) "اشتراک رها شو پلاس (فعال 🌟)" else "ارتقا به «رها شو پلاس» ⭐",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = if (userProfile?.isPremium == true || userProfile.isAdmin) "دسترسی بی‌محدودیت فعال است" else "هوش مصنوعی پیشرفته + گزارش‌های تحلیلی کامل",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
          Icon(Icons.Default.ArrowBack, contentDescription = null, tint = GoldAmber, modifier = Modifier.size(18.dp))
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Smart AI Recommendation Card (🤖 پیشنهاد امروز یار رها)
      val cravingInt = when {
        todayCraving.contains("شدید") || todayCraving.contains("زیاد") -> 8
        todayCraving.contains("متوسط") -> 5
        todayCraving.contains("کم") -> 2
        else -> todayCraving.filter { it.isDigit() }.toIntOrNull() ?: 2
      }
      val sleepInt = todaySleep.filter { it.isDigit() }.toIntOrNull() ?: 7
      val energyInt = when {
        todayEnergy.contains("عالی") || todayEnergy.contains("زیاد") -> 9
        todayEnergy.contains("خوب") -> 7
        todayEnergy.contains("متوسط") -> 5
        todayEnergy.contains("کم") -> 3
        else -> todayEnergy.filter { it.isDigit() }.toIntOrNull() ?: 7
      }

      val currentRecoveryContext = remember(userProfile, todayMood, todayCraving, todaySleep, todayEnergy, cleanDays, currentDay) {
        RecoveryContext(
          habit = userProfile?.habitType ?: "",
          duration = userProfile?.durationText ?: "",
          triggers = userProfile?.triggersText?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList(),
          cleanDays = cleanDays,
          mood = todayMood,
          craving = cravingInt,
          sleep = sleepInt,
          energy = energyInt,
          currentDay = currentDay
        )
      }
      val dailyRecommendation = remember(currentRecoveryContext) {
        RecommendationEngine().generate(currentRecoveryContext)
      }

      DailyRecommendationCard(
        recommendation = dailyRecommendation,
        onStartExerciseClick = { viewModel.navigateTo(AppScreen.JOURNEY) },
        onChatWithAiClick = { viewModel.navigateTo(AppScreen.AI_CHAT) }
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Progress Circle Card
      ProgressCircle(
        currentDay = currentDay,
        totalDays = 30
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Daily Morning Motivational Audio & Healing Frequency Player
      MorningAudioAffirmationCard(
        currentDay = currentDay
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Visual Progress Dashboard (30-day Grid, Streak Graph, Milestones)
      VisualProgressDashboard(
        currentDay = currentDay,
        totalDays = 30,
        streakDays = if (cleanDays > 0) cleanDays else currentDay,
        onDayClick = { day ->
          viewModel.openDailyContent(day)
        }
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Protocol Guidance Card
      val habitType = userProfile?.habitType ?: "ترک عادتهای مخرب"
      val protocol = com.example.data.model.SubstanceProtocolManager.getProtocol(habitType)
      var showFullProtocolDetails by remember { mutableStateOf(false) }

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .shadow(2.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.MedicalServices,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onPrimary,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "پروتکل اختصاصی رهایی",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = habitType,
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.SemiBold,
                  color = MaterialTheme.colorScheme.primary
                )
              }
            }

            IconButton(onClick = { showFullProtocolDetails = !showFullProtocolDetails }) {
              Icon(
                imageVector = if (showFullProtocolDetails) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = "مشاهده جزئیات",
                tint = MaterialTheme.colorScheme.primary
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
          ) {
            Text(
              text = "📌 ${protocol.protocolTitle}",
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "⏳ اوج ولع و خماری: ${protocol.peakWindow}",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "💡 راهبرد استاندارد: ${protocol.keyAdvice}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 20.sp
          )

          if (showFullProtocolDetails) {
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = "⚡ علائم متداول جسمی و روانی:",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
            Text(
              text = protocol.withdrawalSymptoms,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "🥗 تغذیه و مایعات پیشنهادی:",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
            Text(
              text = protocol.nutritionAdvice,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "🚨 تکنیک اضطراری کنترل ولع:",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
            Text(
              text = protocol.emergencyCopingTechnique,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.Warning,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.error,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = protocol.medicalWarning,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onErrorContainer,
                  lineHeight = 18.sp
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Recovery Engine Strategy Card (موتور تخصصی شخصی‌سازی مسیر رهایی)
      val userProfileEntity = userProfile
      val recoveryStrategy = remember(userProfileEntity) {
        com.example.features.recovery_engine.domain.services.RecoveryEngineService.generateStrategy(
          userProfileEntity ?: com.example.data.local.UserProfileEntity()
        )
      }

      PersianCard {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = CircleShape,
              color = EmeraldPrimary.copy(alpha = 0.15f)
            ) {
              Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = null,
                tint = EmeraldPrimary,
                modifier = Modifier
                  .padding(8.dp)
                  .size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "نقشه راهبردی رهایی (Recovery Engine)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "تحلیل هوشمند بر اساس پاسخ‌های ارزیابی شما",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Focus Badge
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = EmeraldPrimary.copy(alpha = 0.12f),
          border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Adjust, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "تمرکز فعلی: ${recoveryStrategy.focus}",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = EmeraldPrimary
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Priorities List
        Text(
          text = "🎯 اولویت‌های درمانی اختصاصی شما:",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(6.dp))
        recoveryStrategy.priorities.forEach { priority ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = priority,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Daily Exercises
        Text(
          text = "🧘‍♂️ تمرین‌های کلیدی امروز (بر اساس محرک‌ها):",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(6.dp))
        recoveryStrategy.dailyExercises.forEach { ex ->
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = GoldAmber, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = ex,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }

        if (recoveryStrategy.personalizedTips.isNotEmpty()) {
          Spacer(modifier = Modifier.height(12.dp))
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = GoldAmber.copy(alpha = 0.12f),
            border = BorderStroke(1.dp, GoldAmber.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.Top
            ) {
              Icon(Icons.Default.Lightbulb, contentDescription = null, tint = GoldAmber, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = recoveryStrategy.personalizedTips.first(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Today's Mission Card
      PersianCard {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "ماموریت امروز",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primaryContainer
          ) {
            Text(
              text = "$missionCount از ۵ انجام شده",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        DailyMissionCard(
          title = "نوشیدن ۸ لیوان آب",
          isCompleted = currentMission?.isWaterDone == true,
          onCheckedChange = { viewModel.updateMissionItem(water = it) },
          icon = Icons.Default.WaterDrop,
          guideText = "آب خنک یا ولرم میل کنید. هر ۲ ساعت یک لیوان بنوشید تا سموم کبد و کلیه سریع‌تر دفع شده و سردرد، خشکی دهان و بی‌حالی ناشی از سم‌زدایی کاملاً برطرف شود."
        )

        DailyMissionCard(
          title = "۱۰ دقیقه تمرین تنفس و آرامش",
          isCompleted = currentMission?.isBreathingDone == true,
          onCheckedChange = { viewModel.updateMissionItem(breathing = it) },
          icon = Icons.Default.Air,
          guideText = "طرز تنفس اصولی ۴-۷-۸: بر روی صندلی یا بالشتک با ستون فقرات صاف بنشینید. ۴ ثانیه دم عمیق از بینی، ۷ ثانیه حبس نفس در سینه، و ۸ ثانیه بازدم بسیار آرام از دهان. این چرخه را ۱۰ بار تکرار کنید تا سیستم عصبی آرام شده و ولع فکری کاملاً متوقف گردد."
        )

        DailyMissionCard(
          title = "پیاده‌روی عصرانه و ورزش",
          isCompleted = currentMission?.isWalkDone == true,
          onCheckedChange = { viewModel.updateMissionItem(walk = it) },
          icon = Icons.Default.DirectionsWalk,
          guideText = "۲۰ تا ۳۰ دقیقه پیاده‌روی با سرعت متوسط در فضای باز یا پارک انجام دهید. تنفس عمیق بکشید تا ترشح اندورفین و دوپامین طبیعی در مغز تحریک شود. از انجام ورزش‌های بسیار سنگین در روزهای اولیه پرهیز کنید."
        )

        DailyMissionCard(
          title = "نوشتن حس روز در دفتر من",
          isCompleted = currentMission?.isJournalDone == true,
          onCheckedChange = { viewModel.updateMissionItem(journal = it) },
          icon = Icons.Default.EditNote,
          guideText = "افکار، وسوسه‌ها، پیروزی‌های کوچک و احساسات امروز خود را با صداقت کامل بنویسید. تخلیه هیجانات منفی روی کاغذ، استرس ذهن را تا ۶۰٪ کاهش می‌دهد و اطلاعات شما ۱۰۰٪ محرمانه می‌ماند."
        )

        DailyMissionCard(
          title = "انجام تمرین روزانه آموزنده",
          isCompleted = currentMission?.isDailyExerciseDone == true,
          onCheckedChange = { viewModel.updateMissionItem(exercise = it) },
          icon = Icons.Default.FitnessCenter,
          guideText = "وارد بخش «روز جاری» در مسیر ۳۰ روزه رهایی شوید، مقاله کوتاه آموزشی همان روز را مطالعه کرده و تمرین خودآگاهی را تکمیل کنید تا امتیاز روزانه دریافت نمایید."
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // AI Companion Quick Access Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .shadow(2.dp, RoundedCornerShape(24.dp))
          .clickable { viewModel.navigateTo(AppScreen.AI_CHAT) },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "🌱", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "یار رها (همراه هوشمند)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "برای گفتگو، احساسات یا پشتیبانی در لحظات سخت با من صحبت کن.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = "ورود به چت",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Motivational Quote Card
      MotivationCard(
        quote = "«هر روز یک قدم، هر قدم یک پیروزی است.»",
        author = "جمله روز"
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Emergency Urge Action Button
      EmergencyButton(
        onClick = { viewModel.navigateTo(AppScreen.URGE_EMERGENCY) }
      )

      Spacer(modifier = Modifier.height(100.dp)) // Padding for bottom nav
    }

    if (showLogStatusDialog) {
      TodayStatusLogDialog(
        currentMood = todayMood,
        currentCraving = todayCraving,
        currentSleep = todaySleep,
        currentEnergy = todayEnergy,
        onDismiss = { showLogStatusDialog = false },
        onSaveStatus = { mood, craving, sleep, energy ->
          viewModel.updateTodayCheckInStatus(mood, craving, sleep, energy)
        }
      )
    }
  }
}
