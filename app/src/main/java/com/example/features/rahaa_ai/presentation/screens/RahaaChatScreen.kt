package com.example.features.rahaa_ai.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.features.rahaa_ai.presentation.providers.ChatProvider
import com.example.features.rahaa_ai.presentation.widgets.EmergencyButton
import com.example.features.rahaa_ai.presentation.widgets.MessageBubble
import com.example.ui.theme.EmeraldPrimary

/**
 * Screen implementation for Yar-e Rahaa AI Companion ("🤖 یار رها").
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RahaaChatScreen(
  chatProvider: ChatProvider = remember { ChatProvider() }
) {
  val messages by chatProvider.messages.collectAsState()
  val isThinking by chatProvider.isThinking.collectAsState()

  var inputText by remember { mutableStateOf("") }
  var showClearDialog by remember { mutableStateOf(false) }

  val listState = rememberLazyListState()

  val quickPrompts = listOf(
    "🔥 الان شدیداً وسوسه شدم، چکار کنم؟",
    "🌱 چند کلمه انگیزه و امید می‌خوام",
    "🧘 چطور اضطرابم رو آروم کنم؟",
    "🧠 تمرین CBT برام بگذار",
    "💔 احساس تنهایی می‌کنم"
  )

  LaunchedEffect(messages.size, isThinking) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(EmeraldPrimary),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "🤖", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "یار رها",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "همراه آنلاین و دلسوز",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        },
        actions = {
          IconButton(onClick = { showClearDialog = true }) {
            Icon(
              imageVector = Icons.Default.DeleteSweep,
              contentDescription = "پاکسازی گفتگو",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      )
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(MaterialTheme.colorScheme.background)
    ) {
      // Emergency Button Container
      Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        EmergencyButton(
          onEmergencyTriggered = {
            chatProvider.triggerEmergencyUrge()
          }
        )
      }

      // Messages List
      LazyColumn(
        state = listState,
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(messages) { message ->
          MessageBubble(message = message)
        }

        if (isThinking) {
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "یار رها در حال نوشتن است...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                  )
                }
              }
            }
          }
        }
      }

      // Quick Suggestion Chips
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(quickPrompts) { prompt ->
          SuggestionChip(
            onClick = {
              chatProvider.sendMessage(prompt)
            },
            label = {
              Text(
                text = prompt,
                style = MaterialTheme.typography.labelMedium
              )
            },
            colors = SuggestionChipDefaults.suggestionChipColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(16.dp)
          )
        }
      }

      // Bottom Input Bar
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("پیام خود را بنویسید...") },
            shape = RoundedCornerShape(24.dp),
            maxLines = 4
          )

          Spacer(modifier = Modifier.width(8.dp))

          IconButton(
            onClick = {
              if (inputText.isNotBlank()) {
                val text = inputText
                inputText = ""
                chatProvider.sendMessage(text)
              }
            },
            modifier = Modifier
              .size(48.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primary)
          ) {
            Icon(
              imageVector = Icons.Default.Send,
              contentDescription = "ارسال",
              tint = Color.White
            )
          }
        }
      }
    }
  }

  if (showClearDialog) {
    AlertDialog(
      onDismissRequest = { showClearDialog = false },
      title = { Text("پاکسازی گفتگو") },
      text = { Text("آیا می‌خواهید تاریخچه پیام‌های این گفتگو پاک شوند؟") },
      confirmButton = {
        Button(
          onClick = {
            chatProvider.clearHistory()
            showClearDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("حذف شود")
        }
      },
      dismissButton = {
        TextButton(onClick = { showClearDialog = false }) {
          Text("انصراف")
        }
      }
    )
  }
}
