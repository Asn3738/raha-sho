package com.example.features.rahaa_ai_engine.engine

import com.example.features.rahaa_ai_engine.domain.RecoveryContext
import com.example.features.rahaa_ai_engine.domain.RiskLevel

class RiskAnalyzer {

  fun analyze(context: RecoveryContext): RiskLevel {
    if (context.craving >= 9) {
      return RiskLevel.EMERGENCY
    }

    if (context.craving >= 7) {
      return RiskLevel.HIGH
    }

    if (context.craving >= 4 || context.mood == "سخت" || context.sleep < 4) {
      return RiskLevel.MEDIUM
    }

    return RiskLevel.LOW
  }
}
