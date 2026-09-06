package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.isAdmin
import com.example.ui.AppScreen
import com.example.ui.RahaaViewModel
import com.example.ui.components.CustomButton
import com.example.ui.components.PersianCard
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAmber

@Composable
fun UserProfileScreen(
  viewModel: RahaaViewModel
) {
  val userProfile by viewModel.userProfile.collectAsState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 18.dp)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(20.dp))

      // Avatar
      Box(
        modifier = Modifier
          .size(110.dp)
          .clip(CircleShape)
          .border(3.dp, EmeraldPrimary, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_rahaa_logo_1785492188226),
          contentDescription = null,
          modifier = Modifier.fillMaxSize()
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = userProfile?.name ?: "همدرد عزیز",
        style = MaterialTheme.typography.displayMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      val isAdmin = userProfile.isAdmin
      if (isAdmin) {
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = GoldAmber.copy(alpha = 0.2f),
          border = BorderStroke(1.dp, GoldAmber)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = GoldAmber, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "👑 دسترسی ادمین (الله اکبر) - قفل‌ها و پرداخت‌ها باز است",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "هدف: ${userProfile?.habitType ?: "ترک عادتهای مخرب"}",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Stats Grid
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        val cleanDaysCount = remember(userProfile?.startDateTimestamp) {
          val start = userProfile?.startDateTimestamp ?: System.currentTimeMillis()
          java.util.concurrent.TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - start).toInt().coerceAtLeast(0)
        }

        Card(
          modifier = Modifier.weight(1f).padding(end = 6.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(Icons.Default.Event, contentDescription = null, tint = EmeraldPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "$cleanDaysCount روز",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "پاکی واقعی تا امروز",
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
            Icon(Icons.Default.Stars, contentDescription = null, tint = GoldAmber)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "${userProfile?.totalPoints ?: 0}",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "کل امتیاز کسب شده",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Premium Membership Banner Card (🌟 اشتراک رها شو پلاس)
      Surface(
        onClick = { viewModel.navigateTo(AppScreen.PREMIUM) },
        shape = RoundedCornerShape(20.dp),
        color = GoldAmber.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, GoldAmber),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = GoldAmber, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = if (userProfile?.isPremium == true || userProfile.isAdmin) "اشتراک رها شو پلاس (فعال 🌟)" else "ارتقا به رها شو پلاس ⭐",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = if (userProfile?.isPremium == true || userProfile.isAdmin) "مشاهده جزئیات و تمدید اشتراک" else "یار رها هوشمند + گزارش‌های کامل + تمرین صوتی",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
          Icon(Icons.Default.ArrowBack, contentDescription = null, tint = GoldAmber)
        }
      }
      PersianCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Favorite, contentDescription = null, tint = EmeraldPrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "دلیل من برای تغییر:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = userProfile?.changeReason ?: "بازگشت به خود واقعی و ساخت آینده‌ای پاک و ارام.",
          style = MaterialTheme.typography.bodyLarge,
          lineHeight = 26.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Recovery Assessment Profile Card
      PersianCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Psychology, contentDescription = null, tint = EmeraldPrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "ارزیابی اختصاصی مسیر رهایی",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          if (!userProfile?.durationText.isNullOrBlank()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = "مدت زمان درگیری: ", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
              Text(text = userProfile?.durationText ?: "", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }

          if (!userProfile?.intensityText.isNullOrBlank()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Bolt, contentDescription = null, tint = GoldAmber, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = "الگو و شدت: ", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
              Text(text = userProfile?.intensityText ?: "", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }

          if (!userProfile?.triggersText.isNullOrBlank()) {
            Row(verticalAlignment = Alignment.Top) {
              Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = "تحریک‌کننده‌ها (Triggers): ", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
              Text(text = userProfile?.triggersText ?: "", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }

          if (!userProfile?.motivationText.isNullOrBlank()) {
            Row(verticalAlignment = Alignment.Top) {
              Icon(Icons.Default.VolunteerActivism, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = "انگیزه اصلی: ", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
              Text(text = userProfile?.motivationText ?: "", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Active Substance Protocol Card
      val habitType = userProfile?.habitType ?: "ترک عادتهای مخرب"
      val activeProtocols = com.example.data.model.SubstanceProtocolManager.getProtocolsForHabits(habitType)

      PersianCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.MedicalServices, contentDescription = null, tint = EmeraldPrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "پروتکل‌های فعال درمانی و رهایی (${activeProtocols.size} مورد)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        activeProtocols.forEach { protocol ->
          Text(
            text = protocol.protocolTitle,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = EmeraldPrimary
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "⏳ اوج خماری و ولع: ${protocol.peakWindow}",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "💡 راهبرد اصلی: ${protocol.keyAdvice}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
          )

          Spacer(modifier = Modifier.height(8.dp))
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Security & Zero Data Sharing Commitment Card
      PersianCard(borderColor = EmeraldPrimary.copy(alpha = 0.4f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Security, contentDescription = null, tint = EmeraldPrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "🛡️ اصل محرمانگی و امنیت ۱۰۰٪ داده‌ها",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "طبق بالاترین استانداردهای جهانی حفاظت از داده‌های پزشکی و بهداشتی، تمامی اطلاعات این اپلیکیشن (شامل نام، عادات، یادداشت‌ها و روند بهبود) فقط روی دیتابیس محلی (Room SQLite) دستگاه شما ذخیره شده و هیچ‌گونه امکان اشتراک‌گذاری، آپلود در سرور ابری یا دسترسی شخص ثالث ندارد.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 20.sp
        )
      }

      Spacer(modifier = Modifier.height(18.dp))

      // About App & Creator Card
      var isCreatorDetailsExpanded by remember { mutableStateOf(false) }

      PersianCard(borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Info, contentDescription = null, tint = EmeraldPrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "درباره برنامه و سازنده اپ",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Creator Sub-Branch Clickable Item
        Surface(
          onClick = { isCreatorDetailsExpanded = !isCreatorDetailsExpanded },
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(EmeraldPrimary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  Icons.Default.Person,
                  contentDescription = null,
                  tint = EmeraldPrimary,
                  modifier = Modifier.size(22.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "طراح و سازنده اپلیکیشن:",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = "احمد سلطانی نوروزی",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = if (isCreatorDetailsExpanded) "برای بستن توضیحات کلیک کنید" else "برای مشاهده توضیحات کامل اپلیکیشن کلیک کنید 👈",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.primary
                )
              }
            }

            Icon(
              imageVector = if (isCreatorDetailsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
              contentDescription = "مشاهده جزئیات",
              tint = MaterialTheme.colorScheme.primary
            )
          }
        }

        AnimatedVisibility(visible = isCreatorDetailsExpanded) {
          Column {
            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = "درباره اپلیکیشن جامع «رها شو»:",
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "اپلیکیشن «رها شو» یک دستیار تخصصی، هوشمند و جامع برای ترک اعتیاد، بهبودی روانی و پاکزیستی است که با هدف همراهی کامل همدردان در مسیر رهایی توسط **احمد سلطانی نوروزی** طراحی گردیده است.\n\n" +
                     "✨ امکانات و ویژگی‌های اصلی:\n" +
                     "• دوره ۳۰ روزه گام‌به‌گام همراه با آموزش‌ها، راهکارهای عملی و خودارزیابی روزانه\n" +
                     "• امکان انتخاب همزمان چند ماده یا عادت مخرب و دریافت پروتکل‌های تخصصی اختصاصی هر کدام\n" +
                     "• پروتکل‌های سم‌زدایی، تغذیه‌ای و مدیریت وسوسه ویژه هر ماده\n" +
                     "• ابزارهای تخصصی شامل دکمه اضطراری وسوسه، تمرین تنفس ریتمیک، ثبت خلق‌وخو و یادداشت‌های روزانه\n" +
                     "• رعایت ۱۰۰٪ اصل محرمانگی و عدم اشتراک‌گذاری اطلاعات با ذخیره‌سازی کاملاً آفلاین روی دیتابیس دستگاه",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 22.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      CustomButton(
        text = "ویرایش مشخصات پروفایل",
        onClick = { viewModel.navigateTo(AppScreen.PROFILE_SETUP) },
        icon = Icons.Default.Edit,
        isSecondary = true
      )

      Spacer(modifier = Modifier.height(10.dp))

      CustomButton(
        text = "تنظیمات برنامه",
        onClick = { viewModel.navigateTo(AppScreen.SETTINGS) },
        icon = Icons.Default.Settings,
        isSecondary = true
      )

      Spacer(modifier = Modifier.height(100.dp))
    }
  }
}
