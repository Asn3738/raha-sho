package com.example.ui.screens

import com.example.data.local.isAdmin
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.RahaaViewModel
import com.example.ui.components.CustomButton
import com.example.ui.components.PersianCard
import com.example.ui.components.ThemeSelector
import com.example.ui.theme.ThemeMode

@Composable
fun SettingsScreen(
  viewModel: RahaaViewModel
) {
  val userProfile by viewModel.userProfile.collectAsState()
  val themeMode by viewModel.themeMode.collectAsState()

  var notificationsEnabled by remember { mutableStateOf(userProfile?.notificationsEnabled ?: true) }
  var textScale by remember { mutableFloatStateOf(1f) }
  var showDeleteDialog by remember { mutableStateOf(false) }
  var showPrivacyDialog by remember { mutableStateOf(false) }
  var showAboutDialog by remember { mutableStateOf(false) }
  var showCreatorDialog by remember { mutableStateOf(false) }
  var showCalmDialog by remember { mutableStateOf(false) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 18.dp)
        .verticalScroll(rememberScrollState())
    ) {
      Spacer(modifier = Modifier.height(16.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = { viewModel.navigateTo(AppScreen.USER_PROFILE) }) {
          Icon(Icons.Default.ArrowForward, contentDescription = "بازگشت")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "تنظیمات",
          style = MaterialTheme.typography.displayMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 0. Premium Subscription Card (⭐ اشتراک رها شو پلاس)
      SettingsCard(
        icon = Icons.Default.WorkspacePremium,
        title = "⭐ اشتراک من (رها شو پلاس)",
        subtitle = if (userProfile?.isPremium == true || userProfile.isAdmin) "اشتراک فعال است 🌟 (ارتقا / مدیریت)" else "ارتقا به نسخه پلاس و فعال‌سازی تمامی امکانات",
        onTap = { viewModel.navigateTo(AppScreen.PREMIUM) }
      )

      // 1. Account Settings Card
      SettingsCard(
        icon = Icons.Default.Person,
        title = "حساب کاربری",
        subtitle = "اطلاعات شخصی و امنیت",
        onTap = { viewModel.navigateTo(AppScreen.USER_PROFILE) }
      )

      // 2. Notifications & Smart Reminders Card (🔔 سیستم یادآوری و اعلان هوشمند)
      var showNotificationDetails by remember { mutableStateOf(false) }
      var morningReminder by remember { mutableStateOf(true) }
      var meditationReminder by remember { mutableStateOf(true) }
      var exerciseReminder by remember { mutableStateOf(true) }
      var urgeSupportAlert by remember { mutableStateOf(true) }

      PersianCard {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "🔔 سیستم یادآوری و اعلان هوشمند",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "تنظیم زمان‌بندی پیام‌های حمایتی و یادآوری تمرین‌ها",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
          IconButton(onClick = { showNotificationDetails = !showNotificationDetails }) {
            Icon(
              imageVector = if (showNotificationDetails) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
              contentDescription = "جزییات"
            )
          }
        }

        if (showNotificationDetails) {
          Spacer(modifier = Modifier.height(12.dp))
          Divider()
          Spacer(modifier = Modifier.height(10.dp))

          // Morning Reminder
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("🌅 پیام صبحگاهی و توکل (۰۷:۳۰)", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
              Text("الهام‌بخش شروع روز با انرژی و عهد جدید", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(checked = morningReminder, onCheckedChange = { morningReminder = it })
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Meditation Reminder
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("🧘 یادآوری مدیتیشن روزانه (۱۸:۰۰)", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
              Text("تمرین تنفس و آرام‌سازی ذهن در پایان روز", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(checked = meditationReminder, onCheckedChange = { meditationReminder = it })
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Exercise Reminder
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("🏃 یادآوری ورزش و مراقبت بدن (۱۷:۰۰)", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
              Text("حرکت ساده و ورزش سبک روزانه", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(checked = exerciseReminder, onCheckedChange = { exerciseReminder = it })
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Urge Support Alert
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("🌊 پیام‌های حمایتی فوری یار رها", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
              Text("اعلان هوشمند هنگام ثبت شدت وسوسه بالا", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(checked = urgeSupportAlert, onCheckedChange = { urgeSupportAlert = it })
          }
        }
      }

      // 3. Calm Settings Card
      SettingsCard(
        icon = Icons.Default.SelfImprovement,
        title = "تنظیمات آرامش",
        subtitle = "مدیتیشن و صداهای آرام",
        onTap = { showCalmDialog = true }
      )

      // 4. Appearance Card / Theme
      PersianCard {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "ظاهر برنامه (حالت روشن و تاریک)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        ThemeSelector(
          selectedMode = themeMode,
          onModeSelected = { viewModel.updateThemeMode(it) }
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // 5. Privacy Card
      SettingsCard(
        icon = Icons.Default.Lock,
        title = "حریم خصوصی",
        subtitle = "امنیت و قفل برنامه",
        onTap = { showPrivacyDialog = true }
      )

      // 6. About App Card
      SettingsCard(
        icon = Icons.Default.Info,
        title = "درباره رها شو",
        subtitle = "معرفی برنامه",
        onTap = { viewModel.navigateTo(AppScreen.ABOUT_APP) }
      )

      // 7. Creator Card
      SettingsCard(
        icon = Icons.Default.DeveloperMode,
        title = "سازنده برنامه",
        subtitle = "ماموریت و معرفی سازنده",
        onTap = { viewModel.navigateTo(AppScreen.CREATOR) }
      )

      Spacer(modifier = Modifier.height(16.dp))

      // 2.5. Exercise Preferences Settings Card (⚙️ تنظیمات ورزش)
      var showImages by remember { mutableStateOf(true) }
      var showSteps by remember { mutableStateOf(true) }
      var difficultyLevel by remember { mutableStateOf("آسان") }

      PersianCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "⚙️ تنظیمات اختصاصی ورزش و حرکت",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Toggle Show Images
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "نمایش تصاویر حرکات",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "نمایش راهنمای تصویری و نمایه حرکات ورزش",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Switch(
            checked = showImages,
            onCheckedChange = { showImages = it }
          )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Toggle Show Steps
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "نمایش توضیحات مرحله‌ای",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "توضیح گام به گام مراحل انجام صحیح حرکت",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Switch(
            checked = showSteps,
            onCheckedChange = { showSteps = it }
          )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Ability Level Choice
        Text(
          text = "سطح توانایی و شدت حرکات:",
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("آسان", "متوسط").forEach { level ->
            val isSelected = difficultyLevel == level
            FilterChip(
              selected = isSelected,
              onClick = { difficultyLevel = level },
              label = { Text(text = "سطح $level", fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
              leadingIcon = if (isSelected) {
                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
              } else null,
              modifier = Modifier.weight(1f)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 3. Text Size
      PersianCard {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "اندازه فونت نوشته‌ها:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = if (textScale > 1.1f) "بزرگ" else if (textScale < 0.9f) "کوچک" else "معمولی",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
          )
        }

        Slider(
          value = textScale,
          onValueChange = { textScale = it },
          valueRange = 0.8f..1.2f,
          steps = 2
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Logout Button
      CustomButton(
        text = "خروج از حساب کاربری",
        onClick = { viewModel.logoutUser() },
        isSecondary = true,
        icon = Icons.Default.ExitToApp
      )

      Spacer(modifier = Modifier.height(12.dp))

      // 5. Reset / Delete Account Button
      CustomButton(
        text = "حذف حساب و شروع مجدد داده‌ها",
        onClick = { showDeleteDialog = true },
        isSecondary = true,
        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
        contentColor = MaterialTheme.colorScheme.error
      )

      Spacer(modifier = Modifier.height(100.dp))
    }

    // Calm Settings Dialog
    if (showCalmDialog) {
      AlertDialog(
        onDismissRequest = { showCalmDialog = false },
        title = { Text("تنظیمات آرامش و مدیتیشن 🧘") },
        text = {
          Text("در بخش آرامش اپلیکیشن «رها شو» می‌توانید از تمرینات تنفس مربع، نوای ریلکسیشن طبیعت و متون آرامش‌بخش برای مهار ذهنی استرس استفاده کنید.")
        },
        confirmButton = {
          Button(
            onClick = {
              showCalmDialog = false
              viewModel.navigateTo(AppScreen.CALM)
            }
          ) {
            Text("ورود به بخش آرامش")
          }
        },
        dismissButton = {
          TextButton(onClick = { showCalmDialog = false }) {
            Text("بستن")
          }
        }
      )
    }

    // Privacy Dialog
    if (showPrivacyDialog) {
      AlertDialog(
        onDismissRequest = { showPrivacyDialog = false },
        title = { Text("حریم خصوصی کاربران") },
        text = {
          Text("تمام داده‌های نوشته شده در برنامه رها شو (شامل یادداشت‌ها، شدت وسوسه‌ها و سن) به صورت ۱۰۰٪ محلی و امن روی دستگاه شما ذخیره شده و به هیچ سرور خارجی منتقل نمی‌شوند.")
        },
        confirmButton = {
          TextButton(onClick = { showPrivacyDialog = false }) {
            Text("متوجه شدم")
          }
        }
      )
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
      AlertDialog(
        onDismissRequest = { showDeleteDialog = false },
        title = { Text("تایید پاکسازی و شروع مجدد") },
        text = {
          Text("آیا مطمئن هستید که می‌خواهید تمام سوابق دفتر، امتیازات و روند ۳۰ روزه شما پاکسازی شود؟ این عمل غیرقابل بازگشت است.")
        },
        confirmButton = {
          Button(
            onClick = {
              showDeleteDialog = false
              viewModel.resetAllData()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
          ) {
            Text("بله، پاکسازی شود")
          }
        },
        dismissButton = {
          TextButton(onClick = { showDeleteDialog = false }) {
            Text("انصراف")
          }
        }
      )
    }

    // About App Dialog
    if (showAboutDialog) {
      AlertDialog(
        onDismissRequest = { showAboutDialog = false },
        title = { Text("درباره اپلیکیشن «رها شو» 🌿") },
        text = {
          Text("اپلیکیشن «رها شو» یک همراه هوشمند و معنوی ۳۰ روزه برای رهایی از عادات ناخواسته، مهار وسوسه‌ها، پاکسازی ذهن و بازسازی جسم است.\n\nویژگی‌های کلیدی:\n• برنامه روزانه ۳۰ روزه به همراه تمرینات، پادکست‌ها و تدبر روزانه\n• دکمه اورژانس وسوسه و تکنیک‌های فوری تنفس\n• یار هوشمند AI برای گفتگوهای همدلانه و تسکین اضطراب\n• ذخیره‌سازی محلی و حفظ ۱۰۰٪ محرمانه بود داده‌ها")
        },
        confirmButton = {
          TextButton(onClick = { showAboutDialog = false }) {
            Text("بستن")
          }
        }
      )
    }

    // Creator Dialog
    if (showCreatorDialog) {
      AlertDialog(
        onDismissRequest = { showCreatorDialog = false },
        title = { Text("سازنده و هویت رها شو") },
        text = {
          Text("این برنامه با هدف کمک به همدردان در مسیر تغییر، خودشناسی و آرامش طراحی شده است.\n\nشعار ما:\n«رها شدن یک اتفاق ناگهانی نیست؛ یک مسیر است... یک قدم کوچک، هر روز.»\n\nامیدواریم در این مسیر، همواره حسام آرامش و امید را همراه خود داشته باشید.")
        },
        confirmButton = {
          TextButton(onClick = { showCreatorDialog = false }) {
            Text("متوجه شدم")
          }
        }
      )
    }
  }
}

@Composable
fun SettingsCard(
  icon: ImageVector,
  title: String,
  subtitle: String,
  onTap: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    onClick = onTap,
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onPrimaryContainer,
          modifier = Modifier.size(24.dp)
        )
      }

      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Start
        )
      }

      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}
