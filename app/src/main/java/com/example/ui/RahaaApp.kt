package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.RahaaTheme
import com.example.ui.theme.UrgeAlertRed

@Composable
fun RahaaApp(
  viewModel: RahaaViewModel = viewModel()
) {
  val themeMode by viewModel.themeMode.collectAsState()
  val currentScreen by viewModel.currentScreen.collectAsState()

  RahaaTheme(themeMode = themeMode) {
    // Force RTL Direction for complete Persian layout support
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
      Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
          // Hide bottom nav on Splash, Welcome, Auth, ProfileSetup, Emergency, DailyContent
          if (currentScreen in listOf(
              AppScreen.HOME,
              AppScreen.JOURNEY,
              AppScreen.AI_CHAT,
              AppScreen.CALM,
              AppScreen.ACHIEVEMENTS,
              AppScreen.JOURNAL,
              AppScreen.USER_PROFILE
            )
          ) {
            RahaaBottomNavigation(
              currentScreen = currentScreen,
              onNavigate = { viewModel.navigateTo(it) }
            )
          }
        }
      ) { innerPadding ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
          when (currentScreen) {
            AppScreen.SPLASH -> SplashScreen(viewModel)
            AppScreen.SPIRITUAL_INTRO -> SpiritualIntroScreen(viewModel)
            AppScreen.WELCOME -> WelcomeScreen(viewModel)
            AppScreen.AUTH -> AuthScreen(viewModel)
            AppScreen.PROFILE_SETUP -> ProfileSetupScreen(viewModel)
            AppScreen.HOME -> MainDashboardScreen(viewModel)
            AppScreen.JOURNEY -> JourneyScreen(viewModel)
            AppScreen.DAILY_CONTENT -> DailyContentScreen(viewModel)
            AppScreen.URGE_EMERGENCY -> UrgeEmergencyScreen(viewModel)
            AppScreen.JOURNAL -> JournalScreen(viewModel)
            AppScreen.CALM -> CalmScreen(viewModel)
            AppScreen.ACHIEVEMENTS -> AchievementsScreen(viewModel)
            AppScreen.USER_PROFILE -> UserProfileScreen(viewModel)
            AppScreen.SETTINGS -> SettingsScreen(viewModel)
            AppScreen.AI_CHAT -> AiCompanionScreen(viewModel)
            AppScreen.ABOUT_APP -> AboutAppScreen(viewModel)
            AppScreen.CREATOR -> CreatorScreen(viewModel)
            AppScreen.PREMIUM -> PremiumScreen(viewModel)
          }
        }
      }
    }
  }
}

@Composable
fun RahaaBottomNavigation(
  currentScreen: AppScreen,
  onNavigate: (AppScreen) -> Unit
) {
  NavigationBar(
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 8.dp
  ) {
    // 1. Home (خانه)
    NavigationBarItem(
      selected = currentScreen == AppScreen.HOME,
      onClick = { onNavigate(AppScreen.HOME) },
      icon = {
        Icon(
          imageVector = if (currentScreen == AppScreen.HOME) Icons.Filled.Home else Icons.Outlined.Home,
          contentDescription = "خانه"
        )
      },
      label = { Text("خانه", fontWeight = FontWeight.Bold) }
    )

    // 2. Journey (مسیر)
    NavigationBarItem(
      selected = currentScreen == AppScreen.JOURNEY || currentScreen == AppScreen.DAILY_CONTENT,
      onClick = { onNavigate(AppScreen.JOURNEY) },
      icon = {
        Icon(
          imageVector = if (currentScreen == AppScreen.JOURNEY) Icons.Filled.Eco else Icons.Outlined.Eco,
          contentDescription = "مسیر"
        )
      },
      label = { Text("مسیر", fontWeight = FontWeight.Bold) }
    )

    // 3. AI Companion (یار رها)
    NavigationBarItem(
      selected = currentScreen == AppScreen.AI_CHAT,
      onClick = { onNavigate(AppScreen.AI_CHAT) },
      icon = {
        Icon(
          imageVector = if (currentScreen == AppScreen.AI_CHAT) Icons.Filled.SmartToy else Icons.Outlined.SmartToy,
          contentDescription = "یار رها",
          tint = EmeraldPrimary
        )
      },
      label = { Text("یار رها", fontWeight = FontWeight.Bold, color = EmeraldPrimary) }
    )

    // 4. Calm & Health (آرامش)
    NavigationBarItem(
      selected = currentScreen == AppScreen.CALM,
      onClick = { onNavigate(AppScreen.CALM) },
      icon = {
        Icon(
          imageVector = if (currentScreen == AppScreen.CALM) Icons.Filled.SelfImprovement else Icons.Outlined.SelfImprovement,
          contentDescription = "آرامش"
        )
      },
      label = { Text("آرامش", fontWeight = FontWeight.Bold) }
    )

    // 5. Progress & Analytics (پیشرفت)
    NavigationBarItem(
      selected = currentScreen == AppScreen.ACHIEVEMENTS,
      onClick = { onNavigate(AppScreen.ACHIEVEMENTS) },
      icon = {
        Icon(
          imageVector = if (currentScreen == AppScreen.ACHIEVEMENTS) Icons.Filled.Insights else Icons.Outlined.Insights,
          contentDescription = "پیشرفت"
        )
      },
      label = { Text("پیشرفت", fontWeight = FontWeight.Bold) }
    )
  }
}
