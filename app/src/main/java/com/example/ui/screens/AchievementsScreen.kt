package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.RahaaViewModel
import com.example.ui.components.AchievementBadge
import com.example.ui.components.PersianCard
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAmber
import java.util.Locale

@Composable
fun AchievementsScreen(
  viewModel: RahaaViewModel
) {
  val userProfile by viewModel.userProfile.collectAsState()
  val achievements by viewModel.allAchievements.collectAsState()
  val journalEntries by viewModel.allJournalEntries.collectAsState()

  val totalPoints = userProfile?.totalPoints ?: 0
  val cleanDays = userProfile?.currentDay ?: 1
  val unlockedCount = achievements.count { it.isUnlocked }

  // Level & Tree Growth determination
  val (treeStageTitle, treeStageEmoji, treeDescription, levelTitle) = when {
    cleanDays >= 90 -> Quadruple("بوستان رهایی 🌳✨", "🌳", "ریشه‌های تو عمیق شده و سایه‌سار امید شده‌ای.", "قهرمان تغییر 🌟")
    cleanDays >= 30 -> Quadruple("درخت استوار 🌴", "🌴", "درخت پاکی تو در برابر طوفان‌ها مقاوم گشته است.", "سازنده زندگی جدید 🌿")
    cleanDays >= 7 -> Quadruple("نهال صبور 🪴", "🪴", "جوانه‌ات رشد کرده و تبدیل به نهالی قوی شده است.", "شروع قدرتمند 💪")
    else -> Quadruple("جوانه امید 🌱", "🌱", "اولین ریشه‌های رهایی در خاک امید قرار گرفته‌اند.", "قدم اول رهایی 🌱")
  }

  // Monthly Analytics calculation
  val avgUrge = if (journalEntries.isNotEmpty()) journalEntries.map { it.urgeIntensity }.average() else 2.5
  val avgSleep = if (journalEntries.isNotEmpty()) journalEntries.map { it.sleepHours }.average() else 7.0
  val cravingDecreasePercent = ((10.0 - avgUrge) * 10).coerceIn(0.0, 100.0).toInt()

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
        text = "تحلیل پیشرفت و درخت رشد 🌱",
        style = MaterialTheme.typography.displayMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )

      Text(
        text = "شناسنامه افتخار، گزارش رشد ماهانه و امتیازات رهایی تو",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(12.dp))

      var selectedTab by remember { mutableIntStateOf(0) }
      val tabs = listOf("📊 پیشرفت من", "👥 جامعه حمایتی", "🏆 نشان‌ها")

      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = EmeraldPrimary,
        divider = {}
      ) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = {
              Text(
                text = title,
                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                style = MaterialTheme.typography.bodyMedium
              )
            }
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        if (selectedTab == 0 || selectedTab == 2) {
          // 1. Personal Growth Tree Card (درخت رشد شخصی)
          item {
            Surface(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(24.dp),
              color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
              border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f))
            ) {
              Column(modifier = Modifier.padding(18.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(treeStageEmoji, fontSize = 36.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                      Text(
                        text = "مرحله: $treeStageTitle",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                      )
                      Text(
                        text = "سطح: $levelTitle",
                        style = MaterialTheme.typography.bodySmall,
                        color = EmeraldPrimary,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                  text = treeDescription,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Growth Progress Indicator
                val progressFraction = (cleanDays % 30) / 30f
                LinearProgressIndicator(
                  progress = { progressFraction.coerceIn(0.05f, 1f) },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                  color = EmeraldPrimary,
                  trackColor = EmeraldPrimary.copy(alpha = 0.2f)
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = "استمرار پاکی: $cleanDays روز متوالی 🌿",
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
              }
            }
          }

          // 2. Summary Stats Row
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Card(
                modifier = Modifier.weight(1f).padding(end = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
              ) {
                Column(
                  modifier = Modifier.padding(14.dp),
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = GoldAmber)
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = "$unlockedCount از ${achievements.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "نشان‌های دریافت شده",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }

              Card(
                modifier = Modifier.weight(1f).padding(start = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
              ) {
                Column(
                  modifier = Modifier.padding(14.dp),
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  Icon(Icons.Default.Stars, contentDescription = null, tint = EmeraldPrimary)
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = "$totalPoints XP",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "امتیاز رهایی (Recovery XP)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }

          // 3. Monthly Progress Analytics Card
          item {
            PersianCard {
              Text(
                text = "📊 گزارش تحلیلی ماهانه روند بهبود",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )

              Spacer(modifier = Modifier.height(12.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = "📉 $cravingDecreasePercent٪",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = EmeraldPrimary
                  )
                  Text(
                    text = "کاهش شدت وسوسه",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = "😴 ${String.format(Locale.ENGLISH, "%.1f", avgSleep)}h",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldAmber
                  )
                  Text(
                    text = "میانگین خواب شبانه",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = "😊 عالی",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                  )
                  Text(
                    text = "روند حال روحی",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }
        }

        // Community Tab (👥 جامعه حمایتی)
        if (selectedTab == 1) {
          // AI Moderation & Privacy Guard Banner
          item {
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = EmeraldPrimary.copy(alpha = 0.12f),
              border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f))
            ) {
              Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(
                    text = "🛡 جامعه حمایتی امن و ناشناس",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                  )
                  Text(
                    text = "تمامی پیام‌ها با پایش هوشمند AI ناظر بررسی شده و بدون افشای هیچگونه اطلاعات هویت شخصی منتشر می‌شوند.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                  )
                }
              }
            }
          }

          // Support Groups
          item {
            Text(
              text = "🌱 گروه‌های هم‌مسیر رهایی:",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          item {
            val groups = listOf(
              Triple("🌱 گروه شروع دوباره (روز‌های ۱ تا ۳۰)", "تثبیت تصمیم، کنترل روزهای اول و ساخت عادات خرد", "۱۲۴۰ عضو"),
              Triple("🌿 گروه ساختن عادت (روز‌های ۳۰ تا ۹۰)", "افزایش انضباط فردی، ورزش و مدیریت احساسات", "۸۵۰ عضو"),
              Triple("🌳 گروه رشد پایدار (۹۰ روز به بالا)", "بازسازی اهداف بزرگ زندگی و کمک به تازه واردان", "۴۲۰ عضو")
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              groups.forEach { (title, desc, members) ->
                PersianCard {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column(modifier = Modifier.weight(1f)) {
                      Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                      Spacer(modifier = Modifier.height(2.dp))
                      Text(text = desc, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                      shape = RoundedCornerShape(12.dp),
                      color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                      Text(
                        text = members,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                      )
                    }
                  }
                }
              }
            }
          }

          // Success Stories / Inspirational Feed
          item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "📖 داستان‌های الهام‌بخش اعضا:",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          item {
            PersianCard {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.FormatQuote, contentDescription = null, tint = GoldAmber)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "داستان همراه رهایی (روز ۶۰ پاکی)",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold
                )
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "«در روزهای اول فکر می‌کردم تنهایم، اما با تمرین‌های تنفس و پیگیری روزانه یار رها توانستم وسوسه‌ها را کنترل کنم. الان حس می‌کنم کنترل زندگی‌ام دست خودم است.»",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
              )
              Spacer(modifier = Modifier.height(8.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = "❤️ ۳۴۲ نفر الهام گرفتند", style = MaterialTheme.typography.labelSmall, color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = EmeraldPrimary.copy(alpha = 0.15f),
                  modifier = Modifier.clickable { /* Like story */ }
                ) {
                  Text(
                    text = "ارسال انرژی مثبت ✨",
                    style = MaterialTheme.typography.labelSmall,
                    color = EmeraldPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                  )
                }
              }
            }
          }
        }

        // Badges Section (🏆 نشان‌ها)
        if (selectedTab == 0 || selectedTab == 2) {
          item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "نشان‌های افتخار رهایی:",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          items(achievements) { badge ->
            AchievementBadge(
              title = badge.titleFa,
              description = badge.descriptionFa,
              iconType = badge.iconType,
              isUnlocked = badge.isUnlocked,
              unlockedDate = badge.unlockedDate
            )
          }
        }
      }
    }
  }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

