package com.example.features.onboarding.domain.entities

/**
 * Domain entity representing the user's addiction profile selection during onboarding.
 */
data class AddictionCategoryItem(
  val id: String,
  val title: String,
  val iconName: String,
  val description: String,
  val subTypes: List<String>
)

data class SelectedAddictionItem(
  val categoryId: String,
  val categoryName: String,
  val selectedSubType: String,
  val customNote: String = ""
)

data class AddictionProfile(
  val userId: String = "",
  val substances: List<String> = emptyList(),
  val behaviors: List<String> = emptyList(),
  val primaryAddictions: List<SelectedAddictionItem> = emptyList(),
  val changeReason: String = "",
  val targetDays: Int = 30,
  val isCustomizedProtocol: Boolean = true
) {
  fun copyWith(
    substances: List<String>? = null,
    behaviors: List<String>? = null,
    primaryAddictions: List<SelectedAddictionItem>? = null,
    changeReason: String? = null,
    targetDays: Int? = null,
    isCustomizedProtocol: Boolean? = null
  ): AddictionProfile {
    return AddictionProfile(
      userId = this.userId,
      substances = substances ?: this.substances,
      behaviors = behaviors ?: this.behaviors,
      primaryAddictions = primaryAddictions ?: this.primaryAddictions,
      changeReason = changeReason ?: this.changeReason,
      targetDays = targetDays ?: this.targetDays,
      isCustomizedProtocol = isCustomizedProtocol ?: this.isCustomizedProtocol
    )
  }
}
