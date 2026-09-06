package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.isAdmin
import com.example.ui.AppScreen
import com.example.ui.RahaaViewModel
import com.example.ui.components.CustomButton
import com.example.ui.components.PersianCard
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAmber

@Composable
fun PremiumScreen(
  viewModel: RahaaViewModel
) {
  val userProfile by viewModel.userProfile.collectAsState()
  var selectedPlanIndex by remember { mutableStateOf(0) } // Default to Monthly Plan (index 0)
  var showPaymentSuccessDialog by remember { mutableStateOf(false) }

  val isUserPremium = userProfile?.isPremium == true || userProfile.isAdmin

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
      Spacer(modifier = Modifier.height(16.dp))

      // Top Back Navigation Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = { viewModel.navigateTo(AppScreen.USER_PROFILE) }) {
          Icon(Icons.Default.ArrowForward, contentDescription = "بازگشت", tint = MaterialTheme.colorScheme.onSurface)
        }
        Text(
          text = "رها شو پلاس ⭐",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(48.dp))
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Hero Banner Header
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = GoldAmber.copy(alpha = 0.15f),
        border = BorderStroke(1.5.dp, GoldAmber)
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(64.dp)
              .clip(CircleShape)
              .background(GoldAmber.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              Icons.Default.WorkspacePremium,
              contentDescription = null,
              tint = GoldAmber,
              modifier = Modifier.size(38.dp)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "ارتقا به «رها شو پلاس» 🌟",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "دسترسی بی‌محدودیت به هوش مصنوعی همراه، تحلیل‌های تخصصی و تمرین‌های صوتی آرامش",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // If user is already Premium or Admin
      if (isUserPremium) {
        PersianCard(borderColor = EmeraldPrimary) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Verified, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "اشتراک پلاس شما فعال است 🌟",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary
              )
              Text(
                text = "نوع اشتراک: ${userProfile?.subscriptionPlan?.ifBlank { "طلایی پلاس" } ?: "طلایی پلاس"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
              )
              if (!userProfile?.subscriptionExpiry.isNullOrBlank()) {
                Text(
                  text = "اعتبار تا: ${userProfile?.subscriptionExpiry}",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "تمامی امکانات ویژه اپلیکیشن از جمله گفتگو با یار رها، گزارش‌های کامل، و تمرینات پیشرفته برای شما فعال می‌باشد.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
          )
        }

        Spacer(modifier = Modifier.height(20.dp))
      }

      // Plans Selector Section
      Text(
        text = "انتخاب پلن اشتراک:",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Plan 0: Monthly
      PlanCard(
        title = "اشتراک ماهانه 🌿",
        price = "۹۹,۰۰۰ تومان / ماه",
        subtitle = "مناسب برای شروع و تست امکانات",
        isSelected = selectedPlanIndex == 0,
        badgeText = null,
        onClick = { selectedPlanIndex = 0 }
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Plan 1: Lifetime
      PlanCard(
        title = "اشتراک دائمی (مادام‌العمر) 👑",
        price = "۹۹۰,۰۰۰ تومان",
        subtitle = "یکبار پرداخت برای همیشه + تمامی بروزرسانی‌های آینده",
        isSelected = selectedPlanIndex == 1,
        badgeText = "👑 بدون انقضا",
        onClick = { selectedPlanIndex = 1 }
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Premium Features Breakdown Card
      PersianCard {
        Text(
          text = "مقایسه امکانات نسخه رایگان و پلاس 💎",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(14.dp))

        FeatureComparisonRow(
          title = "حافظه هوشمند یار رها (AI)",
          freeText = "محدود به ۵ پیام",
          premiumText = "بی‌محدودیت و کاملاً هوشمند 🧠"
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

        FeatureComparisonRow(
          title = "تحلیل روند پیشرفت و وسوسه",
          freeText = "خلاصه پایه",
          premiumText = "نمودارهای تحلیلی و گزارش ماهانه 📊"
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

        FeatureComparisonRow(
          title = "تمرین‌های صوتی مدیتیشن و تنفس",
          freeText = "۳ تمرین پایه",
          premiumText = "پکیج کامل و به‌روزرسانی‌های جدید 🧘"
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

        FeatureComparisonRow(
          title = "پشتیبانی و حریم خصوصی",
          freeText = "عادی",
          premiumText = "اولویت‌دار + ۱۰۰٪ محرمانه 🛡️"
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Action Button
      val actionText = when (selectedPlanIndex) {
        0 -> "خرید اشتراک ماهانه (۹۹,۰۰۰ تومان)"
        else -> "خرید اشتراک مادام‌العمر (۹۹۰,۰۰۰ تومان) 👑"
      }

      CustomButton(
        text = if (isUserPremium) "تمدید یا تغییر اشتراک" else actionText,
        onClick = {
          val planName = when (selectedPlanIndex) {
            0 -> "ماهانه 🌿"
            else -> "مادام‌العمر 👑"
          }
          val days = when (selectedPlanIndex) {
            0 -> 30
            else -> 3650
          }
          viewModel.activatePremiumSubscription(planName, days)
          showPaymentSuccessDialog = true
        },
        icon = Icons.Default.ShoppingCart
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Guarantee Notice
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
      ) {
        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "تضمین ۷ روز بازگشت وجه در صورت عدم رضایت کامل 🛡️",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(100.dp))
    }
  }

  // Payment Success Dialog
  if (showPaymentSuccessDialog) {
    AlertDialog(
      onDismissRequest = { showPaymentSuccessDialog = false },
      icon = {
        Icon(Icons.Default.Stars, contentDescription = null, tint = GoldAmber, modifier = Modifier.size(48.dp))
      },
      title = {
        Text(
          text = "🎉 تبریک! رها شو پلاس فعال شد",
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center
        )
      },
      text = {
        Text(
          text = "اشتراک ویژه شما با موفقیت فعال گردید. اکنون تمامی امکانات پیشرفته هوش مصنوعی، گزارش‌های ماهانه و تمرین‌های اختصاصی در اختیار شماست.",
          textAlign = TextAlign.Center,
          lineHeight = 22.sp
        )
      },
      confirmButton = {
        Button(
          onClick = {
            showPaymentSuccessDialog = false
            viewModel.navigateTo(AppScreen.HOME)
          },
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
        ) {
          Text("ورود به برنامه")
        }
      }
    )
  }
}

@Composable
private fun PlanCard(
  title: String,
  price: String,
  subtitle: String,
  isSelected: Boolean,
  badgeText: String?,
  onClick: () -> Unit
) {
  val borderColor = if (isSelected) GoldAmber else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
  val containerColor = if (isSelected) GoldAmber.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface

  Surface(
    onClick = onClick,
    shape = RoundedCornerShape(18.dp),
    color = containerColor,
    border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
    modifier = Modifier.fillMaxWidth()
  ) {
    Box(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = GoldAmber)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = title,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = price,
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.ExtraBold,
              color = EmeraldPrimary
            )
            Text(
              text = subtitle,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        if (badgeText != null) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = GoldAmber,
            modifier = Modifier.padding(start = 6.dp)
          ) {
            Text(
              text = badgeText,
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = Color.Black,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun FeatureComparisonRow(
  title: String,
  freeText: String,
  premiumText: String
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Text(
      text = title,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(4.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = "رایگان: $freeText",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Text(
        text = "پلاس: $premiumText",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = EmeraldPrimary
      )
    }
  }
}
