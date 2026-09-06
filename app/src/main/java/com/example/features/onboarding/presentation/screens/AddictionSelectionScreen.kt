package com.example.features.onboarding.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.features.onboarding.presentation.providers.AddictionStateHolder
import com.example.features.onboarding.presentation.widgets.MultiSelectCard
import com.example.ui.components.CustomButton
import com.example.ui.components.PersianCard

/**
 * Onboarding Addiction Selection Screen allowing multi-select categories and custom profiling.
 */
@Composable
fun AddictionSelectionScreen(
  stateHolder: AddictionStateHolder = remember { AddictionStateHolder() },
  onConfirmSelection: (habitType: String, reason: String) -> Unit
) {
  val categories by stateHolder.categories.collectAsState()
  val selectedIds by stateHolder.selectedCategoryIds.collectAsState()
  val selectedSubTypes by stateHolder.selectedSubTypes.collectAsState()
  val changeReason by stateHolder.changeReason.collectAsState()

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

      Text(
        text = "انتخاب عادات و مواد مصرفی",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "دسته‌بندی‌های مدنظر خود را انتخاب کنید. امکان انتخاب همزمان چند مورد وجود دارد تا پروتکل اختصاصی برای شما تنظیم گردد.",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 22.sp
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Category Multi-Select Cards List
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        categories.forEach { category ->
          val isSelected = selectedIds.contains(category.id)
          val subType = selectedSubTypes[category.id] ?: category.subTypes.firstOrNull()

          MultiSelectCard(
            category = category,
            isSelected = isSelected,
            selectedSubType = subType,
            onToggleSelection = { stateHolder.toggleCategory(category.id) },
            onSubTypeSelected = { selected -> stateHolder.selectSubType(category.id, selected) }
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Reason for Change Section
      PersianCard {
        Text(
          text = "دلیل اصلی شما برای شروع این مسیر:",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = changeReason,
          onValueChange = { stateHolder.updateChangeReason(it) },
          placeholder = { Text("چرا می‌خواهید رها شوید؟ (برای سلامتی، خانواده، آرامش...)") },
          modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
          shape = RoundedCornerShape(14.dp),
          maxLines = 4
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy Guarantee Notice
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
          ),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "اطلاعات شما فقط بر روی این دستگاه به صورت محرمانه ذخیره می‌شود.",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        CustomButton(
          text = "تایید و دریافت برنامه ۳۰ روزه",
          onClick = {
            val profile = stateHolder.buildProfile()
            val summaryType = profile.primaryAddictions.joinToString(" + ") {
              "${it.categoryName} (${it.selectedSubType})"
            }
            onConfirmSelection(summaryType, profile.changeReason)
          }
        )
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}
