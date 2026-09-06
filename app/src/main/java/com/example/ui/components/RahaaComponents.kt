package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.DailyExerciseInfo
import com.example.data.local.DailyMeditationInfo
import com.example.ui.theme.*
import kotlinx.coroutines.delay

// 1. CustomButton
@Composable
fun CustomButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: ImageVector? = null,
  enabled: Boolean = true,
  isSecondary: Boolean = false,
  containerColor: Color? = null,
  contentColor: Color? = null,
  isLoading: Boolean = false
) {
  val shape = RoundedCornerShape(24.dp)
  val baseContainerColor = if (isSecondary) (containerColor ?: MaterialTheme.colorScheme.surfaceVariant) else (containerColor ?: MaterialTheme.colorScheme.primary)
  val activeColor = if (enabled) baseContainerColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
  val activeContentColor = contentColor ?: if (enabled) (if (isSecondary) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)

  Surface(
    onClick = { if (enabled) onClick() },
    enabled = enabled,
    modifier = modifier
      .fillMaxWidth()
      .height(56.dp)
      .shadow(if (!isSecondary && enabled) 4.dp else 0.dp, shape)
      .testTag("custom_button_$text"),
    shape = shape,
    color = activeColor,
    contentColor = activeContentColor,
    border = if (isSecondary) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)) else null
  ) {
    Box(
      modifier = Modifier
        .padding(horizontal = 20.dp, vertical = 12.dp),
      contentAlignment = Alignment.Center
    ) {
      if (isLoading) {
        CircularProgressIndicator(
          modifier = Modifier.size(24.dp),
          color = MaterialTheme.colorScheme.onPrimary,
          strokeWidth = 2.5.dp
        )
      } else {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          if (icon != null) {
            Icon(
              imageVector = icon,
              contentDescription = null,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
          }
          Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

// 2. PersianCard
@Composable
fun PersianCard(
  modifier: Modifier = Modifier,
  borderColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
  backgroundColor: Color = MaterialTheme.colorScheme.surface,
  elevation: Dp = 1.dp,
  content: @Composable ColumnScope.() -> Unit
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .shadow(elevation, RoundedCornerShape(28.dp))
      .border(1.dp, borderColor, RoundedCornerShape(28.dp)),
    shape = RoundedCornerShape(28.dp),
    colors = CardDefaults.cardColors(containerColor = backgroundColor)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
      content()
    }
  }
}

// 3. ProgressCircle
@Composable
fun ProgressCircle(
  currentDay: Int,
  totalDays: Int = 30,
  modifier: Modifier = Modifier,
  strokeWidth: Dp = 10.dp
) {
  val progress = (currentDay.toFloat() / totalDays.toFloat()).coerceIn(0f, 1f)
  val animatedProgress by animateFloatAsState(
    targetValue = progress,
    animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
    label = "progress_anim"
  )

  val primaryColor = MaterialTheme.colorScheme.primary
  val trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)

  Card(
    modifier = modifier
      .fillMaxWidth()
      .shadow(2.dp, RoundedCornerShape(28.dp)),
    shape = RoundedCornerShape(28.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Box(
        modifier = Modifier.size(96.dp),
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val strokePx = strokeWidth.toPx()
          
          // Background Track
          drawCircle(
            color = trackColor,
            style = Stroke(width = strokePx, cap = StrokeCap.Round)
          )

          // Progress Arc
          drawArc(
            color = primaryColor,
            startAngle = -90f,
            sweepAngle = 360f * animatedProgress,
            useCenter = false,
            style = Stroke(width = strokePx, cap = StrokeCap.Round)
          )
        }

        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(
            text = "${(animatedProgress * 100).toInt()}٪",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }

      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier.weight(1f)) {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.primaryContainer
        ) {
          Text(
            text = "روز $currentDay از $totalDays",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "مسیر بازگشت به خود واقعی توست. با اراده و آرامش ادامه بده!",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 22.sp
        )
      }
    }
  }
}

// 4. DailyMissionCard
@Composable
fun DailyMissionCard(
  title: String,
  isCompleted: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  icon: ImageVector,
  guideText: String = "",
  modifier: Modifier = Modifier
) {
  var isExpanded by remember { mutableStateOf(false) }

  Surface(
    onClick = { isExpanded = !isExpanded },
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    shape = RoundedCornerShape(20.dp),
    color = if (isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f) else MaterialTheme.colorScheme.surface,
    border = BorderStroke(
      1.dp,
      if (isCompleted) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    )
  ) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(
                if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
              )
              .border(
                1.5.dp,
                if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                RoundedCornerShape(10.dp)
              ),
            contentAlignment = Alignment.Center
          ) {
            if (isCompleted) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
              )
            } else {
              Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
              )
            }
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = title,
              style = MaterialTheme.typography.bodyLarge,
              fontWeight = if (isCompleted) FontWeight.SemiBold else FontWeight.Bold,
              color = if (isCompleted) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = if (isExpanded) "برای پنهان‌سازی راهنما کلیک کنید" else "برای مشاهده روش انجام اصولی و ایمنی کلیک کنید 👈",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = { isExpanded = !isExpanded },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
              contentDescription = "راهنمای ماموریت",
              tint = MaterialTheme.colorScheme.primary
            )
          }

          Checkbox(
            checked = isCompleted,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
              checkedColor = MaterialTheme.colorScheme.primary,
              uncheckedColor = MaterialTheme.colorScheme.outline
            )
          )
        }
      }

      // Expandable Dropdown Guide Section
      AnimatedVisibility(visible = isExpanded) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
        ) {
          HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
          Spacer(modifier = Modifier.height(10.dp))

          Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.HelpOutline,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "راهنمای چگونگی انجام اصولی و ایمنی:",
                  style = MaterialTheme.typography.labelLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
              }

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = guideText.ifBlank { "این ماموریت را به صورت منظم و آرام در طول روز انجام دهید تا عادت‌های مثبت در ذهن شما نهادینه شوند." },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
              )
            }
          }
        }
      }
    }
  }
}

// 5. MotivationCard
@Composable
fun MotivationCard(
  quote: String = "«هر روز یک قدم، هر قدم یک پیروزی است.»",
  author: String = "جمله روز",
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .shadow(4.dp, RoundedCornerShape(24.dp)),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primary
    )
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
      Column {
        Text(
          text = quote,
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.Medium,
          lineHeight = 26.sp,
          color = Color.White
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = author.uppercase(),
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = Color.White.copy(alpha = 0.75f)
        )
      }
    }
  }
}

// 6. AchievementBadge
@Composable
fun AchievementBadge(
  title: String,
  description: String,
  iconType: String,
  isUnlocked: Boolean,
  unlockedDate: String = "",
  onClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val badgeIcon = when (iconType) {
    "sprout" -> Icons.Default.Spa
    "cypress" -> Icons.Default.Park
    "willpower" -> Icons.Default.LocalFireDepartment
    "freedom" -> Icons.Default.Flight
    else -> Icons.Default.EmojiEvents
  }

  val badgeColor = if (isUnlocked) SoftSunGold else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

  Card(
    onClick = onClick,
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    ),
    border = BorderStroke(
      1.dp,
      if (isUnlocked) SoftSunGold.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    )
  ) {
    Row(
      modifier = Modifier
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(56.dp)
          .clip(CircleShape)
          .background(
            if (isUnlocked) SoftSunGoldContainer else MaterialTheme.colorScheme.surfaceVariant
          )
          .border(2.dp, badgeColor, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = badgeIcon,
          contentDescription = null,
          tint = badgeColor,
          modifier = Modifier.size(32.dp)
        )
      }

      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          if (isUnlocked) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = CypressGreenContainer
            ) {
              Text(
                text = "دریافت شده $unlockedDate",
                style = MaterialTheme.typography.labelMedium,
                color = CypressGreen,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
              )
            }
          } else {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = "قفل شده",
              tint = MaterialTheme.colorScheme.outline,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = description,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

// 7. JournalCard
@Composable
fun JournalCard(
  dateString: String,
  mood: String,
  urgeIntensity: Int,
  noteText: String,
  successText: String,
  sleepHours: Int = 7,
  energyLevel: Int = 7,
  onDelete: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val moodColor = when (mood) {
    "عالی", "خیلی خوب" -> CypressGreen
    "خوب" -> IranianTurquoise
    "معمولی", "متوسط" -> SoftSunGold
    else -> UrgeAlertRed
  }

  PersianCard(
    modifier = modifier.padding(vertical = 6.dp),
    borderColor = moodColor.copy(alpha = 0.3f)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = moodColor.copy(alpha = 0.15f)
        ) {
          Text(
            text = "حال: $mood",
            style = MaterialTheme.typography.labelLarge,
            color = moodColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.surfaceVariant
        ) {
          Text(
            text = "🔥 وسوسه: $urgeIntensity/۱۰",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
          )
        }
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.surfaceVariant
        ) {
          Text(
            text = "😴 خواب: $sleepHours",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
          )
        }
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.surfaceVariant
        ) {
          Text(
            text = "⚡ انرژی: $energyLevel",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
          )
        }
      }

      IconButton(onClick = onDelete) {
        Icon(
          imageVector = Icons.Outlined.Delete,
          contentDescription = "حذف",
          tint = MaterialTheme.colorScheme.outline
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = dateString,
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.outline
    )

    if (noteText.isNotBlank()) {
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "یادداشت: $noteText",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
      )
    }

    if (successText.isNotBlank()) {
      Spacer(modifier = Modifier.height(6.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = null,
          tint = CypressGreen,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "موفقیت: $successText",
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.SemiBold,
          color = CypressGreen
        )
      }
    }
  }
}

// 8. ThemeSelector
@Composable
fun ThemeSelector(
  selectedMode: ThemeMode,
  onModeSelected: (ThemeMode) -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant)
      .padding(4.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    ThemeMode.values().forEach { mode ->
      val isSelected = mode == selectedMode
      val text = when (mode) {
        ThemeMode.LIGHT -> "روشن"
        ThemeMode.DARK -> "تاریک"
        ThemeMode.SYSTEM -> "سیستم"
      }
      val icon = when (mode) {
        ThemeMode.LIGHT -> Icons.Default.WbSunny
        ThemeMode.DARK -> Icons.Default.NightsStay
        ThemeMode.SYSTEM -> Icons.Default.SettingsSuggest
      }

      Box(
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(10.dp))
          .background(if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent)
          .clickable { onModeSelected(mode) }
          .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}

// 9. EmergencyButton
@Composable
fun EmergencyButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val scale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.02f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scale"
  )

  Surface(
    onClick = onClick,
    modifier = modifier
      .fillMaxWidth()
      .scale(scale)
      .shadow(6.dp, RoundedCornerShape(24.dp))
      .testTag("emergency_button"),
    shape = RoundedCornerShape(24.dp),
    color = UrgeAlertRed
  ) {
    Row(
      modifier = Modifier
        .background(UrgeAlertRed)
        .padding(horizontal = 20.dp, vertical = 18.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Text(
        text = "🔥  وقتی وسوسه شدم",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )
    }
  }
}

// 10. TodayStatusOverviewCard (🌱 وضعیت امروز)
@Composable
fun TodayStatusOverviewCard(
  cleanDays: Int = 12,
  mood: String = "خوب",
  craving: String = "کم",
  sleep: String = "7 ساعت",
  energy: String = "متوسط",
  onLogStatusClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val cravingColor = when {
    craving.contains("کم") || craving.contains("هیچ") || craving.contains("پایین") -> Color(0xFF2E7D32)
    craving.contains("متوسط") -> Color(0xFFF57C00)
    craving.contains("زیاد") || craving.contains("شدید") || craving.contains("بالا") -> Color(0xFFD32F2F)
    else -> Color(0xFF2E7D32)
  }

  val moodColor = when {
    mood.contains("عالی") || mood.contains("خوب") || mood.contains("شاد") -> Color(0xFF2E7D32)
    mood.contains("معمولی") || mood.contains("متوسط") -> Color(0xFFF57C00)
    else -> Color(0xFFD32F2F)
  }

  val energyColor = when {
    energy.contains("عالی") || energy.contains("زیاد") || energy.contains("بالا") -> Color(0xFF2E7D32)
    energy.contains("متوسط") || energy.contains("معمولی") -> Color(0xFFF57C00)
    else -> Color(0xFFD32F2F)
  }

  PersianCard(
    modifier = modifier,
    borderColor = EmeraldPrimary.copy(alpha = 0.35f),
    backgroundColor = MaterialTheme.colorScheme.surface
  ) {
    // Header Row
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "🌱", fontSize = 22.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "وضعیت امروز",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      Surface(
        shape = RoundedCornerShape(12.dp),
        color = cravingColor.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, cravingColor.copy(alpha = 0.3f))
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(cravingColor)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "وسوسه $craving",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = cravingColor
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // 4 Metrics Grid (حال, وسوسه, خواب, انرژی)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // 1. Mood (😊 حال)
      StatusMetricBox(
        modifier = Modifier.weight(1f),
        emoji = "😊",
        label = "حال",
        value = mood,
        accentColor = moodColor
      )

      Spacer(modifier = Modifier.width(6.dp))

      // 2. Craving (🔥 وسوسه)
      StatusMetricBox(
        modifier = Modifier.weight(1f),
        emoji = "🔥",
        label = "وسوسه",
        value = craving,
        accentColor = cravingColor
      )

      Spacer(modifier = Modifier.width(6.dp))

      // 3. Sleep (😴 خواب)
      StatusMetricBox(
        modifier = Modifier.weight(1f),
        emoji = "😴",
        label = "خواب",
        value = sleep,
        accentColor = TurquoiseSecondary
      )

      Spacer(modifier = Modifier.width(6.dp))

      // 4. Energy (⚡ انرژی)
      StatusMetricBox(
        modifier = Modifier.weight(1f),
        emoji = "⚡",
        label = "انرژی",
        value = energy,
        accentColor = energyColor
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Button: ثبت وضعیت امروز
    Button(
      onClick = onLogStatusClick,
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp),
      shape = RoundedCornerShape(16.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = EmeraldPrimary,
        contentColor = Color.White
      )
    ) {
      Icon(
        imageVector = Icons.Default.EditNote,
        contentDescription = null,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "ثبت وضعیت امروز",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Composable
private fun StatusMetricBox(
  modifier: Modifier = Modifier,
  emoji: String,
  label: String,
  value: String,
  accentColor: Color
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = accentColor.copy(alpha = 0.08f),
    border = BorderStroke(1.dp, accentColor.copy(alpha = 0.25f)),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = "$emoji $label:",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 11.sp
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = accentColor,
        fontSize = 13.sp
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayStatusLogDialog(
  currentMood: String,
  currentCraving: String,
  currentSleep: String,
  currentEnergy: String,
  onDismiss: () -> Unit,
  onSaveStatus: (mood: String, craving: String, sleep: String, energy: String) -> Unit
) {
  var mood by remember { mutableStateOf(currentMood) }
  var craving by remember { mutableStateOf(currentCraving) }
  var sleep by remember { mutableStateOf(currentSleep) }
  var energy by remember { mutableStateOf(currentEnergy) }

  val moodOptions = listOf("عالی", "خوب", "معمولی", "بی‌حوصله")
  val cravingOptions = listOf("کم", "متوسط", "زیاد")
  val sleepOptions = listOf("5 ساعت", "6 ساعت", "7 ساعت", "8 ساعت", "9+ ساعت")
  val energyOptions = listOf("عالی", "متوسط", "کم")

  AlertDialog(
    onDismissRequest = onDismiss,
    shape = RoundedCornerShape(28.dp),
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text("🌱", fontSize = 22.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "ثبت وضعیت امروز",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Mood Section
        Column {
          Text("😊 حال شما چطوره؟", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            moodOptions.forEach { option ->
              FilterChip(
                selected = (mood == option),
                onClick = { mood = option },
                label = { Text(option, fontSize = 11.sp) },
                modifier = Modifier.weight(1f)
              )
            }
          }
        }

        // Craving Section (Smart Color System)
        Column {
          Text("🔥 میزان وسوسه امروز:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            cravingOptions.forEach { option ->
              val isSelected = (craving == option)
              val color = when(option) {
                "کم" -> Color(0xFF2E7D32)
                "متوسط" -> Color(0xFFF57C00)
                else -> Color(0xFFD32F2F)
              }
              val icon = when(option) {
                "کم" -> "🟢"
                "متوسط" -> "🟡"
                else -> "🔴"
              }
              FilterChip(
                selected = isSelected,
                onClick = { craving = option },
                label = { Text("$icon $option", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = color.copy(alpha = 0.2f),
                  selectedLabelColor = color
                ),
                modifier = Modifier.weight(1f)
              )
            }
          }
        }

        // Sleep Section
        Column {
          Text("😴 خواب دیشب:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            sleepOptions.take(4).forEach { option ->
              FilterChip(
                selected = (sleep == option),
                onClick = { sleep = option },
                label = { Text(option, fontSize = 11.sp) },
                modifier = Modifier.weight(1f)
              )
            }
          }
        }

        // Energy Section
        Column {
          Text("⚡ سطح انرژی:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            energyOptions.forEach { option ->
              FilterChip(
                selected = (energy == option),
                onClick = { energy = option },
                label = { Text(option, fontSize = 11.sp) },
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          onSaveStatus(mood, craving, sleep, energy)
          onDismiss()
        },
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text("ثبت وضعیت", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("انصراف")
      }
    }
  )
}

@Composable
private fun StatusItemBox(
  modifier: Modifier = Modifier,
  icon: ImageVector,
  iconTint: Color,
  title: String,
  value: String
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 11.sp
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 12.sp,
        maxLines = 1
      )
    }
  }
}

// 11. LightExerciseCard (🏃‍♂️ حرکت امروز)
@Composable
fun LightExerciseCard(
  exerciseInfo: DailyExerciseInfo,
  isCompleted: Boolean,
  selectedFeeling: String?,
  showImages: Boolean = true,
  showSteps: Boolean = true,
  onComplete: () -> Unit,
  onFeelingSelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  PersianCard(
    modifier = modifier,
    borderColor = EmeraldPrimary.copy(alpha = 0.4f),
    backgroundColor = MaterialTheme.colorScheme.surface
  ) {
    // Card Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
          shape = CircleShape,
          color = EmeraldPrimary.copy(alpha = 0.15f)
        ) {
          Icon(
            imageVector = Icons.Default.DirectionsRun,
            contentDescription = null,
            tint = EmeraldPrimary,
            modifier = Modifier.padding(8.dp).size(22.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "🏃‍♂️ حرکت کوچک امروز",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "لازم نیست سخت بگیری. فقط ۵ دقیقه برای خودت وقت بگذار.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      if (isCompleted) {
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = EmeraldPrimary
        ) {
          Text(
            text = "✓ انجام شد (+5 XP)",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Title & Tags Row
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Text(
          text = exerciseInfo.title,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = EmeraldPrimary.copy(alpha = 0.2f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.Speed, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "سطح: ${exerciseInfo.level}",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary
              )
            }
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = TurquoiseSecondary.copy(alpha = 0.2f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.Timer, contentDescription = null, tint = TurquoiseSecondary, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = exerciseInfo.duration,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = TurquoiseSecondary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "🎯 هدف: ${exerciseInfo.goal}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Step-by-Step Instructions
    if (showSteps) {
      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "📋 مراحل انجام حرکت (گام به گام):",
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(8.dp))

      exerciseInfo.steps.forEach { step ->
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(EmeraldPrimary),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "${step.stepNumber}",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
              Text(
                text = step.instructionText,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
              )
              if (step.detailHint.isNotBlank()) {
                Text(
                  text = step.detailHint,
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }

    // Safety Tips Warning Box
    if (exerciseInfo.safetyTips.isNotEmpty()) {
      Spacer(modifier = Modifier.height(12.dp))

      Surface(
        shape = RoundedCornerShape(12.dp),
        color = GoldAmber.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, GoldAmber.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = GoldAmber, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "⚠️ نکته ایمنی و مراقبت:",
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Spacer(modifier = Modifier.height(4.dp))

          exerciseInfo.safetyTips.forEach { tip ->
            Text(
              text = "• $tip",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurface,
              lineHeight = 18.sp
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Completion Button
    CustomButton(
      text = if (isCompleted) "✓ حرکت انجام شد (+5 XP)" else "☑ انجام دادم (+5 XP)",
      onClick = onComplete,
      containerColor = if (isCompleted) EmeraldPrimary.copy(alpha = 0.8f) else EmeraldPrimary,
      icon = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Check
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Post Feeling Feedback Section
    Text(
      text = "😊 حال بعد از ورزش:",
      style = MaterialTheme.typography.labelLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(6.dp))

    val feelings = listOf("بهتر شد", "فرقی نکرد", "سخت بود")
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      feelings.forEach { feeling ->
        val isSelected = selectedFeeling == feeling
        val chipColor = when (feeling) {
          "بهتر شد" -> EmeraldPrimary
          "فرقی نکرد" -> MaterialTheme.colorScheme.primary
          else -> UrgeAlertRed
        }

        Surface(
          onClick = { onFeelingSelected(feeling) },
          shape = RoundedCornerShape(12.dp),
          color = if (isSelected) chipColor else MaterialTheme.colorScheme.surfaceVariant,
          border = BorderStroke(1.dp, if (isSelected) chipColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
          modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp)
        ) {
          Box(
            modifier = Modifier.padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = feeling,
              style = MaterialTheme.typography.bodySmall,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }
  }
}

// 12. MeditationCard (🧘 آرامش و مدیتیشن امروز)
@Composable
fun MeditationCard(
  meditationInfo: DailyMeditationInfo,
  isCompleted: Boolean,
  onComplete: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isAudioPlaying by remember { mutableStateOf(false) }
  var secondsLeft by remember { mutableIntStateOf(180) } // 3 mins = 180s

  LaunchedEffect(isAudioPlaying) {
    while (isAudioPlaying && secondsLeft > 0) {
      delay(1000)
      secondsLeft--
    }
    if (secondsLeft == 0) {
      isAudioPlaying = false
    }
  }

  val minutesStr = String.format("%02d", secondsLeft / 60)
  val secondsStr = String.format("%02d", secondsLeft % 60)

  PersianCard(
    modifier = modifier,
    borderColor = TurquoiseSecondary.copy(alpha = 0.4f),
    backgroundColor = MaterialTheme.colorScheme.surface
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
          shape = CircleShape,
          color = TurquoiseSecondary.copy(alpha = 0.15f)
        ) {
          Icon(
            imageVector = Icons.Default.SelfImprovement,
            contentDescription = null,
            tint = TurquoiseSecondary,
            modifier = Modifier.padding(8.dp).size(22.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "🧘 آرامش امروز",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "مدت: ${meditationInfo.duration} — تمرکز بر تنفس و آگاهی",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Surface(
        shape = RoundedCornerShape(10.dp),
        color = TurquoiseSecondary.copy(alpha = 0.2f)
      ) {
        Text(
          text = meditationInfo.phaseCategory,
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = TurquoiseSecondary,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Interactive Player Box
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = TurquoiseSecondary.copy(alpha = 0.12f),
      border = BorderStroke(1.dp, TurquoiseSecondary.copy(alpha = 0.3f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Headphones, contentDescription = null, tint = TurquoiseSecondary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "🎧 صدای راهنما و اصوات آرامش",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = TurquoiseSecondary
            )
          }

          Text(
            text = "$minutesStr:$secondsStr",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TurquoiseSecondary
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          IconButton(
            onClick = { isAudioPlaying = !isAudioPlaying },
            modifier = Modifier
              .size(52.dp)
              .clip(CircleShape)
              .background(TurquoiseSecondary)
          ) {
            Icon(
              imageVector = if (isAudioPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = "پخش صدای راهنما",
              tint = Color.White,
              modifier = Modifier.size(32.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          TextButton(
            onClick = {
              secondsLeft = 180
              isAudioPlaying = false
            }
          ) {
            Text("بازنشانی زمان", color = TurquoiseSecondary)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Meditation Steps
    Text(
      text = "🌱 راهنمای تمرین امروز:",
      style = MaterialTheme.typography.labelLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(6.dp))

    meditationInfo.steps.forEach { stepText ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 3.dp),
        verticalAlignment = Alignment.Top
      ) {
        Icon(Icons.Default.Spa, contentDescription = null, tint = TurquoiseSecondary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = stepText,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurface,
          lineHeight = 20.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    CustomButton(
      text = if (isCompleted) "✓ مدیتیشن ثبت شد (+5 XP)" else "▶ ثبت انجام مدیتیشن (+5 XP)",
      onClick = onComplete,
      containerColor = TurquoiseSecondary,
      icon = Icons.Default.CheckCircle
    )
  }
}

