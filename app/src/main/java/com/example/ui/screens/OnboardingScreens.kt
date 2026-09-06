package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.AppScreen
import com.example.ui.RahaaViewModel
import com.example.ui.components.CustomButton
import com.example.ui.components.PersianCard
import com.example.ui.theme.*
import kotlinx.coroutines.delay

// 1. SplashScreen
@Composable
fun SplashScreen(
  viewModel: RahaaViewModel
) {
  var startAnimation by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    startAnimation = true
    delay(3000)
    val isSetup = viewModel.userProfile.value?.isSetupCompleted == true
    if (isSetup) {
      viewModel.navigateTo(AppScreen.HOME)
    } else {
      viewModel.navigateTo(AppScreen.SPIRITUAL_INTRO)
    }
  }

  Box(
    modifier = Modifier.fillMaxSize()
  ) {
    // Sunrise Background Art
    Image(
      painter = painterResource(id = R.drawable.img_splash_sunrise_1785492161128),
      contentDescription = "طلوع خورشید",
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize()
    )

    // Dark Overlay Gradient
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color.Black.copy(alpha = 0.35f),
              Color.Black.copy(alpha = 0.82f)
            )
          )
        )
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      AnimatedVisibility(
        visible = startAnimation,
        enter = fadeIn(animationSpec = tween(1200)) + scaleIn(initialScale = 0.75f, animationSpec = tween(1200))
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          // Bismillah / Identity Header
          Text(
            text = "به نام رهایی و امید ✨",
            style = TextStyle(
              fontFamily = FontFamily.Serif,
              fontSize = 26.sp,
              fontWeight = FontWeight.Bold,
              color = GoldLight,
              textAlign = TextAlign.Center
            )
          )

          Spacer(modifier = Modifier.height(24.dp))

          Box(
            modifier = Modifier
              .size(135.dp)
              .clip(CircleShape)
              .shadow(16.dp, CircleShape)
              .border(3.5.dp, GoldAmber, CircleShape)
          ) {
            Image(
              painter = painterResource(id = R.drawable.img_rahaa_logo_1785492188226),
              contentDescription = "لوگوی رها شو",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          }

          Spacer(modifier = Modifier.height(20.dp))

          Text(
            text = "رها شو",
            style = TextStyle(
              fontFamily = FontFamily.Serif,
              fontSize = 44.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White,
              textAlign = TextAlign.Center
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          Surface(
            shape = RoundedCornerShape(14.dp),
            color = GoldAmber.copy(alpha = 0.25f),
            border = BorderStroke(1.dp, GoldAmber)
          ) {
            Text(
              text = "۳۰ روز تا شروع دوباره 🌱",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = GoldLight,
              modifier = Modifier.padding(horizontal = 18.dp, vertical = 7.dp)
            )
          }

          Spacer(modifier = Modifier.height(24.dp))

          Text(
            text = "هر تغییر بزرگی،\nبا یک تصمیم کوچک آغاز می‌شود.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            lineHeight = 28.sp,
            color = Color.White.copy(alpha = 0.95f),
            fontWeight = FontWeight.Medium
          )

          Spacer(modifier = Modifier.height(36.dp))

          CircularProgressIndicator(
            modifier = Modifier.size(28.dp),
            color = GoldAmber,
            strokeWidth = 2.5.dp
          )
        }
      }
    }
  }
}

// 1.5 SpiritualIntroScreen (🌿 صفحه مقدمه معنوی: ای همدرد، چرا باید همراه شوی؟)
@Composable
fun SpiritualIntroScreen(
  viewModel: RahaaViewModel
) {
  var isAccepted by remember { mutableStateOf(false) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(16.dp))

      // 1. New Identity Header
      Text(
        text = "به نام رهایی و امید ✨",
        style = TextStyle(
          fontFamily = FontFamily.Serif,
          fontSize = 28.sp,
          fontWeight = FontWeight.Bold,
          color = EmeraldPrimary,
          textAlign = TextAlign.Center
        )
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Heart Icon Accent
      Surface(
        shape = CircleShape,
        color = EmeraldPrimary.copy(alpha = 0.12f),
        border = BorderStroke(1.5.dp, EmeraldPrimary.copy(alpha = 0.4f)),
        modifier = Modifier.size(72.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = EmeraldPrimary,
            modifier = Modifier.size(36.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Brand Name
      Text(
        text = "رها شو",
        style = TextStyle(
          fontFamily = FontFamily.Serif,
          fontSize = 38.sp,
          fontWeight = FontWeight.ExtraBold,
          color = EmeraldPrimary,
          textAlign = TextAlign.Center
        )
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Main Title
      Text(
        text = "ای همدرد،\nچرا باید همراه شوی؟",
        style = TextStyle(
          fontFamily = FontFamily.Serif,
          fontSize = 28.sp,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
          lineHeight = 38.sp
        )
      )

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "هر تغییر بزرگی، با یک تصمیم کوچک آغاز می‌شود.\nدر این مسیر، تو تنها نیستی...",
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        lineHeight = 26.sp
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Emotional Heartfelt Card
      PersianCard(
        borderColor = EmeraldPrimary.copy(alpha = 0.35f),
        backgroundColor = MaterialTheme.colorScheme.surface
      ) {
        Text(
          text = "همراهی برای ساختن یک زندگی آرام‌تر، سالم‌تر و روشن‌تر.\n\nاین مسیر قرار نیست تو را قضاوت کند؛ قرار است پناه و مربی کنار تو باشد.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurface,
          lineHeight = 26.sp,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "در این مسیر یاد می‌گیری:",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = EmeraldPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        val goals = listOf(
          "🌱 خودت را دوباره بشناسی",
          "🧠 افکار و احساساتت را مدیریت کنی",
          "❤️ با خودت مهربان‌تر باشی",
          "🏃 جسم و انرژی را دوباره بسازی",
          "🧘 آرامش عمیق را تمرین کنی",
          "🤲 با امید و توکل قدم برداری"
        )

        goals.forEach { goal ->
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp)
          ) {
            Text(
              text = goal,
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "رها شدن یک اتفاق ناگهانی نیست؛ یک مسیر است...\n\nیک قدم کوچک، هر روز.\n\n🌿 ما همراهت هستیم.",
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary,
          textAlign = TextAlign.Center,
          lineHeight = 24.sp
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Checkbox Card (✓ همراهی من را بپذیر)
      Surface(
        onClick = { isAccepted = !isAccepted },
        shape = RoundedCornerShape(16.dp),
        color = if (isAccepted) EmeraldPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.5.dp, if (isAccepted) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Checkbox(
            checked = isAccepted,
            onCheckedChange = { isAccepted = it },
            colors = CheckboxDefaults.colors(checkedColor = EmeraldPrimary)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "✓ همراهی من را بپذیر",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = if (isAccepted) EmeraldPrimary else MaterialTheme.colorScheme.onSurface
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Button disabled until checkbox is checked
      CustomButton(
        text = "شروع مسیر رهایی 🌱",
        onClick = {
          viewModel.navigateTo(AppScreen.WELCOME)
        },
        enabled = isAccepted,
        containerColor = EmeraldPrimary,
        icon = Icons.Default.ArrowBack
      )

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}

// 2. WelcomeScreen
@Composable
fun WelcomeScreen(
  viewModel: RahaaViewModel
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Spacer(modifier = Modifier.height(10.dp))

      // Card with Addiction Recovery Helping Hands image
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .shadow(8.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp)
      ) {
        Box(modifier = Modifier.height(300.dp)) {
          Image(
            painter = painterResource(id = R.drawable.recovery_helping_hand_1785501716039),
            contentDescription = "دست یاری رسان در حال گرفتن دست بیمار",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                )
              )
          )
          Text(
            text = "ای همدرد، همراه شو",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
              .align(Alignment.BottomStart)
              .padding(20.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      PersianCard {
        Text(
          text = "این مسیر فقط ترک یک عادت نیست؛\nمسیر بازگشت به خود واقعی توست.",
          style = MaterialTheme.typography.titleMedium,
          textAlign = TextAlign.Center,
          lineHeight = 28.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "در این سفر ۳۰ روزه، قدم به قدم همراهی‌ات می‌کنیم تا آرامش، قدرت اراده و هویت جدیدت را بازسازی کنی.",
          style = MaterialTheme.typography.bodyMedium,
          textAlign = TextAlign.Center,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      Column(modifier = Modifier.fillMaxWidth()) {
        CustomButton(
          text = "شروع مسیر من",
          onClick = { viewModel.navigateTo(AppScreen.PROFILE_SETUP) },
          icon = Icons.Default.ArrowBack // RTL direction icon
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomButton(
          text = "ورود به حساب",
          onClick = { viewModel.navigateTo(AppScreen.AUTH) },
          isSecondary = true
        )
      }

      Spacer(modifier = Modifier.height(10.dp))
    }
  }
}

// 3. AuthScreen
@Composable
fun AuthScreen(
  viewModel: RahaaViewModel
) {
  val authMessage by viewModel.authMessage.collectAsState()

  var selectedTab by remember { mutableIntStateOf(0) } // 0: Email, 1: Mobile, 2: Google, 3: Guest
  var isRegisteringMode by remember { mutableStateOf(false) }

  var emailInput by remember { mutableStateOf("") }
  var passwordInput by remember { mutableStateOf("") }
  var nameInput by remember { mutableStateOf("") }
  var mobileNumber by remember { mutableStateOf("") }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(10.dp))

      // Auth Warning Banner if redirected
      if (!authMessage.isNullOrBlank()) {
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = MaterialTheme.colorScheme.errorContainer,
          border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.error),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = authMessage ?: "",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onErrorContainer,
              modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { viewModel.clearAuthMessage() }) {
              Icon(Icons.Default.Close, contentDescription = "بستن", tint = MaterialTheme.colorScheme.onErrorContainer)
            }
          }
        }
        Spacer(modifier = Modifier.height(16.dp))
      }

      // 🌟 Centered Calligraphic Header: "ای همدرد، همراه شو"
      var headerVisible by remember { mutableStateOf(false) }
      LaunchedEffect(Unit) {
        headerVisible = true
      }

      AnimatedVisibility(
        visible = headerVisible,
        enter = fadeIn(animationSpec = tween(1000)) + scaleIn(initialScale = 0.85f, animationSpec = tween(1000))
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(24.dp),
            color = EmeraldPrimary.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "ای همدرد\nهمراه شو",
                textAlign = TextAlign.Center,
                style = TextStyle(
                  fontFamily = FontFamily.Serif,
                  fontSize = 38.sp,
                  fontWeight = FontWeight.Bold,
                  lineHeight = 52.sp,
                  color = EmeraldPrimary
                )
              )

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = "«هیچ قدمی برای تغییر کوچک نیست؛ امروز می‌تواند آغاز دوباره باشد.»",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Box(
        modifier = Modifier
          .size(80.dp)
          .clip(CircleShape)
          .background(EmeraldContainerLight),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.LockReset,
          contentDescription = null,
          tint = EmeraldPrimary,
          modifier = Modifier.size(44.dp)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = if (isRegisteringMode) "ایجاد حساب جدید در رها شو 🌿" else "ورود به حساب کاربری",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "اطلاعات شما کاملاً محرمانه و امن در دستگاه ذخیره می‌شود.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Tab selector for Auth Methods
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant)
          .padding(4.dp)
      ) {
        listOf("ایمیل", "موبایل", "گوگل", "مهمان").forEachIndexed { index, title ->
          val isSelected = selectedTab == index
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent)
              .clickable { selectedTab = index }
              .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = title,
              style = MaterialTheme.typography.labelMedium,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      PersianCard {
        when (selectedTab) {
          0 -> {
            // Email + Password
            if (isRegisteringMode) {
              OutlinedTextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                label = { Text("نام یا نام مستعار") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
              )
              Spacer(modifier = Modifier.height(12.dp))
            }

            OutlinedTextField(
              value = emailInput,
              onValueChange = { emailInput = it },
              label = { Text("آدرس ایمیل") },
              leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
              value = passwordInput,
              onValueChange = { passwordInput = it },
              label = { Text("رمز عبور") },
              leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
              text = if (isRegisteringMode) "ثبت‌نام و ساخت حساب" else "ورود به حساب",
              onClick = {
                val email = if (emailInput.isNotBlank()) emailInput else "user@rahaa.app"
                if (isRegisteringMode) {
                  viewModel.registerUser(email, nameInput, "EMAIL")
                } else {
                  viewModel.loginUser(email, "EMAIL", nameInput)
                }
              }
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
              onClick = { isRegisteringMode = !isRegisteringMode },
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = if (isRegisteringMode) "حساب کاربری دارید؟ ورود کنید" else "حساب کاربری ندارید؟ ایجاد حساب کنید",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }

          1 -> {
            // Mobile + OTP
            OutlinedTextField(
              value = mobileNumber,
              onValueChange = { mobileNumber = it },
              label = { Text("شماره همراه (مثال: ۰۹۱۲۳۴۵۶۷۸۹)") },
              leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
              text = "ارسال کد تایید OTP",
              onClick = {
                val mob = if (mobileNumber.isNotBlank()) mobileNumber else "09120000000"
                viewModel.loginUser("$mob@mobile.rahaa", "MOBILE", "کاربر همراه")
              }
            )
          }

          2 -> {
            // Google Login
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = EmeraldPrimary,
                modifier = Modifier.size(48.dp)
              )
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "ورود سریع و امن با گوگل",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "یک لمس برای هماهنگی و پشتیبان‌گیری ابری حساب کاربری شما.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.height(16.dp))

              CustomButton(
                text = "🔵 ورود با اکانت گوگل",
                onClick = {
                  viewModel.loginUser("user.google@gmail.com", "GOOGLE", "کاربر گوگل")
                }
              )
            }
          }

          3 -> {
            // Guest mode
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
              )
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "ورود به عنوان مهمان (محدود)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(8.dp))

              Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
              ) {
                Text(
                  text = "❌ کاربر مهمان فقط می‌تواند صفحات معرفی و ورود را ببیند. برای دسترسی به داشبورد، مسیر ۳۰ روزه رهایی، یار رها و دفترچه، ساخت حساب الزامی است.",
                  style = MaterialTheme.typography.bodySmall,
                  textAlign = TextAlign.Center,
                  color = MaterialTheme.colorScheme.onErrorContainer,
                  lineHeight = 20.sp,
                  modifier = Modifier.padding(12.dp)
                )
              }

              Spacer(modifier = Modifier.height(16.dp))

              CustomButton(
                text = "مشاهده صفحات معرفی (مهمان)",
                onClick = {
                  viewModel.navigateTo(AppScreen.WELCOME)
                },
                isSecondary = true
              )
            }
          }
        }
      }
    }
  }
}

// 4. ProfileSetupScreen (Recovery Assessment - ارزیابی مسیر رهایی)
@Composable
fun ProfileSetupScreen(
  viewModel: RahaaViewModel
) {
  var currentStep by remember { mutableIntStateOf(0) }
  val totalSteps = 8

  // Form State
  var name by remember { mutableStateOf("") }
  var ageStr by remember { mutableStateOf("25") }

  val allProtocols = com.example.data.model.SubstanceProtocolManager.ALL_PROTOCOLS
  var selectedCategoryIndices by remember { mutableStateOf(setOf(1)) } // Default to Opioids / Substance
  val selectedSubtypesMap = remember { mutableStateMapOf<Int, String>() }
  var customSubtype by remember { mutableStateOf("") }

  var durationText by remember { mutableStateOf("۶ ماه تا ۲ سال") }
  var intensityText by remember { mutableStateOf("تقریباً هر روز") }

  var selectedTriggers by remember { mutableStateOf(setOf("تنهایی", "استرس")) }
  var previousAttempt by remember { mutableStateOf("بله") }
  var relapseReason by remember { mutableStateOf("") }

  var goalText by remember { mutableStateOf("پاکی کامل") }
  var motivationText by remember { mutableStateOf("") }

  val isAdminTyped = name.trim().lowercase() == "الله اکبر" || name.contains("الله اکبر")

  // Helper for final Habit string
  val finalHabitType = remember(selectedCategoryIndices, selectedSubtypesMap, customSubtype) {
    if (selectedCategoryIndices.isEmpty()) {
      "ترک عادتهای مخرب"
    } else {
      selectedCategoryIndices.mapNotNull { idx ->
        val proto = allProtocols.getOrNull(idx) ?: return@mapNotNull null
        val sub = selectedSubtypesMap[idx] ?: proto.subTypes.firstOrNull() ?: ""
        if (sub == "سایر") {
          if (customSubtype.isNotBlank()) "${proto.categoryName} ($customSubtype)" else proto.categoryName
        } else if (sub.isNotBlank()) {
          "${proto.categoryName} ($sub)"
        } else {
          proto.categoryName
        }
      }.joinToString(" + ")
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(10.dp))

      // Header: Progress & Steps
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        if (currentStep > 0) {
          IconButton(onClick = { currentStep-- }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "قبلی", tint = MaterialTheme.colorScheme.primary)
          }
        } else {
          Spacer(modifier = Modifier.width(48.dp))
        }

        Text(
          text = "ارزیابی مسیر رهایی 🌿",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )

        Surface(
          shape = RoundedCornerShape(20.dp),
          color = MaterialTheme.colorScheme.primaryContainer
        ) {
          Text(
            text = "${currentStep + 1} از $totalSteps",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      LinearProgressIndicator(
        progress = { (currentStep + 1).toFloat() / totalSteps },
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(CircleShape),
        color = EmeraldPrimary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Scrollable Content per Step
      Column(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        when (currentStep) {
          0 -> {
            // Step 1: Personal Info & Introduction
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
              Column(modifier = Modifier.padding(20.dp)) {
                Text(
                  text = "🌿 خوش آمدی به رها شو",
                  style = MaterialTheme.typography.headlineSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = "برای اینکه «رها شو» و «یار رها» بتوانند دقیق‌ترین مسیر شخصیسازی‌شده و تمرین‌های روزانه را برای شما آماده کنند، چند سوال کوتاه داریم.",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                  value = name,
                  onValueChange = { name = it },
                  label = { Text("نام یا نام مستعار شما") },
                  leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(12.dp)
                )

                if (isAdminTyped) {
                  Spacer(modifier = Modifier.height(8.dp))
                  Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = GoldAmber.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, GoldAmber)
                  ) {
                    Text(
                      text = "👑 نام ادمین (الله اکبر) وارد شد! دسترسی کامل به تمامی امکانات فعال گردید.",
                      style = MaterialTheme.typography.bodySmall,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(10.dp)
                    )
                  }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                  value = ageStr,
                  onValueChange = { ageStr = it.filter { char -> char.isDigit() } },
                  label = { Text("سن شما") },
                  leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null) },
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(12.dp)
                )
              }
            }
          }

          1 -> {
            // Step 2: Substance & Habit Selection
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
              Column(modifier = Modifier.padding(20.dp)) {
                Text(
                  text = "چه چیزی را می‌خواهی تغییر دهی؟ 🎯",
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "می‌توانی یک یا چند مورد از عادات یا مواد مصرفی را انتخاب کنی.",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                allProtocols.forEachIndexed { index, proto ->
                  val isSelected = selectedCategoryIndices.contains(index)
                  Surface(
                    onClick = {
                      val newSet = selectedCategoryIndices.toMutableSet()
                      if (isSelected) {
                        if (newSet.size > 1) newSet.remove(index)
                      } else {
                        newSet.add(index)
                      }
                      selectedCategoryIndices = newSet
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = BorderStroke(
                      width = if (isSelected) 2.dp else 1.dp,
                      color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(vertical = 4.dp)
                  ) {
                    Row(
                      modifier = Modifier.padding(14.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Checkbox(
                        checked = isSelected,
                        onCheckedChange = { checked ->
                          val newSet = selectedCategoryIndices.toMutableSet()
                          if (checked) newSet.add(index) else if (newSet.size > 1) newSet.remove(index)
                          selectedCategoryIndices = newSet
                        }
                      )
                      Spacer(modifier = Modifier.width(8.dp))
                      Column {
                        Text(
                          text = proto.categoryName,
                          style = MaterialTheme.typography.titleMedium,
                          fontWeight = FontWeight.Bold
                        )
                        Text(
                          text = proto.subTypes.take(4).joinToString("، "),
                          style = MaterialTheme.typography.bodySmall,
                          color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                      }
                    }
                  }
                }
              }
            }
          }

          2 -> {
            // Step 3: Duration
            val durations = listOf("کمتر از ۶ ماه", "۶ ماه تا ۲ سال", "۲ تا ۵ سال", "بیشتر از ۵ سال")
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Column(modifier = Modifier.padding(20.dp)) {
                Text(
                  text = "مدت زمان درگیری با این عادت؟ ⏳",
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = "این اطلاعات به «یار رها» کمک می‌کند تا سطح تمرین‌ها و انتظارات ذهنی را تنظیم کند.",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                durations.forEach { option ->
                  val isSelected = durationText == option
                  Surface(
                    onClick = { durationText = option },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) EmeraldPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(vertical = 5.dp)
                  ) {
                    Row(
                      modifier = Modifier.padding(16.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      RadioButton(
                        selected = isSelected,
                        onClick = { durationText = option }
                      )
                      Spacer(modifier = Modifier.width(10.dp))
                      Text(
                        text = option,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                      )
                    }
                  }
                }
              }
            }
          }

          3 -> {
            // Step 4: Intensity / Pattern
            val intensities = listOf("گاهی و گاه‌گدار", "چند بار در هفته", "تقریباً هر روز", "چند بار در روز")
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Column(modifier = Modifier.padding(20.dp)) {
                Text(
                  text = "الگوی فعلی چقدر شدید است؟ ⚡",
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))

                intensities.forEach { option ->
                  val isSelected = intensityText == option
                  Surface(
                    onClick = { intensityText = option },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) EmeraldPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(vertical = 5.dp)
                  ) {
                    Row(
                      modifier = Modifier.padding(16.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      RadioButton(
                        selected = isSelected,
                        onClick = { intensityText = option }
                      )
                      Spacer(modifier = Modifier.width(10.dp))
                      Text(
                        text = option,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                      )
                    }
                  }
                }
              }
            }
          }

          4 -> {
            // Step 5: Triggers (عوامل تحریک‌کننده)
            val triggerOptions = listOf(
              "🧍‍♂️ تنهایی و انزوا",
              "⚡ استرس و فشار کاری/روانی",
              "💔 غم، ناامیدی و احساس خستگی",
              "👥 دوستان یا محیط قبلی",
              "🌙 بی‌خوابی و کم‌خوابی",
              "💸 مشکلات مالی و نگران‌کننده",
              "🧠 افکار منفی و ناخوشایند"
            )

            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Column(modifier = Modifier.padding(20.dp)) {
                Text(
                  text = "عوامل تحریک‌کننده اصلی (Triggers) 💥",
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "چه شرایطی بیشتر باعث ایجاد وسوسه در شما می‌شود؟ (انتخاب چندگانه)",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                triggerOptions.forEach { trig ->
                  val cleanTrig = trig.substringAfter(" ")
                  val isSelected = selectedTriggers.contains(cleanTrig)

                  Surface(
                    onClick = {
                      val set = selectedTriggers.toMutableSet()
                      if (isSelected) set.remove(cleanTrig) else set.add(cleanTrig)
                      selectedTriggers = set
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(vertical = 4.dp)
                  ) {
                    Row(
                      modifier = Modifier.padding(14.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Checkbox(
                        checked = isSelected,
                        onCheckedChange = { checked ->
                          val set = selectedTriggers.toMutableSet()
                          if (checked) set.add(cleanTrig) else set.remove(cleanTrig)
                          selectedTriggers = set
                        }
                      )
                      Spacer(modifier = Modifier.width(10.dp))
                      Text(
                        text = trig,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                      )
                    }
                  }
                }
              }
            }
          }

          5 -> {
            // Step 6: Previous Attempt
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Column(modifier = Modifier.padding(20.dp)) {
                Text(
                  text = "تجربه تلاش قبلی برای ترک 🛡️",
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                  Surface(
                    onClick = { previousAttempt = "بله" },
                    shape = RoundedCornerShape(16.dp),
                    color = if (previousAttempt == "بله") EmeraldPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    border = BorderStroke(if (previousAttempt == "بله") 2.dp else 1.dp, if (previousAttempt == "بله") EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier.weight(1f)
                  ) {
                    Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                      Text("بله، قبلاً اقدام کردم", fontWeight = FontWeight.Bold)
                    }
                  }

                  Surface(
                    onClick = { previousAttempt = "خیر" },
                    shape = RoundedCornerShape(16.dp),
                    color = if (previousAttempt == "خیر") EmeraldPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    border = BorderStroke(if (previousAttempt == "خیر") 2.dp else 1.dp, if (previousAttempt == "خیر") EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier.weight(1f)
                  ) {
                    Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                      Text("خیر، بار اول است", fontWeight = FontWeight.Bold)
                    }
                  }
                }

                if (previousAttempt == "بله") {
                  Spacer(modifier = Modifier.height(20.dp))
                  OutlinedTextField(
                    value = relapseReason,
                    onValueChange = { relapseReason = it },
                    label = { Text("چه چیزی در نوبتهای قبلی باعث بازگشت شد؟") },
                    placeholder = { Text("مثلاً: فشار استرس شدید، عدم لغزش‌گیری، تنهایی یا احساس خستگی...") },
                    modifier = Modifier
                      .fillMaxWidth()
                      .height(110.dp),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4
                  )
                }
              }
            }
          }

          6 -> {
            // Step 7: Goals & Personal Motivation
            val goals = listOf("🌱 پاکی کامل", "💪 کنترل وسوسه", "🧠 آرامش ذهن", "❤️ بازسازی کامل زندگی", "👨‍👩‍👧 بهبود روابط خانوادگی")

            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Column(modifier = Modifier.padding(20.dp)) {
                Text(
                  text = "هدف اصلی و انگیزه شخصی 🎯",
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))

                goals.forEach { g ->
                  val cleanGoal = g.substringAfter(" ")
                  val isSelected = goalText == cleanGoal
                  Surface(
                    onClick = { goalText = cleanGoal },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) EmeraldPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(vertical = 4.dp)
                  ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                      RadioButton(selected = isSelected, onClick = { goalText = cleanGoal })
                      Spacer(modifier = Modifier.width(8.dp))
                      Text(text = g, style = MaterialTheme.typography.titleMedium, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                    }
                  }
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                  value = motivationText,
                  onValueChange = { motivationText = it },
                  label = { Text("برای چه چیزی یا چه کسانی می‌خواهی تغییر کنی؟") },
                  placeholder = { Text("مثلاً: خانواده‌ام، سلامتی جانی خودم، آینده روشن، عزت نفس و آرامش ذهن...") },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                  shape = RoundedCornerShape(12.dp),
                  maxLines = 4
                )
              }
            }
          }

          7 -> {
            // Step 8: Summary & Final Confirmation
            val activeProtocols = com.example.data.model.SubstanceProtocolManager.getProtocolsForHabits(finalHabitType)

            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Column(modifier = Modifier.padding(20.dp)) {
                Text(
                  text = "نقشه راه اختصاصی شما آماده شد! 🗺️",
                  style = MaterialTheme.typography.headlineSmall,
                  fontWeight = FontWeight.Bold,
                  color = EmeraldPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                  shape = RoundedCornerShape(16.dp),
                  color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                  border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "👤 کاربر: ${if (name.isBlank()) "همدرد عزیز" else name} ($ageStr ساله)", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "📌 برنامه اختصاصی: $finalHabitType", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "⏳ مدت درگیری: $durationText | شدت: $intensityText", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "🎯 هدف اصلی: $goalText", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (selectedTriggers.isNotEmpty()) {
                      Spacer(modifier = Modifier.height(4.dp))
                      Text(text = "💥 شناساگر تحریک‌کننده‌ها: ${selectedTriggers.joinToString("، ")}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                  }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                  text = "پروتکل‌های پزشکی و روانشناسی درمانی فعال شما (${activeProtocols.size} مورد):",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                activeProtocols.forEach { p ->
                  Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                  ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                      Text(text = "📍 ${p.protocolTitle}", fontWeight = FontWeight.Bold)
                      Spacer(modifier = Modifier.height(2.dp))
                      Text(text = p.keyAdvice, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                  }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                  shape = RoundedCornerShape(14.dp),
                  color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                  border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f))
                ) {
                  Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = EmeraldPrimary)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                      text = "🔒 اصل ۱۰۰٪ محرمانگی: تمام داده‌های ارزیابی شما به صورت آفلاین روی حافظه این گوشی ذخیره گردید و به سرور ثالثی ارسال نمی‌شود.",
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      lineHeight = 18.sp
                    )
                  }
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Navigation Action Button
      CustomButton(
        text = if (currentStep == totalSteps - 1) "🌿 ساخت و شروع مسیر من" else "ادامه ➜",
        onClick = {
          if (currentStep < totalSteps - 1) {
            currentStep++
          } else {
            val ageVal = ageStr.toIntOrNull() ?: 25
            val finalName = if (name.isBlank()) "همدرد عزیز" else name
            val finalMotivation = if (motivationText.isNotBlank()) motivationText else goalText

            viewModel.saveProfileSetup(
              name = finalName,
              age = ageVal,
              habitType = finalHabitType,
              changeReason = goalText,
              durationText = durationText,
              intensityText = intensityText,
              triggersText = selectedTriggers.joinToString(", "),
              previousAttempt = previousAttempt,
              relapseReason = relapseReason,
              motivationText = finalMotivation
            )
          }
        }
      )
    }
  }
}
