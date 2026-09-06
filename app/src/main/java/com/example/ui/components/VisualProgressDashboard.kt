package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAmber

/**
 * Visual Progress Dashboard component for tracking 30-day recovery journey,
 * milestones, and sobriety streaks using custom Compose Canvas charts and cards.
 */
@Composable
fun VisualProgressDashboard(
  currentDay: Int,
  totalDays: Int = 30,
  streakDays: Int = currentDay,
  onDayClick: (Int) -> Unit = {}
) {
  var selectedTab by remember { mutableStateOf(0) } // 0: 30-Day Grid, 1: Streak Graph, 2: Milestones

  PersianCard(borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)) {
    Column {
      // Header Title & Streak Badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(EmeraldPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Analytics,
              contentDescription = null,
              tint = EmeraldPrimary,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "داشبورد تصویری پیشرفت و پاکی",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "روند ۳۰ روزه، نقاط عطف و زنجیره پایداری",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        // Streak Chip
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = GoldAmber.copy(alpha = 0.18f),
          border = BorderStroke(1.dp, GoldAmber.copy(alpha = 0.5f))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.LocalFireDepartment,
              contentDescription = null,
              tint = GoldAmber,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "$streakDays روز پاکی",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = GoldAmber
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Navigation Tab Row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
          .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        val tabs = listOf("جدول ۳۰ روزه", "نمودار زنجیره پاکی", "نقاط عطف و مراحل")
        tabs.forEachIndexed { index, title ->
          val isSelected = selectedTab == index
          Surface(
            onClick = { selectedTab = index },
            modifier = Modifier
              .weight(1f)
              .height(38.dp),
            shape = RoundedCornerShape(10.dp),
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                textAlign = TextAlign.Center
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      when (selectedTab) {
        0 -> ThirtyDayGridSection(currentDay = currentDay, totalDays = totalDays, onDayClick = onDayClick)
        1 -> SobrietyStreakGraph(currentDay = currentDay, totalDays = totalDays)
        2 -> MilestonePhasesSection(currentDay = currentDay)
      }
    }
  }
}

@Composable
private fun ThirtyDayGridSection(
  currentDay: Int,
  totalDays: Int,
  onDayClick: (Int) -> Unit
) {
  Column {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "نمای ۳۰ روزه مسیر رهایی:",
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )
      Text(
        text = "پیشرفت: ${((currentDay.toFloat() / totalDays) * 100).toInt()}%",
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Progress Bar
    val progressAnim by animateFloatAsState(
      targetValue = (currentDay.toFloat() / totalDays).coerceIn(0f, 1f),
      animationSpec = tween(durationMillis = 800), label = "gridProgress"
    )

    LinearProgressIndicator(
      progress = { progressAnim },
      modifier = Modifier
        .fillMaxWidth()
        .height(10.dp)
        .clip(RoundedCornerShape(5.dp)),
      color = EmeraldPrimary,
      trackColor = EmeraldPrimary.copy(alpha = 0.15f)
    )

    Spacer(modifier = Modifier.height(14.dp))

    // 30 Days Grid (6 Columns x 5 Rows)
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      for (row in 0 until 5) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          for (col in 0 until 6) {
            val dayNum = row * 6 + col + 1
            if (dayNum <= totalDays) {
              val isPassed = dayNum < currentDay
              val isCurrent = dayNum == currentDay
              val isMilestone = dayNum in listOf(7, 14, 21, 30)

              val bgColor = when {
                isPassed -> EmeraldPrimary.copy(alpha = 0.85f)
                isCurrent -> GoldAmber
                isMilestone -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
              }

              val textColor = when {
                isPassed -> Color.White
                isCurrent -> Color.Black
                else -> MaterialTheme.colorScheme.onSurface
              }

              Surface(
                onClick = { onDayClick(dayNum) },
                shape = RoundedCornerShape(10.dp),
                color = bgColor,
                border = if (isCurrent) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                modifier = Modifier
                  .weight(1f)
                  .height(42.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                  ) {
                    if (isPassed) {
                      Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                      )
                      Spacer(modifier = Modifier.width(2.dp))
                    } else if (isMilestone && !isPassed && !isCurrent) {
                      Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = GoldAmber,
                        modifier = Modifier.size(12.dp)
                      )
                      Spacer(modifier = Modifier.width(2.dp))
                    }
                    Text(
                      text = "$dayNum",
                      style = MaterialTheme.typography.labelMedium,
                      fontWeight = if (isCurrent || isMilestone) FontWeight.Bold else FontWeight.Normal,
                      color = textColor
                    )
                  }
                }
              }
            } else {
              Spacer(modifier = Modifier.weight(1f))
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Legend
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically
    ) {
      LegendItem(color = EmeraldPrimary, label = "تکمیل شده")
      LegendItem(color = GoldAmber, label = "امروز")
      LegendItem(color = MaterialTheme.colorScheme.primaryContainer, label = "نقطه عطف ⭐")
      LegendItem(color = MaterialTheme.colorScheme.surfaceVariant, label = "آینده")
    }
  }
}

@Composable
private fun LegendItem(color: Color, label: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(
      modifier = Modifier
        .size(10.dp)
        .clip(CircleShape)
        .background(color)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

@Composable
private fun SobrietyStreakGraph(
  currentDay: Int,
  totalDays: Int
) {
  Column {
    Text(
      text = "نمودار استمرار و پایداری پاکی (هفتگی):",
      style = MaterialTheme.typography.labelLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = "میزان پایبندی و پیشرفت در هفته‌های ۳۰ روزه",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(14.dp))

    // Canvas Chart for Weekly Streaks
    val primaryColor = EmeraldPrimary
    val goldColor = GoldAmber
    val gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)

    val weekPercentages = remember(currentDay) {
      listOf(
        (currentDay.coerceIn(0, 7) / 7f) * 100f,
        ((currentDay - 7).coerceIn(0, 7) / 7f) * 100f,
        ((currentDay - 14).coerceIn(0, 7) / 7f) * 100f,
        ((currentDay - 21).coerceIn(0, 9) / 9f) * 100f
      )
    }

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(180.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        .padding(16.dp)
    ) {
      Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val barWidth = width / 9f

        // Draw horizontal grid lines
        for (i in 0..4) {
          val y = height * (i / 4f)
          drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = 1f
          )
        }

        // Draw Bars for each week
        val weekLabels = listOf("هفته ۱", "هفته ۲", "هفته ۳", "هفته ۴")
        weekPercentages.forEachIndexed { index, pct ->
          val x = (index * 2 + 1) * barWidth
          val barHeight = (pct / 100f) * height
          val topY = height - barHeight

          val brush = Brush.verticalGradient(
            colors = listOf(
              if (pct > 0) primaryColor else primaryColor.copy(alpha = 0.3f),
              if (pct > 0) goldColor else primaryColor.copy(alpha = 0.1f)
            )
          )

          drawRoundRect(
            brush = brush,
            topLeft = Offset(x, topY),
            size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
            cornerRadius = CornerRadius(12f, 12f)
          )
        }
      }

      // Overlaid Week Values
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.BottomCenter)
          .padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        listOf("هفته ۱\n(روز ۱-۷)", "هفته ۲\n(روز ۸-۱۴)", "هفته ۳\n(روز ۱۵-۲۱)", "هفته ۴\n(روز ۲۲-۳۰)").forEachIndexed { idx, label ->
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "${weekPercentages[idx].toInt()}%",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = if (weekPercentages[idx] > 0) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = label,
              style = MaterialTheme.typography.labelSmall,
              fontSize = 10.sp,
              textAlign = TextAlign.Center,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}

@Composable
private fun MilestonePhasesSection(currentDay: Int) {
  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text(
      text = "مراحل و مدال‌های رهایی:",
      style = MaterialTheme.typography.labelLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.primary
    )

    val phases = listOf(
      PhaseMilestoneData(1, "مرحله اول: شروع دوباره", "روز ۱ تا ۷", "سم‌زدایی اولیه، غلبه بر وسوسه‌های حاد و ایجاد نظم روزانه", 7),
      PhaseMilestoneData(2, "مرحله دوم: تسلط بر ذهن", "روز ۸ تا ۱۴", "کنترل گفتگوی درونی، تعویض عادت‌های منفی و پایداری خلق", 14),
      PhaseMilestoneData(3, "مرحله سوم: تثبیت شخصیت", "روز ۱۵ تا ۲۱", "تثبیت انضباط شخصی، تقویت اراده و صمیمیت با خود", 21),
      PhaseMilestoneData(4, "مرحله چهارم: پرواز رهایی", "روز ۲۲ تا ۳۰", "تثبیت همیشگی پاکی، استقلال روانی و شروع زندگی جدید", 30)
    )

    phases.forEach { phase ->
      val isUnlocked = currentDay >= phase.endDay
      val isCurrentPhase = currentDay in (phase.endDay - 6)..phase.endDay

      Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isUnlocked) EmeraldPrimary.copy(alpha = 0.12f) else if (isCurrentPhase) GoldAmber.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        border = BorderStroke(
          width = 1.dp,
          color = if (isUnlocked) EmeraldPrimary else if (isCurrentPhase) GoldAmber else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        ),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(if (isUnlocked) EmeraldPrimary else if (isCurrentPhase) GoldAmber else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isUnlocked) Icons.Default.EmojiEvents else if (isCurrentPhase) Icons.Default.DirectionsRun else Icons.Default.Lock,
              contentDescription = null,
              tint = if (isUnlocked || isCurrentPhase) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(22.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = phase.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = phase.range,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (isUnlocked) EmeraldPrimary else if (isCurrentPhase) GoldAmber else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = phase.description,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 18.sp
            )
          }
        }
      }
    }
  }
}

private data class PhaseMilestoneData(
  val phaseNumber: Int,
  val title: String,
  val range: String,
  val description: String,
  val endDay: Int
)
