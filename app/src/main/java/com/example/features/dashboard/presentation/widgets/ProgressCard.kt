package com.example.features.dashboard.presentation.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Progress Card displaying 30-day recovery pathway linear progress indicator.
 */
@Composable
fun ProgressCard(
  day: Int,
  totalDays: Int = 30,
  modifier: Modifier = Modifier
) {
  val progress = (day.toFloat() / totalDays.toFloat()).coerceIn(0f, 1f)

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(24.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
      Text(
        text = "🌿 مسیر ۳۰ روزه رهایی",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "روز $day از $totalDays",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary
      )

      Spacer(modifier = Modifier.height(12.dp))

      ClipRRectProgress(progress = progress)
    }
  }
}

@Composable
private fun ClipRRectProgress(progress: Float) {
  Box(modifier = Modifier.fillMaxWidth()) {
    LinearProgressIndicator(
      progress = { progress },
      modifier = Modifier
        .fillMaxWidth()
        .height(12.dp)
        .clip(RoundedCornerShape(20.dp)),
      color = MaterialTheme.colorScheme.primary,
      trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    )
  }
}
