package com.example.features.onboarding.presentation.providers

import com.example.features.onboarding.data.models.AddictionProfileModel
import com.example.features.onboarding.domain.entities.AddictionCategoryItem
import com.example.features.onboarding.domain.entities.AddictionProfile
import com.example.features.onboarding.domain.entities.SelectedAddictionItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * State Holder / Provider managing state for onboarding addiction selection.
 */
class AddictionStateHolder {

  private val _categories = MutableStateFlow<List<AddictionCategoryItem>>(AddictionProfileModel.AVAILABLE_CATEGORIES)
  val categories: StateFlow<List<AddictionCategoryItem>> = _categories.asStateFlow()

  private val _selectedCategoryIds = MutableStateFlow<Set<String>>(setOf("opioids"))
  val selectedCategoryIds: StateFlow<Set<String>> = _selectedCategoryIds.asStateFlow()

  private val _selectedSubTypes = MutableStateFlow<Map<String, String>>(
    mapOf("opioids" to "تریاک و شیره")
  )
  val selectedSubTypes: StateFlow<Map<String, String>> = _selectedSubTypes.asStateFlow()

  private val _changeReason = MutableStateFlow("")
  val changeReason: StateFlow<String> = _changeReason.asStateFlow()

  private val _substances = MutableStateFlow<List<String>>(listOf("تریاک و شیره"))
  val substances: StateFlow<List<String>> = _substances.asStateFlow()

  private val _behaviors = MutableStateFlow<List<String>>(emptyList())
  val behaviors: StateFlow<List<String>> = _behaviors.asStateFlow()

  fun toggleSubstance(item: String) {
    val current = _substances.value.toMutableList()
    if (current.contains(item)) {
      current.remove(item)
    } else {
      current.add(item)
    }
    _substances.value = current
  }

  fun toggleBehavior(item: String) {
    val current = _behaviors.value.toMutableList()
    if (current.contains(item)) {
      current.remove(item)
    } else {
      current.add(item)
    }
    _behaviors.value = current
  }

  fun toggleCategory(categoryId: String) {
    val current = _selectedCategoryIds.value.toMutableSet()
    if (current.contains(categoryId)) {
      if (current.size > 1) { // keep at least 1
        current.remove(categoryId)
      }
    } else {
      current.add(categoryId)
      // Auto-assign default subtype if missing
      val cat = _categories.value.find { it.id == categoryId }
      val currentSubs = _selectedSubTypes.value.toMutableMap()
      if (!currentSubs.containsKey(categoryId) && cat != null && cat.subTypes.isNotEmpty()) {
        currentSubs[categoryId] = cat.subTypes.first()
        _selectedSubTypes.value = currentSubs
      }
    }
    _selectedCategoryIds.value = current
  }

  fun selectSubType(categoryId: String, subType: String) {
    val currentSubs = _selectedSubTypes.value.toMutableMap()
    currentSubs[categoryId] = subType
    _selectedSubTypes.value = currentSubs
  }

  fun updateChangeReason(reason: String) {
    _changeReason.value = reason
  }

  fun buildProfile(userId: String = ""): AddictionProfile {
    val selectedItems = _selectedCategoryIds.value.mapNotNull { catId ->
      val cat = _categories.value.find { it.id == catId } ?: return@mapNotNull null
      val sub = _selectedSubTypes.value[catId] ?: cat.subTypes.firstOrNull() ?: ""
      SelectedAddictionItem(
        categoryId = catId,
        categoryName = cat.title,
        selectedSubType = sub
      )
    }

    val substancesList = (selectedItems
      .filter { it.categoryId != "behavioral" }
      .map { "${it.categoryName}: ${it.selectedSubType}" } + _substances.value).distinct()

    val behaviorsList = (selectedItems
      .filter { it.categoryId == "behavioral" }
      .map { "${it.categoryName}: ${it.selectedSubType}" } + _behaviors.value).distinct()

    return AddictionProfile(
      userId = userId,
      substances = substancesList,
      behaviors = behaviorsList,
      primaryAddictions = selectedItems,
      changeReason = _changeReason.value
    )
  }
}
