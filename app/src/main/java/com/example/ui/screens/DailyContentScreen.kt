package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.DailyExerciseMeditationData
import com.example.data.local.Recovery30DayHelper
import com.example.ui.AppScreen
import com.example.ui.RahaaViewModel
import com.example.ui.components.CustomButton
import com.example.ui.components.LightExerciseCard
import com.example.ui.components.MeditationCard
import com.example.ui.components.PersianCard
import com.example.ui.theme.*

@Composable
fun DailyContentScreen(
  viewModel: RahaaViewModel
) {
  val content by viewModel.selectedDailyContent.collectAsState()
  val currentDayNum = content?.dayNumber ?: 1
  val currentMission by viewModel.currentDayMission.collectAsState()
  var reflectionText by remember(content?.dayNumber) { mutableStateOf(content?.userReflection ?: "") }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {
      // Top Bar Navigation
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        IconButton(onClick = { viewModel.navigateTo(AppScreen.JOURNEY) }) {
          Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "بازگشت"
          )
        }

        Text(
          text = "روز ${content?.dayNumber ?: 1} از ۳۰ — مسیر رهایی",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = SoftSunGold.copy(alpha = 0.2f)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Star, contentDescription = null, tint = SoftSunGold, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "+${content?.pointsAwarded ?: 50} امتیاز",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = SoftSunGold
            )
          }
        }
      }

      // Hero Banner Image
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(190.dp)
          .padding(horizontal = 18.dp)
          .clip(RoundedCornerShape(24.dp))
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_persian_garden_1785492176042),
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
              )
            )
        )
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(18.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = IranianTurquoise
          ) {
            Text(
              text = content?.phaseName ?: "مرحله اول",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = Color.White,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = content?.titleFa ?: "روز ${content?.dayNumber}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = content?.subtitleFa ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.8f)
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      Column(modifier = Modifier.padding(horizontal = 18.dp)) {

        // Progress Header Card
        val day30Model = remember(currentDayNum, content) { Recovery30DayHelper.getDayModel(currentDayNum, content) }
        
        var spiritualDone by remember(currentDayNum) { mutableStateOf(currentMission?.isJournalDone == true) }
        var prayerDone by remember(currentDayNum) { mutableStateOf(false) }
        var meditationDone by remember(currentDayNum) { mutableStateOf(currentMission?.isBreathingDone == true) }
        var exerciseDone by remember(currentDayNum) { mutableStateOf(currentMission?.isDailyExerciseDone == true) }
        var lessonDone by remember(currentDayNum) { mutableStateOf(false) }
        var actionDone by remember(currentDayNum) { mutableStateOf(false) }

        val totalTasks = 6
        val completedTasksCount = (if (spiritualDone) 1 else 0) +
            (if (prayerDone) 1 else 0) +
            (if (meditationDone) 1 else 0) +
            (if (exerciseDone) 1 else 0) +
            (if (lessonDone) 1 else 0) +
            (if (actionDone) 1 else 0)
        val progressPercent = completedTasksCount.toFloat() / totalTasks.toFloat()

        Surface(
          shape = RoundedCornerShape(24.dp),
          color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
          border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("🌱", fontSize = 20.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "امروز یک قدم دیگر نزدیک‌تر شو",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
              progress = progressPercent,
              modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
              color = EmeraldPrimary,
              trackColor = EmeraldPrimary.copy(alpha = 0.15f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "${(progressPercent * 100).toInt()}٪ تکمیل روز $currentDayNum",
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold,
              color = EmeraldPrimary
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ==========================================
        // SECTION 1: 🌿 ۱. توکل و شروع روز
        // ==========================================
        SectionTitleHeader(title = "🌿 ۱. توکل و شروع روز", icon = Icons.Default.Spa, color = EmeraldPrimary)
        Spacer(modifier = Modifier.height(8.dp))
        PersianCard(borderColor = EmeraldPrimary.copy(alpha = 0.3f)) {
          Text(
            text = day30Model.spiritual,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(8.dp))
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            Text("انجام دادم", style = MaterialTheme.typography.labelMedium)
            Checkbox(
              checked = spiritualDone,
              onCheckedChange = {
                spiritualDone = it
                viewModel.updateMissionItem(journal = it)
              },
              colors = CheckboxDefaults.colors(checkedColor = EmeraldPrimary)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ==========================================
        // SECTION 2: 🙏 ۲. دعای کوتاه روز
        // ==========================================
        SectionTitleHeader(title = "🙏 ۲. دعای کوتاه روز", icon = Icons.Default.Favorite, color = PersianLapisBlue)
        Spacer(modifier = Modifier.height(8.dp))
        PersianCard(borderColor = PersianLapisBlue.copy(alpha = 0.3f)) {
          Text(
            text = day30Model.prayer,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(8.dp))
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            Text("خواندم و زمزمه کردم", style = MaterialTheme.typography.labelMedium)
            Checkbox(
              checked = prayerDone,
              onCheckedChange = { prayerDone = it },
              colors = CheckboxDefaults.colors(checkedColor = PersianLapisBlue)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ==========================================
        // SECTION 3: 📖 ۳. آیه یا پیام امید
        // ==========================================
        SectionTitleHeader(title = "📖 ۳. آیه یا پیام امید", icon = Icons.Default.MenuBook, color = SoftSunGold)
        Spacer(modifier = Modifier.height(8.dp))
        QuoteCardItem(quoteText = day30Model.verse, meaningText = "")

        Spacer(modifier = Modifier.height(14.dp))

        // ==========================================
        // SECTION 4: ❤️ ۴. عهد امروز با خدا
        // ==========================================
        SectionTitleHeader(title = "❤️ ۴. عهد امروز با خدا", icon = Icons.Default.Handshake, color = UrgeAlertRed)
        Spacer(modifier = Modifier.height(8.dp))
        PersianCard(borderColor = UrgeAlertRed.copy(alpha = 0.3f)) {
          Text(
            text = day30Model.commitment,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ==========================================
        // SECTION 2: 📖 داستان امروز (Today's Story)
        // ==========================================
        if (!content?.storyTitle.isNullOrBlank()) {
          SectionTitleHeader(title = "📖 داستان امروز", icon = Icons.Default.MenuBook, color = PersianLapisBlue)
          Spacer(modifier = Modifier.height(8.dp))

          PersianCard {
            Text(
              text = content?.storyTitle ?: "داستان امروز",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = content?.storyContent ?: "",
              style = MaterialTheme.typography.bodyMedium,
              lineHeight = 24.sp,
              color = MaterialTheme.colorScheme.onSurface
            )

            if (!content?.storyMainMessage.isNullOrBlank()) {
              Spacer(modifier = Modifier.height(12.dp))
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
              ) {
                Row(
                  modifier = Modifier.padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(Icons.Default.Lightbulb, contentDescription = null, tint = SoftSunGold, modifier = Modifier.size(20.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Text(
                      text = "پیام اصلی داستان:",
                      style = MaterialTheme.typography.labelMedium,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                      text = content?.storyMainMessage ?: "",
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  }
                }
              }
            }
          }
          Spacer(modifier = Modifier.height(16.dp))
        }

        // ==========================================
        // SECTION 3: 🧠 مقاله کوتاه (Short Article)
        // ==========================================
        SectionTitleHeader(title = "🧠 مقاله کوتاه آموزشی", icon = Icons.Default.Psychology, color = IranianTurquoise)
        Spacer(modifier = Modifier.height(8.dp))

        PersianCard {
          Text(
            text = if (!content?.articleTitle.isNullOrBlank()) content?.articleTitle ?: "" else "آموزش گام امروز",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = content?.educationalText ?: "",
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 26.sp,
            color = MaterialTheme.colorScheme.onSurface
          )

          if (!content?.articleKeyTakeaway.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = CypressGreenContainer
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CypressGreen, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "نکته کلیدی: ${content?.articleKeyTakeaway}",
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.SemiBold,
                  color = CypressGreen
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // SECTION 3.5: 🛡️ راهبرد پروتکل اختصاصی رهایی
        // ==========================================
        val userProfile by viewModel.userProfile.collectAsState()
        val habitType = userProfile?.habitType ?: "ترک عادتهای مخرب"
        val activeProtocols = com.example.data.model.SubstanceProtocolManager.getProtocolsForHabits(habitType)

        SectionTitleHeader(
          title = "🛡️ راهبرد پروتکل‌های اختصاصی رهایی",
          icon = Icons.Default.MedicalServices,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        activeProtocols.forEach { protocol ->
          PersianCard(
            borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
          ) {
            Text(
              text = protocol.protocolTitle,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "💡 راهبرد استاندارد: ${protocol.keyAdvice}",
              style = MaterialTheme.typography.bodyMedium,
              lineHeight = 22.sp,
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "🥗 مراقبت غذایی و بدنی: ${protocol.nutritionAdvice}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
              shape = RoundedCornerShape(10.dp),
              color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.TaskAlt, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "تمرکز روز: ${protocol.dailyFocusPoints[(content?.dayNumber ?: 1) % protocol.dailyFocusPoints.size]}",
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // SECTION 4: 🎵 شعر امروز (Today's Poem)
        // ==========================================
        if (!content?.poemText.isNullOrBlank()) {
          SectionTitleHeader(title = "🎵 شعر امروز", icon = Icons.Default.MusicNote, color = SoftSunGold)
          Spacer(modifier = Modifier.height(8.dp))

          PersianCard(
            borderColor = SoftSunGold.copy(alpha = 0.4f)
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = content?.poemText ?: "",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            if (!content?.poemMeaning.isNullOrBlank()) {
              Spacer(modifier = Modifier.height(8.dp))
              Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "تفسیر شعر: ${content?.poemMeaning}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
        }

        // ==========================================
        // SECTION 5: ✍️ تمرین امروز (Interactive Exercise)
        // ==========================================
        SectionTitleHeader(title = "✍️ تمرین امروز", icon = Icons.Default.FitnessCenter, color = SoftSunGold)
        Spacer(modifier = Modifier.height(8.dp))

        PersianCard {
          Text(
            text = content?.exerciseText ?: "تمرین امروز را انجام داده و احساس خود را بنویسید.",
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = reflectionText,
            onValueChange = { reflectionText = it },
            placeholder = { Text("پاسخ به تمرین یا تجربه و حس خود را اینجا بنویسید...") },
            modifier = Modifier
              .fillMaxWidth()
              .height(110.dp),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(14.dp))

          CustomButton(
            text = if (content?.isCompleted == true) "ویرایش و ذخیره پاسخ" else "تکمیل روز $currentDayNum و دریافت امتیاز",
            onClick = {
              viewModel.completeCurrentDayContent(reflectionText)
            },
            icon = Icons.Default.CheckCircle
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // SECTION 5.5: 🏃‍♂️ حرکت و مدیتیشن امروز (Light Movement & Meditation)
        // ==========================================
        val exerciseInfo = DailyExerciseMeditationData.getExerciseForDay(currentDayNum)
        val meditationInfo = DailyExerciseMeditationData.getMeditationForDay(currentDayNum)
        var selectedExerciseFeeling by remember(currentDayNum) { mutableStateOf<String?>(null) }

        // Light Movement Card
        LightExerciseCard(
          exerciseInfo = exerciseInfo,
          isCompleted = currentMission?.isDailyExerciseDone == true,
          selectedFeeling = selectedExerciseFeeling,
          showImages = true,
          showSteps = true,
          onComplete = {
            viewModel.updateMissionItem(exercise = true)
          },
          onFeelingSelected = { feeling ->
            selectedExerciseFeeling = feeling
          }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Meditation Card
        MeditationCard(
          meditationInfo = meditationInfo,
          isCompleted = currentMission?.isBreathingDone == true,
          onComplete = {
            viewModel.updateMissionItem(breathing = true)
          }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // SECTION 6: 🤝 پیام همراه رها (Raha Companion Message)
        // ==========================================
        SectionTitleHeader(title = "🤝 پیام همراه رها", icon = Icons.Default.Favorite, color = IranianTurquoise)
        Spacer(modifier = Modifier.height(8.dp))

        PersianCard(
          backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
          borderColor = IranianTurquoise.copy(alpha = 0.4f)
        ) {
          Row(verticalAlignment = Alignment.Top) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(IranianTurquoise),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
              Text(
                text = "همراه هوشمند رها",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = IranianTurquoise
              )

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = content?.supportMessage ?: "من در تمام مراحل این مسیر کنار تو هستم. با اشتیاق ادامه بده!",
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
              )

              Spacer(modifier = Modifier.height(12.dp))

              OutlinedButton(
                onClick = {
                  viewModel.navigateTo(AppScreen.AI_CHAT)
                },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, IranianTurquoise)
              ) {
                Icon(Icons.Default.Chat, contentDescription = null, tint = IranianTurquoise, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("گفتگو با «یار رها» درباره این روز 💬", color = IranianTurquoise)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(100.dp))
      }
    }
  }
}

@Composable
fun SectionTitleHeader(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color
) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}

@Composable
fun QuoteCardItem(
  quoteText: String,
  meaningText: String
) {
  Surface(
    shape = RoundedCornerShape(14.dp),
    color = MaterialTheme.colorScheme.surfaceVariant,
    border = BorderStroke(1.dp, SoftSunGold.copy(alpha = 0.3f))
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(verticalAlignment = Alignment.Top) {
        Icon(Icons.Default.FormatQuote, contentDescription = null, tint = SoftSunGold, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = quoteText,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
      if (meaningText.isNotBlank()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = meaningText,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
