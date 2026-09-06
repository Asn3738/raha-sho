package com.example.features.onboarding.data.models

import com.example.features.onboarding.domain.entities.AddictionCategoryItem
import com.example.features.onboarding.domain.entities.AddictionProfile
import com.example.features.onboarding.domain.entities.SelectedAddictionItem

/**
 * Data Model layer for converting between persistence storage and domain entities.
 */
data class SelectedAddictionItemDto(
  val categoryId: String = "",
  val categoryName: String = "",
  val selectedSubType: String = "",
  val customNote: String = ""
)

data class AddictionProfileModel(
  val userId: String = "",
  val substances: List<String> = emptyList(),
  val behaviors: List<String> = emptyList(),
  val selectedAddictions: List<SelectedAddictionItemDto> = emptyList(),
  val changeReason: String = "",
  val targetDays: Int = 30
) {
  fun toDomain(): AddictionProfile {
    return AddictionProfile(
      userId = userId,
      substances = substances,
      behaviors = behaviors,
      primaryAddictions = selectedAddictions.map {
        SelectedAddictionItem(
          categoryId = it.categoryId,
          categoryName = it.categoryName,
          selectedSubType = it.selectedSubType,
          customNote = it.customNote
        )
      },
      changeReason = changeReason,
      targetDays = targetDays
    )
  }

  fun toMap(): Map<String, Any> {
    return mapOf(
      "substances" to substances,
      "behaviors" to behaviors,
      "userId" to userId,
      "changeReason" to changeReason,
      "targetDays" to targetDays
    )
  }

  companion object {
    fun fromMap(map: Map<String, Any?>): AddictionProfileModel {
      @Suppress("UNCHECKED_CAST")
      val substancesList = map["substances"] as? List<String> ?: emptyList()
      @Suppress("UNCHECKED_CAST")
      val behaviorsList = map["behaviors"] as? List<String> ?: emptyList()
      return AddictionProfileModel(
        userId = map["userId"] as? String ?: "",
        substances = substancesList,
        behaviors = behaviorsList,
        changeReason = map["changeReason"] as? String ?: "",
        targetDays = (map["targetDays"] as? Number)?.toInt() ?: 30
      )
    }

    fun fromDomain(domain: AddictionProfile): AddictionProfileModel {
      return AddictionProfileModel(
        userId = domain.userId,
        substances = domain.substances,
        behaviors = domain.behaviors,
        selectedAddictions = domain.primaryAddictions.map {
          SelectedAddictionItemDto(
            categoryId = it.categoryId,
            categoryName = it.categoryName,
            selectedSubType = it.selectedSubType,
            customNote = it.customNote
          )
        },
        changeReason = domain.changeReason,
        targetDays = domain.targetDays
      )
    }

    val AVAILABLE_CATEGORIES = listOf(
      AddictionCategoryItem(
        id = "stimulants",
        title = "مواد محرک (شیشه، کوکائین، ریتالین)",
        iconName = "Bolt",
        description = "بازسازی سیستم دوپامین و افت عصبانیت و کسالت",
        subTypes = listOf("شیشه (آیس)", "کوکائین", "آمفتامین / کاپتاگون", "ریتالین / مت‌آمفتامین")
      ),
      AddictionCategoryItem(
        id = "opioids",
        title = "مواد افیونی (تریاک، شیره، متادون، ترامادول)",
        iconName = "Healing",
        description = "تسکین دردهای عضلانی و سم‌زدایی آرام جسمی",
        subTypes = listOf("تریاک و شیره", "هروئین و کراک", "قرص متادون / شربت", "ترامادول / ب۲")
      ),
      AddictionCategoryItem(
        id = "alcohol",
        title = "الکل و مشروبات الکلی",
        iconName = "WineBar",
        description = "تنظیم گیرنده‌های GABA و تعادل الکترولیتی",
        subTypes = listOf("مشروبات الکلی سنگین", "مصرف مداوم الکل", "مصرف آخر هفته‌ها")
      ),
      AddictionCategoryItem(
        id = "nicotine",
        title = "سیگار، ویپ و نیکوتین",
        iconName = "SmokingRooms",
        description = "مدیریت موج ولع‌های ۳ تا ۵ دقیقه‌ای و پاکسازی ریوی",
        subTypes = listOf("سیگار", "ویپ و سیگار الکترونیکی", "قلیان", "ناس و تنباکو")
      ),
      AddictionCategoryItem(
        id = "cannabis",
        title = "کانابیس، گل و حشیش",
        iconName = "Psychology",
        description = "تنظیم فاز خواب REM و بازگشت شفافیت ذهن",
        subTypes = listOf("گل (ماری‌جوانا)", "حشیش", "کمیکال / اسپایس", "روغن THC / ویپ گل")
      ),
      AddictionCategoryItem(
        id = "behavioral",
        title = "عادات رفتاری و فضای مجازی",
        iconName = "Smartphone",
        description = "پروتکل پاکسازی دوپامین (Dopamine Detox) و کنترل تکانه",
        subTypes = listOf("پورنوگرافی و خلوت‌های مخرب", "قمار و شرط‌بندی آنلاین", "بازی‌های ویدئویی شدید", "شبکه‌های اجتماعی")
      )
    )
  }
}
