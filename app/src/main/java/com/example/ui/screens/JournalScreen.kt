package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.RahaaViewModel
import com.example.ui.components.CustomButton
import com.example.ui.components.JournalCard
import com.example.ui.components.PersianCard
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAmber
import com.example.ui.theme.UrgeAlertRed

@Composable
fun JournalScreen(
  viewModel: RahaaViewModel
) {
  val journalEntries by viewModel.allJournalEntries.collectAsState()

  var selectedMood by remember { mutableStateOf("خوب") }
  var craving by remember { mutableFloatStateOf(2f) }
  var sleep by remember { mutableFloatStateOf(7f) }
  var energy by remember { mutableFloatStateOf(7f) }
  var noteText by remember { mutableStateOf("") }
  var successText by remember { mutableStateOf("") }
  var isSavedNoticeVisible by remember { mutableStateOf(false) }

  val moodOptions = listOf("عالی", "خوب", "معمولی", "سخت")

  // Calculated adaptive recommendation based on live sliders
  val liveAdaptiveAdvice = remember(selectedMood, craving, sleep, energy) {
    when {
      craving >= 8f ->
        "🌿 الان زمان تصمیم بزرگ نیست؛ فقط این لحظه را مدیریت کنیم.\n\nاول:\n✓ از محیط محرک فاصله بگیر\n✓ چند نفس آرام\n✓ یک لیوان آب\n\nمن کنار تو هستم."
      sleep < 4f ->
        "امروز بدن تو خسته است.\nاولویت امروز:\nاستراحت، آب کافی و فشار کمتر."
      energy <= 4f && (selectedMood == "سخت" || selectedMood == "معمولی") ->
        "امروز فقط یک کار کوچک انجام بده.\nکوچک‌ترین قدم هم پیشرفت است."
      else ->
        "حالت متعادل و عالی است! به مسیر رشد و انضباط امروز ادامه بده 🌱"
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 18.dp)
    ) {
      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "وضعیت امروز من 🌿",
        style = MaterialTheme.typography.displayMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )

      Text(
        text = "ثبت حالت روحی، شدت وسوسه، کیفیت خواب و میزان انرژی امروز",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(16.dp))

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
      ) {
        // Entry Input Form
        item {
          PersianCard {
            Text(
              text = "😊 حال روحی امروز من:",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mood chip selector
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              moodOptions.forEach { mood ->
                val isSelected = mood == selectedMood
                val chipColor = when (mood) {
                  "عالی" -> EmeraldPrimary
                  "خوب" -> MaterialTheme.colorScheme.secondary
                  "معمولی" -> GoldAmber
                  else -> UrgeAlertRed
                }

                Box(
                  modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 3.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) chipColor else MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { selectedMood = mood }
                    .padding(vertical = 10.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = mood,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Craving Slider
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "🔥 شدت وسوسه:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "${craving.toInt()} از ۱۰",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (craving >= 7f) UrgeAlertRed else MaterialTheme.colorScheme.primary
              )
            }

            Slider(
              value = craving,
              onValueChange = { craving = it },
              valueRange = 0f..10f,
              steps = 9
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Sleep Slider
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "😴 خواب دیشب (ساعت/کیفیت):",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "${sleep.toInt()} از ۱۰",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
              )
            }

            Slider(
              value = sleep,
              onValueChange = { sleep = it },
              valueRange = 0f..10f,
              steps = 9
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Energy Slider
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "⚡ انرژی امروز:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "${energy.toInt()} از ۱۰",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GoldAmber
              )
            }

            Slider(
              value = energy,
              onValueChange = { energy = it },
              valueRange = 0f..10f,
              steps = 9
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Journal note (حرف دل من)
            OutlinedTextField(
              value = noteText,
              onValueChange = { noteText = it },
              label = { Text("✍ حرف دل من / امروز چه احساسی داری؟") },
              placeholder = { Text("امروز چطور گذشت؟...") },
              modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
              shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Success note
            OutlinedTextField(
              value = successText,
              onValueChange = { successText = it },
              label = { Text("پیروزی یا قدم مثبت امروز") },
              leadingIcon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldPrimary) },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
              text = "ثبت وضعیت امروز",
              onClick = {
                val textToAnalyze = noteText
                viewModel.addJournalEntry(
                  mood = selectedMood,
                  urgeIntensity = craving.toInt(),
                  noteText = noteText,
                  successText = successText,
                  sleepHours = sleep.toInt(),
                  energyLevel = energy.toInt()
                )
                if (textToAnalyze.isNotBlank()) {
                  viewModel.analyzeJournalEmotion(textToAnalyze)
                }
                noteText = ""
                successText = ""
                isSavedNoticeVisible = true
              },
              icon = Icons.Default.Save
            )

            if (isSavedNoticeVisible) {
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "✓ وضعیت امروز شما با موفقیت ثبت شد و به «یار رها» ارسال گردید.",
                style = MaterialTheme.typography.labelMedium,
                color = EmeraldPrimary,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Live Adaptive Recommendation Card from Yar-e Rahaa
        item {
          Spacer(modifier = Modifier.height(16.dp))
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🌱", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "پیشنهاد اختصاصی «یار رها» بر اساس شرایط امروز شما:",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = liveAdaptiveAdvice,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                lineHeight = 22.sp
              )

              Spacer(modifier = Modifier.height(12.dp))
              OutlinedButton(
                onClick = { viewModel.navigateTo(com.example.ui.AppScreen.AI_CHAT) },
                shape = RoundedCornerShape(12.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Psychology,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("گفتگو با یار رها درباره وضعیت امروز")
              }
            }
          }
        }

        // AI Emotion Analysis Card
        item {
          val emotionResult by viewModel.aiEmotionAnalysis.collectAsState()
          emotionResult?.let { result ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(text = "💡", fontSize = 20.sp)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "تحلیل عاطفی متن: ${result.dominantEmotion}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                  )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = result.summaryAdvice,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSecondaryContainer,
                  lineHeight = 22.sp
                )
              }
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(20.dp))
          Text(
            text = "تاریخچه وضعیت روزانه من",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(10.dp))
        }

        if (journalEntries.isEmpty()) {
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Icon(
                  imageVector = Icons.Default.MenuBook,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.outline,
                  modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = "هنوز هیچ وضعیتی ثبت نکرده‌اید.",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        } else {
          items(journalEntries) { entry ->
            JournalCard(
              dateString = entry.dateString,
              mood = entry.mood,
              urgeIntensity = entry.urgeIntensity,
              noteText = entry.noteText,
              successText = entry.successText,
              sleepHours = entry.sleepHours,
              energyLevel = entry.energyLevel,
              onDelete = { viewModel.deleteJournalEntry(entry.id) }
            )
          }
        }
      }
    }
  }
}
