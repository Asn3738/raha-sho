package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.RahaaViewModel
import com.example.ui.components.PersianCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppScreen(viewModel: RahaaViewModel) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = { viewModel.navigateTo(AppScreen.SETTINGS) }) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "بازگشت"
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "درباره رها شو",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(32.dp))

      Box(
        modifier = Modifier
          .size(100.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Spa,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(70.dp)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      Text(
        text = "رها شو",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )

      Spacer(modifier = Modifier.height(24.dp))

      PersianCard {
        Text(
          text = "رها شو یک همراه هوشمند برای مسیر\nتغییر، آرامش و بازسازی زندگی است.\n\nهدف برنامه کمک به ساختن عادتهای سالم،\nشناخت خود و قدم برداشتن به سوی آینده بهتر است.",
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 32.sp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Release & Security Card
      PersianCard {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(
            text = "🛡 امنیت و انتشار Google Play",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "✓ رمزنکاری داده‌های حساس محلی (AES Encryption)\n✓ حفاظت کامل از اطلاعات با قوانین Firebase Security\n✓ ناشناس‌سازی دادها پیش از تحلیل AI\n✓ پشتیبانی از حذف کامل حساب کاربری\n✓ بهینه‌سازی شده برای Google Play (App Bundle & Obfuscated)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      Text(
        text = "نسخه ۱.۰.۰ | آماده انتشار در Google Play 🚀",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.outline
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorScreen(viewModel: RahaaViewModel) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = { viewModel.navigateTo(AppScreen.SETTINGS) }) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "بازگشت"
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "سازنده برنامه",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(32.dp))

      Box(
        modifier = Modifier
          .size(100.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Favorite,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(60.dp)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      Text(
        text = "تیم رها شو",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )

      Spacer(modifier = Modifier.height(24.dp))

      PersianCard {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "این برنامه با هدف ایجاد یک همراه\nآگاه و حمایتگر برای مسیر تغییر طراحی شده است.\n\nماموریت ما:\nکمک به انسانها برای ساختن زندگی\nآرامتر، سالمتر و هدفمندتر.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 30.sp)
          )
        }
      }
    }
  }
}
