package com.example.features.onboarding.presentation.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.features.onboarding.domain.entities.AddictionCategoryItem

/**
 * Reusable Multi-Select Card Widget for choosing addiction categories and specific sub-types.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MultiSelectCard(
  category: AddictionCategoryItem,
  isSelected: Boolean,
  selectedSubType: String?,
  onToggleSelection: () -> Unit,
  onSubTypeSelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val getCategoryIcon: (String) -> ImageVector = { iconName ->
    when (iconName) {
      "Bolt" -> Icons.Default.Bolt
      "Healing" -> Icons.Default.Healing
      "WineBar" -> Icons.Default.WineBar
      "SmokingRooms" -> Icons.Default.SmokingRooms
      "Psychology" -> Icons.Default.Psychology
      "Smartphone" -> Icons.Default.Smartphone
      else -> Icons.Default.Checklist
    }
  }

  Surface(
    onClick = onToggleSelection,
    shape = RoundedCornerShape(18.dp),
    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    border = BorderStroke(
      width = if (isSelected) 2.dp else 1.dp,
      color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    ),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Checkbox(
          checked = isSelected,
          onCheckedChange = { onToggleSelection() },
          colors = CheckboxDefaults.colors(
            checkedColor = MaterialTheme.colorScheme.primary,
            uncheckedColor = MaterialTheme.colorScheme.outline
          )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
          modifier = Modifier.size(36.dp),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = getCategoryIcon(category.iconName),
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = category.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = category.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 16.sp
          )
        }
      }

      // Expandable SubType chips section when selected
      AnimatedVisibility(visible = isSelected) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
        ) {
          HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "نوع دقیق مصرفی یا عادت:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
          )

          Spacer(modifier = Modifier.height(8.dp))

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            category.subTypes.forEach { subType ->
              val isSubSelected = selectedSubType == subType
              Surface(
                onClick = { onSubTypeSelected(subType) },
                shape = RoundedCornerShape(12.dp),
                color = if (isSubSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                border = BorderStroke(
                  width = if (isSubSelected) 1.5.dp else 1.dp,
                  color = if (isSubSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  if (isSubSelected) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = null,
                      tint = MaterialTheme.colorScheme.onPrimary,
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                  }
                  Text(
                    text = subType,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSubSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSubSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
