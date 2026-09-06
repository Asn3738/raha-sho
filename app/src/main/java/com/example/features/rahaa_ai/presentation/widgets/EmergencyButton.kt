package com.example.features.rahaa_ai.presentation.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Emergency Button Component for instant urge assistance ("🆘 کمک فوری - الان وسوسه دارم").
 */
@Composable
fun EmergencyButton(
  onEmergencyTriggered: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showDialog by remember { mutableStateOf(false) }

  Surface(
    onClick = {
      showDialog = true
      onEmergencyTriggered()
    },
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(18.dp),
    color = MaterialTheme.colorScheme.errorContainer,
    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.error)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = Icons.Default.Warning,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onErrorContainer,
        modifier = Modifier.size(22.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "🆘 کمک فوری (الان وسوسه دارم)",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onErrorContainer
      )
    }
  }

  if (showDialog) {
    AlertDialog(
      onDismissRequest = { showDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = "🆘", fontSize = 24.sp)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "شروع تمرین ۵ دقیقه‌ای رهایی",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = "وسوسه مثل یک موج ۵ دقیقه‌ای است. گام‌های زیر را انجام بده:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
          )

          val steps = listOf(
            "۱. توقف کامل فعالیت فعلی 🛑",
            "۲. ۳ بار نفس بسیار عمیق کشیدن 🌬️",
            "۳. نوشیدن یک لیوان آب خنک 💧",
            "۴. گفتگو با یار رها 🤖",
            "۵. تغییر محیط و حرکت 🚶‍♂️"
          )

          steps.forEach { step ->
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = step,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(10.dp)
              )
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { showDialog = false },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Text("متوجه شدم، گفتگو را ادامه بده")
        }
      }
    )
  }
}
