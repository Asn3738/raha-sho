package com.example.features.daily_recommendation.presentation.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.features.daily_recommendation.domain.DailyRecommendation
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAmber

@Composable
fun DailyRecommendationCard(
  recommendation: DailyRecommendation,
  onStartExerciseClick: () -> Unit,
  onChatWithAiClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(24.dp),
    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
    border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.35f))
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(EmeraldPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Text("🤖", fontSize = 20.sp)
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "پیشنهاد هوشمند امروز یار رها",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "دلیل: ${recommendation.reason}",
              style = MaterialTheme.typography.bodySmall,
              color = EmeraldPrimary,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Motivation banner
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.FormatQuote, contentDescription = null, tint = GoldAmber)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = recommendation.motivation,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 18.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Items list
      RecommendationItemRow(
        icon = "🙏",
        title = "شروع و توکل:",
        detail = recommendation.spiritual
      )

      RecommendationItemRow(
        icon = "🧘",
        title = "مدیتیشن:",
        detail = recommendation.meditation
      )

      RecommendationItemRow(
        icon = "🏃",
        title = "ورزش و حرکت:",
        detail = recommendation.exercise
      )

      RecommendationItemRow(
        icon = "🎯",
        title = "اقدام کلیدی:",
        detail = recommendation.action
      )

      RecommendationItemRow(
        icon = "📖",
        title = "آموزش روز:",
        detail = recommendation.lesson
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Action buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = onStartExerciseClick,
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("شروع تمرین", fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
          onClick = onChatWithAiClick,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("گفتگو با یار رها")
        }
      }
    }
  }
}

@Composable
private fun RecommendationItemRow(
  icon: String,
  title: String,
  detail: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.Top
  ) {
    Text(icon, fontSize = 16.sp)
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = "$title ",
      style = MaterialTheme.typography.labelLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface
    )
    Text(
      text = detail,
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      lineHeight = 18.sp
    )
  }
}
