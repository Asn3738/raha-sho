package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.RahaaViewModel
import com.example.ui.components.CustomButton
import com.example.ui.components.PersianCard
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TurquoiseSecondary
import com.example.ui.theme.UrgeAlertRed
import kotlinx.coroutines.delay

@Composable
fun UrgeEmergencyScreen(
  viewModel: RahaaViewModel
) {
  // 10-minute timer = 600 seconds
  var totalSecondsLeft by remember { mutableIntStateOf(600) }
  var isTimerRunning by remember { mutableStateOf(true) }
  var resultSavedMessage by remember { mutableStateOf<String?>(null) }

  // Breathing phase state: 4s Inhale (دم), 4s Hold (نگه داشتن), 6s Exhale (بازدم) = 14s cycle
  var breathPhaseText by remember { mutableStateOf("دم (۴ ثانیه)") }

  val infiniteTransition = rememberInfiniteTransition(label = "breath")
  val breathScale by infiniteTransition.animateFloat(
    initialValue = 0.85f,
    targetValue = 1.25f,
    animationSpec = infiniteRepeatable(
      animation = tween(4000, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "breath_scale"
  )

  // Timer Coroutine
  LaunchedEffect(isTimerRunning) {
    while (isTimerRunning && totalSecondsLeft > 0) {
      delay(1000)
      totalSecondsLeft--
    }
  }

  // Breathing text cycle updater
  LaunchedEffect(Unit) {
    while (true) {
      breathPhaseText = "دم (عميق ميكشيم... ۴ ثانیه)"
      delay(4000)
      breathPhaseText = "نگه‌داشتن تنفس... (۴ ثانیه)"
      delay(4000)
      breathPhaseText = "بازدم آرام... (۶ ثانیه)"
      delay(6000)
    }
  }

  val minutes = totalSecondsLeft / 60
  val seconds = totalSecondsLeft % 60
  val formattedTime = String.format("%02d:%02d", minutes, seconds)

  val supportiveMessages = listOf(
    "تو قوی‌تر از این لحظه وسوسه هستی.",
    "وسوسه مانند موج دریاست؛ می‌آید، اوج می‌گیرد و می‌گذرد.",
    "فقط به ۱۰ دقیقه آینده فکر کن و عمیق نفس بکش.",
    "پاکی امروز تو، ساختن فرداهای روشن توست.",
    "اراده تو بزرگ‌تر از هر عادت کوتاه‌مدت است."
  )
  var messageIndex by remember { mutableIntStateOf(0) }

  LaunchedEffect(Unit) {
    while (true) {
      delay(5000)
      messageIndex = (messageIndex + 1) % supportiveMessages.size
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
        .padding(18.dp)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(10.dp))

      // Header Back button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = { viewModel.navigateTo(AppScreen.HOME) }) {
          Icon(Icons.Default.ArrowForward, contentDescription = "بازگشت")
        }

        Text(
          text = "پشتیبانی اضطراری وسوسه",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(48.dp))
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Big Title Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "این لحظه می‌گذرد",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "شدت وسوسه معمولاً پس از ۱۰ دقیقه کاهش می‌یابد. آرامش خود را حفظ کنید.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // 10-Minute Timer & Breathing Visualizer
      Box(
        modifier = Modifier
          .size(220.dp)
          .scale(breathScale)
          .clip(CircleShape)
          .background(
            Brush.radialGradient(
              colors = listOf(
                TurquoiseSecondary.copy(alpha = 0.4f),
                EmeraldPrimary.copy(alpha = 0.2f),
                Color.Transparent
              )
            )
          )
          .border(4.dp, TurquoiseSecondary, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = formattedTime,
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = breathPhaseText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = TurquoiseSecondary
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Timer Controls
      Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
      ) {
        OutlinedButton(
          onClick = { isTimerRunning = !isTimerRunning },
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(
            imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = null
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(if (isTimerRunning) "توقف تایمر" else "ادامه تایمر")
        }

        Spacer(modifier = Modifier.width(12.dp))

        OutlinedButton(
          onClick = {
            totalSecondsLeft = 600
            isTimerRunning = true
          },
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Refresh, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text("شروع مجدد")
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Supportive Message Carousel Card
      PersianCard(
        borderColor = TurquoiseSecondary.copy(alpha = 0.5f)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Psychology,
            contentDescription = null,
            tint = TurquoiseSecondary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "پیام حمایتی ذهن‌آگاهی",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TurquoiseSecondary
          )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = supportiveMessages[messageIndex],
          style = MaterialTheme.typography.bodyLarge,
          lineHeight = 26.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Dedicated Substance Emergency Protocol Guidance Card
      val userProfile by viewModel.userProfile.collectAsState()
      val habitType = userProfile?.habitType ?: "ترک عادتهای مخرب"
      val protocol = com.example.data.model.SubstanceProtocolManager.getProtocol(habitType)

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.MedicalServices,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "تکنیک اختصاصی کنترل ولع ($habitType)",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "⚡ ${protocol.emergencyCopingTechnique}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 22.sp
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "🥗 توصیه مایعات و هیدراتاسیون: ${protocol.nutritionAdvice}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
          )

          Spacer(modifier = Modifier.height(8.dp))

          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
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

      Spacer(modifier = Modifier.height(16.dp))

      // AI Companion Crisis Chat Button
      CustomButton(
        text = "گفتگو با یار رها (پشتیبانی هوشمند) 🌱",
        onClick = {
          viewModel.sendAiChatMessage("الان شدیداً احساس وسوسه می‌کنم و به کمک و آرامش فکری احتیاج دارم.")
          viewModel.navigateTo(AppScreen.AI_CHAT)
        },
        containerColor = MaterialTheme.colorScheme.secondary,
        icon = Icons.Default.Psychology
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Result Logging Section
      if (resultSavedMessage != null) {
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = EmeraldPrimary.copy(alpha = 0.15f),
          border = BorderStroke(1.dp, EmeraldPrimary)
        ) {
          Text(
            text = resultSavedMessage!!,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = EmeraldPrimary,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
          )
        }
      } else {
        Text(
          text = "ثبت نتیجه عبور از این لحظه:",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
          Box(modifier = Modifier.weight(1f)) {
            CustomButton(
              text = "موفق شدم! 💪",
              onClick = {
                val passedSecs = 600 - totalSecondsLeft
                viewModel.logUrgeEmergency(
                  durationSeconds = passedSecs,
                  passedSuccessfully = true,
                  note = "عبور موفق از وسوسه"
                )
                resultSavedMessage = "پیروزی بزرگ! این موفقیت در سابقه شما ثبت شد. آفرین به اراده قوی تو! 🌿"
              },
              containerColor = EmeraldPrimary
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Box(modifier = Modifier.weight(1f)) {
            CustomButton(
              text = "نیاز به کمک دارم 🤝",
              onClick = {
                val passedSecs = 600 - totalSecondsLeft
                viewModel.logUrgeEmergency(
                  durationSeconds = passedSecs,
                  passedSuccessfully = false,
                  note = "نیاز به پشتیبانی"
                )
                viewModel.navigateTo(AppScreen.CALM)
              },
              isSecondary = true,
              containerColor = UrgeAlertRed.copy(alpha = 0.15f),
              contentColor = UrgeAlertRed
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(40.dp))
    }
  }
}
