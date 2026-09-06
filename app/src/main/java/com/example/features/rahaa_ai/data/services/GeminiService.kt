package com.example.features.rahaa_ai.data.services

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Gemini Service providing online Persian empathetic companion responses ("یار رها 🤖").
 */
class GeminiService {

  private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .build()

  suspend fun sendMessage(message: String): String = withContext(Dispatchers.IO) {
    val apiKey = try {
      BuildConfig.GEMINI_API_KEY
    } catch (e: Throwable) {
      ""
    }

    if (apiKey.isNullOrBlank() || apiKey == "null") {
      return@withContext getFallbackEmpatheticResponse(message)
    }

    try {
      val systemPrompt = """
        تو «یار رها» هستی؛ یک دستیار هوشمند، بسیار دلسوز، همدرد، صبور و باتجربه در زمینه ترک اعتیاد، بهبود عادات، سلامت روان و تکنیک‌های CBT و آرام‌سازی.
        لحن تو بسیار گرم، امیدبخش، فارسی صمیمی و بدون قضاوت است.
        برای موج‌های وسوسه، پاسخ‌های تسکین‌دهنده و عملی ارائه بده (مثل توقف، نفس عمیق، نوشیدن آب، تغییر محیط).
        پاسخ‌هایت باید مختصر، خوانا و با علامت‌های ایموجی گل و گیاه 🌿🌱 باشد.
      """.trimIndent()

      val systemInstructionObj = JSONObject().apply {
        put("parts", JSONArray().put(JSONObject().put("text", systemPrompt)))
      }

      val contentObj = JSONObject().apply {
        put("role", "user")
        put("parts", JSONArray().put(JSONObject().put("text", message)))
      }

      val requestObj = JSONObject().apply {
        put("contents", JSONArray().put(contentObj))
        put("systemInstruction", systemInstructionObj)
      }

      val mediaType = "application/json; charset=utf-8".toMediaType()
      val httpRequest = Request.Builder()
        .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
        .post(requestObj.toString().toRequestBody(mediaType))
        .build()

      val httpResponse = client.newCall(httpRequest).execute()
      val responseBodyString = httpResponse.body?.string() ?: ""

      if (httpResponse.isSuccessful && responseBodyString.isNotBlank()) {
        val jsonResponse = JSONObject(responseBodyString)
        val candidates = jsonResponse.optJSONArray("candidates")
        if (candidates != null && candidates.length() > 0) {
          val candidate = candidates.getJSONObject(0)
          val content = candidate.optJSONObject("content")
          val parts = content?.optJSONArray("parts")
          if (parts != null && parts.length() > 0) {
            val text = parts.getJSONObject(0).optString("text")
            if (!text.isNullOrBlank()) {
              return@withContext text
            }
          }
        }
      }

      return@withContext getFallbackEmpatheticResponse(message)
    } catch (e: Exception) {
      return@withContext getFallbackEmpatheticResponse(message)
    }
  }

  private fun getFallbackEmpatheticResponse(message: String): String {
    return when {
      message.contains("وسوسه") || message.contains("🆘") -> """
        من کنار تو هستم 🌿

        این لحظه سخت است، اما احساس وسوسه فقط یک موج موقت ۳ تا ۵ دقیقه‌ای است.

        تمرین فوری ۵ دقیقه‌ای:
        ۱. توقف کامل فعالیت فعلی 🛑
        ۲. ۳ نفس عمیق شکمی کشیدن 🌬️
        ۳. نوشیدن یک لیوان آب خنک 💧
        ۴. تغییر محیط یا پیاده‌روی کوتاه 🚶‍♂️
        ۵. به یاد آوردن دلیل قوی‌ات برای پاکی ✨

        تو قوی‌تر از این موج هستی!
      """.trimIndent()

      message.contains("اضطراب") || message.contains("نگران") -> """
        دستت را روی قلبت بگذار و با من هم‌نفس شو 🌿

        اضطراب نشان‌دهنده بازسازی سیستم عصبی توست. بدن تو در حال پاکسازی و بازیابی تعادل طبیعی است.

        تمرین آرام‌سازی ۴-۷-۸:
        - ۴ ثانیه دم عمیق
        - ۷ ثانیه حبس نفس
        - ۸ ثانیه بازدم آرام

        همه‌چیز درست خواهد شد. تو در امانی 🌱
      """.trimIndent()

      message.contains("تنها") || message.contains("همراه") -> """
        تو تنها نیستی، من و هزاران همدرد دیگر در این مسیر کنار تو هستیم 🤝

        احساس تنهایی در مسیر بهبودی طبیعی است. این فرآیند فرصتی است برای آشتی با خود واقعی‌ات.

        اگر دوست داری، کمی درباره احساسات امروزت با من صحبت کن یا یک یادداشت دلتنگی در دفترچه ثبت کن 💚
      """.trimIndent()

      message.contains("بی‌انگیزه") || message.contains("خسته") -> """
        خستگی تو نشانه ضعف نیست، نشانه این است که داری سخت تلاش می‌کنی 🌿

        انگیزه مثل آب‌وهوا تغییر می‌کند، اما «تعهد» تو به سلامتی‌ات ثابت می‌ماند.

        امروز فقط روی ۱ ساعت آینده تمرکز کن. نیازی نیست کل ۳۰ روز را یک‌جا فتح کنی. فقط همین ساعت را پاک بمان 🌱
      """.trimIndent()

      message.contains("CBT") || message.contains("تمرین") -> """
        تمرین شناختی (CBT) تغییر افکار منفی 🧠:

        فکر منفی: «من نمی‌توانم دوام بیاورم»
        بازسازی منطقی: «من قبلاً هم موج‌های سخت را رد کرده‌ام. این نیز بگذرد و من هر روز قوی‌تر می‌شوم.»

        همین حالا یک فکر منفی را بنویس تا با هم آن را بازسازی کنیم! 📝
      """.trimIndent()

      else -> """
        من کنار تو هستم 🌿

        این لحظه سخت است، اما یک احساس موقت است.
        یک نفس عمیق بکش.
        فقط روی امروز تمرکز کن.
        تو مجبور نیستی کل مسیر را امروز طی کنی 🌱
      """.trimIndent()
    }
  }
}
