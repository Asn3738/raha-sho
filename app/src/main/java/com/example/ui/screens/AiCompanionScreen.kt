package com.example.ui.screens

import com.example.data.local.isAdmin
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AiChatEntity
import com.example.ui.RahaaViewModel
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TurquoiseSecondary
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

import com.example.features.rahaa_ai.presentation.widgets.EmergencyButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiCompanionScreen(
  viewModel: RahaaViewModel
) {
  val chatList by viewModel.allAiChats.collectAsState()
  val isAiThinking by viewModel.isAiThinking.collectAsState()
  val userProfile by viewModel.userProfile.collectAsState()

  var inputText by remember { mutableStateOf("") }
  var showClearDialog by remember { mutableStateOf(false) }

  val listState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()
  val clipboardManager = LocalClipboardManager.current

  val quickPrompts = listOf(
    "🔥 امروز شدیداً وسوسه شدم، چکار کنم؟",
    "🌱 چند کلمه انگیزه و امید می‌خوام",
    "🌬️ یک تمرین تنفس و آرامش بهم بگو",
    "🧘 چطور اضطرابم رو آروم کنم؟",
    "🧠 تمرین CBT برام بگذار",
    "💔 احساس تنهایی می‌کنم"
  )

  // Auto scroll to bottom when new messages arrive
  LaunchedEffect(chatList.size, isAiThinking) {
    if (chatList.isNotEmpty()) {
      listState.animateScrollToItem(chatList.size - 1)
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      
      // Top App Bar
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 3.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(
                  Brush.linearGradient(
                    colors = listOf(EmeraldPrimary, TurquoiseSecondary)
                  )
                ),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "🌱",
                fontSize = 22.sp
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
              Text(
                text = "یار رها 🤖",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "همراه هوشمند آنلاین",
                  style = MaterialTheme.typography.labelMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }

          IconButton(
            onClick = { showClearDialog = true },
            modifier = Modifier.testTag("clear_chat_button")
          ) {
            Icon(
              imageVector = Icons.Default.DeleteSweep,
              contentDescription = "پاکسازی گفتگو",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      // Emergency Button
      Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        EmergencyButton(
          onEmergencyTriggered = {
            viewModel.sendAiChatMessage("🆘 الان وسوسه دارم")
          }
        )
      }

      // Chat Messages List
      LazyColumn(
        state = listState,
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // AI Profile Memory Context Banner
        item {
          Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f))
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("🧠", fontSize = 20.sp)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "حافظه هوشمند یار رها فعال است" + if (userProfile?.isPremium == true || userProfile.isAdmin) " (پلاس 🌟)" else "",
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                val habit = userProfile?.habitType?.ifBlank { "عادت گذشته" } ?: "عادت گذشته"
                val triggers = userProfile?.triggersText?.ifBlank { "استرس و تنهایی" } ?: "استرس و تنهایی"
                Text(
                  text = "شناخت مسیر شما: $habit | محرک‌ها: $triggers",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  fontSize = 11.sp
                )
              }
            }
          }
        }

        items(chatList) { chat ->
          ChatBubbleItem(
            chat = chat,
            onCopy = { clipboardManager.setText(AnnotatedString(chat.message)) }
          )
        }

        if (isAiThinking) {
          item {
            AiTypingIndicator()
          }
        }
      }

      // Quick Suggestion Chips
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(quickPrompts) { prompt ->
          SuggestionChip(
            onClick = {
              viewModel.sendAiChatMessage(prompt)
            },
            label = {
              Text(
                text = prompt,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
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
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = inputText,
              onValueChange = { inputText = it },
              modifier = Modifier
                .weight(1f)
                .testTag("ai_chat_input"),
              placeholder = {
                Text(
                  text = "احساساتت رو بنویس یا سوالت رو بپرس...",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.outline
                )
              },
              shape = RoundedCornerShape(24.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
              ),
              maxLines = 4
            )

            Spacer(modifier = Modifier.width(8.dp))

            FloatingActionButton(
              onClick = {
                if (inputText.isNotBlank() && !isAiThinking) {
                  val prompt = inputText
                  inputText = ""
                  viewModel.sendAiChatMessage(prompt)
                }
              },
              modifier = Modifier
                .size(48.dp)
                .testTag("ai_send_button"),
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = Color.White,
              shape = CircleShape
            ) {
              Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "ارسال",
                modifier = Modifier.size(20.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "🔒 گفتگوهای شما با یار رها کاملاً شخصی و امن باقی می‌ماند.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.align(Alignment.CenterHorizontally)
          )
        }
      }
    }
  }

  // Clear History Dialog
  if (showClearDialog) {
    AlertDialog(
      onDismissRequest = { showClearDialog = false },
      title = {
        Text(
          text = "پاکسازی تاریخچه گفتگو",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )
      },
      text = {
        Text(
          text = "آیا اطمینان دارید که می‌خواهید تمام پیام‌های قبلی پاک شوند؟",
          style = MaterialTheme.typography.bodyMedium
        )
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.clearAiChatHistory()
            showClearDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("حذف شود", color = Color.White)
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

@Composable
fun ChatBubbleItem(
  chat: AiChatEntity,
  onCopy: () -> Unit
) {
  val isUser = chat.sender == "USER"
  val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(chat.timestamp))

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (isUser) Arrangement.Start else Arrangement.End
  ) {
    if (isUser) {
      Box(
        modifier = Modifier
          .size(32.dp)
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
        bottomStart = if (isUser) 4.dp else 20.dp,
        bottomEnd = if (isUser) 20.dp else 4.dp
      ),
      color = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
      contentColor = if (isUser) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
      shadowElevation = 1.dp
    ) {
      Column(
        modifier = Modifier.padding(12.dp)
      ) {
        Text(
          text = chat.message,
          style = MaterialTheme.typography.bodyMedium,
          lineHeight = 22.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = timeStr,
            style = MaterialTheme.typography.labelSmall,
            color = (if (isUser) Color.White else MaterialTheme.colorScheme.onSurfaceVariant).copy(alpha = 0.6f)
          )

          if (!isUser) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = "کپی",
              tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
              modifier = Modifier
                .size(14.dp)
                .clickable { onCopy() }
            )
          }
        }
      }
    }

    if (!isUser) {
      Spacer(modifier = Modifier.width(8.dp))
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(EmeraldPrimary),
        contentAlignment = Alignment.Center
      ) {
        Text(text = "🌱", fontSize = 16.sp)
      }
    }
  }
}

@Composable
fun AiTypingIndicator() {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.End
  ) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.surfaceVariant,
      shadowElevation = 1.dp
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "یار رها در حال نوشتن است...",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(8.dp))
        CircularProgressIndicator(
          modifier = Modifier.size(16.dp),
          strokeWidth = 2.dp,
          color = MaterialTheme.colorScheme.primary
        )
      }
    }
    Spacer(modifier = Modifier.width(8.dp))
    Box(
      modifier = Modifier
        .size(32.dp)
        .clip(CircleShape)
        .background(EmeraldPrimary),
      contentAlignment = Alignment.Center
    ) {
      Text(text = "🌱", fontSize = 16.sp)
    }
  }
}
