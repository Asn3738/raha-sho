package com.example.ui.components

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAmber
import com.example.ui.theme.TurquoiseSecondary
import kotlinx.coroutines.*
import java.util.Locale
import kotlin.math.sin

/**
 * Frequency Audio Player Helper class utilizing Android AudioTrack
 * to synthesize PCM Solfeggio frequencies (528Hz, 852Hz, 432Hz, etc.)
 */
class SolfeggioFrequencyPlayer(private val context: Context) {
  private var audioTrack: AudioTrack? = null
  private var playJob: Job? = null
  private var tts: TextToSpeech? = null
  private var isTtsReady = false

  init {
    try {
      tts = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
          val localeResult = tts?.setLanguage(Locale("fa"))
          if (localeResult == TextToSpeech.LANG_MISSING_DATA || localeResult == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts?.setLanguage(Locale.getDefault())
          }
          isTtsReady = true
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun playFrequency(frequencyHz: Float, durationMinutes: Int = 10, onComplete: () -> Unit = {}) {
    stop()
    val sampleRate = 44100
    val minBufferSize = AudioTrack.getMinBufferSize(
      sampleRate,
      AudioFormat.CHANNEL_OUT_MONO,
      AudioFormat.ENCODING_PCM_16BIT
    )

    try {
      audioTrack = AudioTrack.Builder()
        .setAudioAttributes(
          AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        )
        .setAudioFormat(
          AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(sampleRate)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()
        )
        .setBufferSizeInBytes(minBufferSize * 2)
        .setTransferMode(AudioTrack.MODE_STREAM)
        .build()

      audioTrack?.play()

      playJob = CoroutineScope(Dispatchers.Default).launch {
        val buffer = ShortArray(1024)
        var sampleIndex = 0L
        val twoPi = 2.0 * Math.PI
        val baseFreq = frequencyHz.toDouble()

        val startTime = System.currentTimeMillis()
        val endTime = startTime + durationMinutes * 60 * 1000

        while (isActive && System.currentTimeMillis() < endTime) {
          for (i in buffer.indices) {
            val t = sampleIndex / sampleRate.toDouble()
            val fundamental = sin(twoPi * baseFreq * t)
            val harmonic = sin(twoPi * (baseFreq * 1.5) * t) * 0.15
            val subHarmonic = sin(twoPi * (baseFreq * 0.5) * t) * 0.2
            val modulation = 0.85 + 0.15 * sin(twoPi * 0.25 * t)

            val sampleValue = ((fundamental + harmonic + subHarmonic) * 0.35 * modulation * Short.MAX_VALUE)
            buffer[i] = sampleValue.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            sampleIndex++
          }
          audioTrack?.write(buffer, 0, buffer.size)
        }
        withContext(Dispatchers.Main) {
          stop()
          onComplete()
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun speakAffirmation(text: String) {
    if (isTtsReady && tts != null) {
      tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "affirmation_tts")
    }
  }

  fun stop() {
    playJob?.cancel()
    playJob = null
    try {
      if (audioTrack?.playState == AudioTrack.PLAYSTATE_PLAYING) {
        audioTrack?.stop()
      }
      audioTrack?.release()
      audioTrack = null
    } catch (e: Exception) {
      e.printStackTrace()
    }
    try {
      tts?.stop()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun release() {
    stop()
    try {
      tts?.shutdown()
      tts = null
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}

data class HealingFrequencyItem(
  val frequencyHz: Float,
  val name: String,
  val benefit: String,
  val icon: String,
  val color: Color
)

val healingFrequenciesList = listOf(
  HealingFrequencyItem(528f, "۵۲۸ هرتز", "ترمیم DNA و بازسازی سلولی بدن", "🧬", Color(0xFF2E7D32)),
  HealingFrequencyItem(852f, "۸۵۲ هرتز", "تقویت اراده و عزم فولادین", "⚡", Color(0xFFD84315)),
  HealingFrequencyItem(432f, "۴۳۲ هرتز", "بالابردن قدرت ذهن و آرامش عمیق", "🧠", Color(0xFF1565C0)),
  HealingFrequencyItem(741f, "۷۴۱ هرتز", "سم‌زدایی بدن و پاکسازی ذهنی", "🍃", Color(0xFF00838F)),
  HealingFrequencyItem(639f, "۶۳۹ هرتز", "تعادل احساسی و انرژی مثبت", "💛", Color(0xFFF57F17))
)

val dailyAffirmationsList = listOf(
  "امروز سلول‌های بدن من در حال ترمیم و نوآوری هستند. اراده من از هر وسوسه‌ای قوی‌تر است.",
  "من کنترل کامل بر ذهن و احساسات خود دارم. پاکی بزرگترین هدیه‌ای است که به خودم می‌دهم.",
  "با هر نفس عمیق، آرامش و هوشیاری وارد وجودم می‌شود و تمام سموم از بدنم خارج می‌گردد.",
  "قدرت ذهن من بی‌انتهاست. من هر روز استوارتر و بی‌نیازتر از گذشته گام برمی‌دارم.",
  "امروز روز پیروزی من بر عادت‌های گذشته است. آینده‌ای روشن و با عزت در انتظار من است."
)

@Composable
fun MorningAudioAffirmationCard(
  currentDay: Int,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val player = remember { SolfeggioFrequencyPlayer(context) }

  DisposableEffect(Unit) {
    onDispose {
      player.release()
    }
  }

  var isAffirmationPlaying by remember { mutableStateOf(false) }
  var activeFrequencyHz by remember { mutableStateOf<Float?>(null) }
  var isFrequencyPlaying by remember { mutableStateOf(false) }
  var selectedDurationMinutes by remember { mutableIntStateOf(10) }

  val todayAffirmation = remember(currentDay) {
    dailyAffirmationsList[(currentDay - 1) % dailyAffirmationsList.size]
  }

  // Waveform animation
  val infiniteTransition = rememberInfiniteTransition(label = "waveform")
  val wavePhase by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ), label = "wavePhase"
  )

  PersianCard(borderColor = EmeraldPrimary.copy(alpha = 0.5f)) {
    Column {
      // Header with Sunny Badge
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
              .background(GoldAmber.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.WbSunny,
              contentDescription = null,
              tint = GoldAmber,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "پادکست و عبارت تاکیدی صبحگاهی 🌅",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "فعال‌شده برای صبح روز $currentDay - تقویت اراده و ترمیم بدن",
              style = MaterialTheme.typography.labelSmall,
              color = EmeraldPrimary,
              fontWeight = FontWeight.SemiBold
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = EmeraldPrimary.copy(alpha = 0.15f),
          border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f))
        ) {
          Text(
            text = "روز $currentDay",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = EmeraldPrimary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Today's Morning Affirmation Box
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.FormatQuote,
              contentDescription = null,
              tint = EmeraldPrimary,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "جمله تاکیدی قدرتمند امروز:",
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold,
              color = EmeraldPrimary
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "« $todayAffirmation »",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 22.sp
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Audio Voice Play Button for Affirmation
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              onClick = {
                if (isAffirmationPlaying) {
                  player.stop()
                  isAffirmationPlaying = false
                } else {
                  if (isFrequencyPlaying) {
                    player.stop()
                    isFrequencyPlaying = false
                  }
                  isAffirmationPlaying = true
                  player.speakAffirmation(todayAffirmation)
                }
              },
              shape = RoundedCornerShape(12.dp),
              color = if (isAffirmationPlaying) GoldAmber else EmeraldPrimary,
              contentColor = Color.White
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = if (isAffirmationPlaying) Icons.Default.Pause else Icons.Default.VolumeUp,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = if (isAffirmationPlaying) "توقف خوانش صوتی" else "پخش صوتی عبارت تاکیدی",
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            // Waveform Visualizer
            if (isAffirmationPlaying || isFrequencyPlaying) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
              ) {
                for (i in 0..5) {
                  val heightFactor = sin((wavePhase * 2 * Math.PI) + (i * 0.8)).toFloat().coerceIn(0.2f, 1f)
                  Box(
                    modifier = Modifier
                      .width(4.dp)
                      .height((16 * heightFactor).dp)
                      .clip(RoundedCornerShape(2.dp))
                      .background(EmeraldPrimary)
                  )
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Healing Frequency Music Selector Header
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.GraphicEq,
          contentDescription = null,
          tint = EmeraldPrimary,
          modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "پخش اهنگ‌های فرکانس ترمیم و تقویت اراده 🎵",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "برای بازسازی سلولی، ارتقای قدرت ذهن و تقویت عزم فولادین کلیک کنید:",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Frequency Buttons Horizontal Scroll Row
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(healingFrequenciesList) { freqItem ->
          val isSelected = activeFrequencyHz == freqItem.frequencyHz && isFrequencyPlaying

          Surface(
            onClick = {
              if (isSelected) {
                player.stop()
                isFrequencyPlaying = false
                activeFrequencyHz = null
              } else {
                if (isAffirmationPlaying) {
                  player.stop()
                  isAffirmationPlaying = false
                }
                activeFrequencyHz = freqItem.frequencyHz
                isFrequencyPlaying = true
                player.playFrequency(
                  frequencyHz = freqItem.frequencyHz,
                  durationMinutes = selectedDurationMinutes,
                  onComplete = {
                    isFrequencyPlaying = false
                    activeFrequencyHz = null
                  }
                )
              }
            },
            shape = RoundedCornerShape(14.dp),
            color = if (isSelected) freqItem.color else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            border = BorderStroke(
              width = if (isSelected) 2.dp else 1.dp,
              color = if (isSelected) freqItem.color else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
            ),
            modifier = Modifier.width(160.dp)
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              horizontalAlignment = Alignment.Start
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "${freqItem.icon} ${freqItem.name}",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                )
                Icon(
                  imageVector = if (isSelected) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                  contentDescription = null,
                  tint = if (isSelected) Color.White else freqItem.color,
                  modifier = Modifier.size(22.dp)
                )
              }

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = freqItem.benefit,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
              )
            }
          }
        }
      }

      // Playing Status Banner & Controls
      if (isFrequencyPlaying && activeFrequencyHz != null) {
        val currentFreqItem = healingFrequenciesList.find { it.frequencyHz == activeFrequencyHz }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
          shape = RoundedCornerShape(14.dp),
          color = (currentFreqItem?.color ?: EmeraldPrimary).copy(alpha = 0.15f),
          border = BorderStroke(1.dp, currentFreqItem?.color ?: EmeraldPrimary)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = null,
                tint = currentFreqItem?.color ?: EmeraldPrimary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "در حال پخش فرکانس ${currentFreqItem?.name}",
                  style = MaterialTheme.typography.labelLarge,
                  fontWeight = FontWeight.Bold,
                  color = currentFreqItem?.color ?: EmeraldPrimary
                )
                Text(
                  text = currentFreqItem?.benefit ?: "",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            IconButton(
              onClick = {
                player.stop()
                isFrequencyPlaying = false
                activeFrequencyHz = null
              }
            ) {
              Icon(
                imageVector = Icons.Default.StopCircle,
                contentDescription = "توقف",
                tint = currentFreqItem?.color ?: EmeraldPrimary,
                modifier = Modifier.size(28.dp)
              )
            }
          }
        }
      }
    }
  }
}
