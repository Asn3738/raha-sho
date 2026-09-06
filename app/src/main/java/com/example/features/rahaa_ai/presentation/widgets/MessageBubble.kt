package com.example.features.rahaa_ai.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.features.rahaa_ai.domain.entities.ChatMessage
import com.example.ui.theme.EmeraldPrimary

/**
 * Message Bubble Widget displaying individual chat bubbles.
 */
@Composable
fun MessageBubble(
  message: ChatMessage,
  modifier: Modifier = Modifier
) {
  val clipboardManager = LocalClipboardManager.current
  val user = message.isUser

  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    horizontalArrangement = if (user) Arrangement.Start else Arrangement.End
  ) {
    if (user) {
      Box(
        modifier = Modifier
          .size(34.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onPrimaryContainer,
          modifier = Modifier.size(18.dp)
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
    }

    Surface(
      modifier = Modifier.widthIn(max = 280.dp),
      shape = RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 20.dp,
        bottomStart = if (user) 4.dp else 20.dp,
        bottomEnd = if (user) 20.dp else 4.dp
      ),
      color = if (user) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
      contentColor = if (user) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
      shadowElevation = 1.dp
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = message.text,
          style = MaterialTheme.typography.bodyMedium,
          lineHeight = 23.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = message.timeFormatted,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
          )

          if (!user) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = "کپی",
              tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
              modifier = Modifier
                .size(14.dp)
                .clickable {
                  clipboardManager.setText(AnnotatedString(message.text))
                }
            )
          }
        }
      }
    }

    if (!user) {
      Spacer(modifier = Modifier.width(8.dp))
      Box(
        modifier = Modifier
          .size(34.dp)
          .clip(CircleShape)
          .background(EmeraldPrimary),
        contentAlignment = Alignment.Center
      ) {
        Text(text = "🌱", fontSize = 16.sp)
      }
    }
  }
}
