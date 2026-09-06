package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.AppScreen
import com.example.ui.RahaaViewModel
import com.example.ui.components.CustomButton
import com.example.ui.components.MorningAudioAffirmationCard
import com.example.ui.components.PersianCard
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TurquoiseSecondary
import kotlinx.coroutines.delay

@Composable
fun CalmScreen(
  viewModel: RahaaViewModel
) {
  val userProfile by viewModel.userProfile.collectAsState()
  val currentDay = userProfile?.currentDay ?: 1
  var activeSoundTab by remember { mutableStateOf("آب روان") }
  var isPlayingSound by remember { mutableStateOf(false) }

  // Guided Meditation Timer (e.g. 5 minutes = 300s)
  var meditationSecondsLeft by remember { mutableIntStateOf(300) }
  var isMeditationActive by remember { mutableStateOf(false) }

  LaunchedEffect(isMeditationActive) {
    while (isMeditationActive && meditationSecondsLeft > 0) {
      delay(1000)
      meditationSecondsLeft--
    }
  }

  val medMinutes = meditationSecondsLeft / 60
  val medSeconds = meditationSecondsLeft % 60
  val medFormatted = String.format("%02d:%02d", medMinutes, medSeconds)

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

      Text(
        text = "بخش آرامش باغ ایرانی",
        style = MaterialTheme.typography.displayMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )

      Text(
        text = "مدیتیشن، تنفس عمیق و اصوات آرامش‌بخش طبیعت",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Garden Hero Banner
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
          .clip(RoundedCornerShape(20.dp))
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_persian_garden_1785492176042),
          contentDescription = "باغ ایرانی",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
              )
            )
        )
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(16.dp)
        ) {
          Text(
            text = "فضای امن و آرامش ذهن",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "با تمرکز بر صدای آب روان و نفس‌های عمیق، استرس را از خود دور کنید.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.9f)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // 1. Guided Meditation Section
      PersianCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.SelfImprovement, contentDescription = null, tint = TurquoiseSecondary, modifier = Modifier.size(28.dp))
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "مدیتیشن کوتاه آرامش (۵ دقیقه)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = medFormatted,
              style = MaterialTheme.typography.displayLarge,
              fontWeight = FontWeight.Bold,
              color = TurquoiseSecondary
            )
            Text(
              text = "چشمان خود را ببندید و بر جریان نفس‌هایتان تمرکز کنید.",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
          Box(modifier = Modifier.weight(1f)) {
            CustomButton(
              text = if (isMeditationActive) "توقف" else "شروع مدیتیشن",
              onClick = { isMeditationActive = !isMeditationActive },
              containerColor = TurquoiseSecondary
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Box(modifier = Modifier.weight(1f)) {
            CustomButton(
              text = "بازنشانی",
              onClick = {
                meditationSecondsLeft = 300
                isMeditationActive = false
              },
              isSecondary = true
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // 2. Nature Sound Player
      PersianCard {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.GraphicEq, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "صدای طبیعت و اصوات آرامش",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
          }

          IconButton(onClick = { isPlayingSound = !isPlayingSound }) {
            Icon(
              imageVector = if (isPlayingSound) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
              contentDescription = null,
              tint = EmeraldPrimary,
              modifier = Modifier.size(36.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Sound Selection Chips
        val soundList = listOf("آب روان", "باران بهاری", "نسیم باغ", "طلوع آرام")

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          soundList.forEach { sound ->
            val isSelected = sound == activeSoundTab
            Box(
              modifier = Modifier
                .weight(1f)
                .padding(horizontal = 3.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant)
                .clickable {
                  activeSoundTab = sound
                  isPlayingSound = true
                }
                .padding(vertical = 10.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = sound,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        if (isPlayingSound) {
          Spacer(modifier = Modifier.height(12.dp))
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = EmeraldPrimary.copy(alpha = 0.12f)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.VolumeUp, contentDescription = null, tint = EmeraldPrimary)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "در حال پخش: $activeSoundTab (آرامش‌بخش و طراوت‌بخش)",
                style = MaterialTheme.typography.bodyMedium,
                color = EmeraldPrimary,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // 3. Physical Health & Exercises Section (🏃 سلامت بدن)
      PersianCard {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "🏃 سلامت و حرکت بدن (تمرین‌های پیشنهادی)",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
          }
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = EmeraldPrimary.copy(alpha = 0.15f)
          ) {
            Text(
              text = "انرژی امروز: ۶/۱۰ ⚡",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = EmeraldPrimary,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "حرکت بدنی منظم باعث ترشح اندورفین طبیعی، تقویت اراده و تخلیه استرس می‌شود.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Exercise Level Cards
        val exercises = listOf(
          Triple("🌱 کشش ۵ دقیقه‌ای مبتدی", "تمرین ملایم عضلات و ریلکسیشن صبحگاهی", "۵ دقیقه"),
          Triple("🌿 تمرین متوسط تعادل و تنفس", "حرکات کششی و نرمش برای بالابردن سطح انرژی", "۱۰ دقیقه"),
          Triple("💪 ورزش پیشرفته هوازی و قدرتی", "تمرین‌های تقویت اراده و استقامت بدنی", "۱۵ دقیقه")
        )

        exercises.forEach { (title, desc, duration) ->
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = title,
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = desc,
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = EmeraldPrimary,
                modifier = Modifier.clickable { /* Trigger exercise */ }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = duration,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(100.dp))
    }
  }
}
