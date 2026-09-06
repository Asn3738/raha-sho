package com.example.features.dashboard.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.features.dashboard.presentation.widgets.CleanDaysCard
import com.example.features.dashboard.presentation.widgets.DailyQuoteCard
import com.example.features.dashboard.presentation.widgets.ProgressCard
import com.example.features.dashboard.presentation.widgets.StatCard
import com.example.ui.RahaaViewModel

/**
 * Professional "Rahaa" Dashboard Screen (رها شو 🌿)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
  viewModel: RahaaViewModel? = null,
  day: Int = 8,
  totalDays: Int = 30,
  cleanDays: Int = 45,
  xpPoints: String = "1250 XP",
  exercisesCount: String = "24",
  vowsCount: String = "12",
  notesCount: String = "18"
) {
  val userProfileState = viewModel?.userProfile?.collectAsState()
  val userDay = userProfileState?.value?.currentDay ?: day
  val computedCleanDays = remember(userProfileState?.value?.startDateTimestamp) {
    val start = userProfileState?.value?.startDateTimestamp ?: System.currentTimeMillis()
    java.util.concurrent.TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - start).toInt().coerceAtLeast(0)
  }

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Text(
            text = "رها شو 🌿",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
        }
      )
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ) {
      ProgressCard(
        day = userDay,
        totalDays = totalDays
      )

      Spacer(modifier = Modifier.height(16.dp))

      CleanDaysCard(
        days = if (userProfileState?.value != null) computedCleanDays else cleanDays
      )

      Spacer(modifier = Modifier.height(16.dp))

      // 2x2 Grid for Stats
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        StatCard(
          title = "امتیاز",
          value = xpPoints,
          icon = Icons.Default.Stars,
          modifier = Modifier.weight(1f)
        )
        StatCard(
          title = "تمرین‌ها",
          value = exercisesCount,
          icon = Icons.Default.CheckCircle,
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        StatCard(
          title = "عهدها",
          value = vowsCount,
          icon = Icons.Default.Favorite,
          modifier = Modifier.weight(1f)
        )
        StatCard(
          title = "یادداشت‌ها",
          value = notesCount,
          icon = Icons.Default.Book,
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      DailyQuoteCard()

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
