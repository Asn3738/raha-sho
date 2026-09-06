package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.local.DailyContentEntity
import com.example.data.local.isAdmin
import com.example.ui.RahaaViewModel
import com.example.ui.components.PersianCard
import com.example.ui.theme.EmeraldContainerLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAmber

@Composable
fun JourneyScreen(
  viewModel: RahaaViewModel
) {
  val userProfile by viewModel.userProfile.collectAsState()
  val allContents by viewModel.allDailyContents.collectAsState()

  val currentActiveDay = userProfile?.currentDay ?: 1
  val isAdmin = userProfile.isAdmin

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
        text = "مسیر ۳۰ روزه رهایی",
        style = MaterialTheme.typography.displayMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )

      Text(
        text = "سفر رشد و دگرگونی از تاریکی به سوی نور",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      if (isAdmin) {
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = GoldAmber.copy(alpha = 0.2f),
          border = BorderStroke(1.dp, GoldAmber)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = GoldAmber)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "👑 حالت دسترسی ادمین (الله اکبر) فعال است",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "تمامی قفل‌های روزانه و پرداخت‌های درون‌برنامه‌ای کاملاً باز گردیده است.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Group contents by phase
      val phase1 = allContents.filter { it.phaseNumber == 1 }
      val phase2 = allContents.filter { it.phaseNumber == 2 }
      val phase3 = allContents.filter { it.phaseNumber == 3 }
      val phase4 = allContents.filter { it.phaseNumber == 4 }

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
      ) {
        item {
          PhaseHeaderCard(
            phaseNumber = 1,
            phaseTitle = "مرحله اول: شروع دوباره (روز ۱ تا ۷)",
            description = "تمرکز بر پایداری اولیه، شکستن زنجیر‌های عادت و حس امید."
          )
        }
        items(phase1) { content ->
          DayPathItemCard(
            content = content,
            currentActiveDay = currentActiveDay,
            isAdmin = isAdmin,
            onDayClick = { viewModel.openDailyContent(content.dayNumber) }
          )
        }

        item {
          Spacer(modifier = Modifier.height(16.dp))
          PhaseHeaderCard(
            phaseNumber = 2,
            phaseTitle = "مرحله دوم: قدرت ذهن (روز ۸ تا ۱۴)",
            description = "شناخت محرک‌ها،کنترل گفتگوی درونی و مدیریت استرس."
          )
        }
        items(phase2) { content ->
          DayPathItemCard(
            content = content,
            currentActiveDay = currentActiveDay,
            isAdmin = isAdmin,
            onDayClick = { viewModel.openDailyContent(content.dayNumber) }
          )
        }

        item {
          Spacer(modifier = Modifier.height(16.dp))
          PhaseHeaderCard(
            phaseNumber = 3,
            phaseTitle = "مرحله سوم: ساخت شخصیت جدید (روز ۱۵ تا ۲۱)",
            description = "تثبیت انضباط شخصی، تثبیت عادت‌های سازنده و اعتماد به نفس."
          )
        }
        items(phase3) { content ->
          DayPathItemCard(
            content = content,
            currentActiveDay = currentActiveDay,
            isAdmin = isAdmin,
            onDayClick = { viewModel.openDailyContent(content.dayNumber) }
          )
        }

        item {
          Spacer(modifier = Modifier.height(16.dp))
          PhaseHeaderCard(
            phaseNumber = 4,
            phaseTitle = "مرحله چهارم: زندگی جدید (روز ۲۲ تا ۳۰)",
            description = "پرواز رهایی، تثبیت همیشگی پاکی و نگاه به آینده روشن."
          )
        }
        items(phase4) { content ->
          DayPathItemCard(
            content = content,
            currentActiveDay = currentActiveDay,
            isAdmin = isAdmin,
            onDayClick = { viewModel.openDailyContent(content.dayNumber) }
          )
        }
      }
    }
  }
}

@Composable
fun PhaseHeaderCard(
  phaseNumber: Int,
  phaseTitle: String,
  description: String
) {
  val headerColor = when (phaseNumber) {
    1 -> EmeraldPrimary
    2 -> MaterialTheme.colorScheme.secondary
    3 -> GoldAmber
    else -> MaterialTheme.colorScheme.primary
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = headerColor.copy(alpha = 0.12f)
    ),
    border = BorderStroke(1.dp, headerColor.copy(alpha = 0.4f))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Text(
        text = phaseTitle,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = headerColor
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun DayPathItemCard(
  content: DailyContentEntity,
  currentActiveDay: Int,
  isAdmin: Boolean = false,
  onDayClick: () -> Unit
) {
  val isCompleted = content.isCompleted
  val isActive = content.dayNumber == currentActiveDay
  val isLocked = if (isAdmin) false else (content.dayNumber > currentActiveDay && !isCompleted)

  val cardBg = when {
    isCompleted -> EmeraldContainerLight.copy(alpha = 0.6f)
    isActive -> MaterialTheme.colorScheme.primaryContainer
    isAdmin -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
    else -> MaterialTheme.colorScheme.surface
  }

  val statusText = when {
    isCompleted -> "تکمیل شده"
    isActive -> "امروز (فعال)"
    isAdmin -> "باز شده (ادمین)"
    else -> "قفل شده"
  }

  val statusColor = when {
    isCompleted -> EmeraldPrimary
    isActive -> GoldAmber
    else -> MaterialTheme.colorScheme.outline
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
      .clickable(enabled = !isLocked) { onDayClick() },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = cardBg),
    border = BorderStroke(
      1.dp,
      if (isActive) GoldAmber else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    )
  ) {
    Row(
      modifier = Modifier
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(
            when {
              isCompleted -> EmeraldPrimary
              isActive -> GoldAmber
              else -> MaterialTheme.colorScheme.surfaceVariant
            }
          ),
        contentAlignment = Alignment.Center
      ) {
        if (isCompleted) {
          Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
        } else if (isLocked) {
          Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
        } else {
          Text(
            text = "${content.dayNumber}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "روز ${content.dayNumber}: ${content.titleFa}",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = content.subtitleFa,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      Surface(
        shape = RoundedCornerShape(8.dp),
        color = statusColor.copy(alpha = 0.15f)
      ) {
        Text(
          text = statusText,
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = statusColor,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
      }
    }
  }
}
