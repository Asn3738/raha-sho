package com.example.data.ai

import android.util.Log
import com.example.BuildConfig
import com.example.data.local.AiChatEntity
import com.example.data.local.UserProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class EmotionAnalysisResult(
  val dominantEmotion: String, // e.g. "اضطراب", "امید", "خستگی", "آرامش", "وسوسه"
  val summaryAdvice: String
)

class AIService {

  private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

  private val systemPrompt = """
    تو "یار رها" هستی، همراه و پشتیبان هوشمند، صمیمی و مهربان کاربر در اپلیکیشن "رها شو | Rahaa Sho".
    هدف این برنامه، کمک به کاربران برای ترک عادات ناپسند، غلبه بر وسوسه‌ها، ایجاد انضباط شخصی و طی کردن یک مسیر ۳۰ روزه رهایی است.
    
    ویژگی‌های شخصیت تو (یار رها):
    ۱. لحن گفتگو: بسیار محترمانه، گرم، امیدبخش، بدون قضاوت و با فارسی بسیار روان و شیوا.
    ۲. پشتیبانی و همدردی: به احساسات کاربر گوش بده، او را تشویق کن و احساس ارزشمندی را به او بازگردان.
    ۳. راهکارهای عملی: پاسخ‌های کوتاه و کاربردی بده (مثل تمرین تنفس ۴-۴-۶، نوشیدن آب، تغییر محیط، یا پیاده‌روی کوتاه).
    ۴. سادگی و خلاصه بودن: پاسخ‌ها را خلاصه، خوانا و با فاصله‌گذاری مناسب بنویس تا کاربر در شرایط اضطراب یا وسوسه، پیام را به راحتی بخواند.
    ۵. مرز اخلاقی: تو مشاور یا پزشک درمانی نیستی، بلکه یک دوست و همراه صمیمانه در مسیر خودسازی هستی.
  """.trimIndent()

  /**
   * Main chat function with conversation memory
   */
  suspend fun generateChatResponse(
    userPrompt: String,
    history: List<AiChatEntity>,
    userProfile: UserProfileEntity?,
    todayStatus: com.example.data.local.JournalEntryEntity? = null
  ): String = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY.ifBlank { "" }

    if (apiKey.isBlank()) {
      Log.w("AIService", "GEMINI_API_KEY is missing. Using smart fallback.")
      return@withContext getFallbackChatResponse(userPrompt, todayStatus)
    }

    try {
      val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

      val jsonRequest = JSONObject()

      // System instruction using AIEngineProvider Recovery Context and Decision Engine
      val sysInstructionObj = JSONObject()
      val sysPartsArray = JSONArray()
      
      val recoveryContext = com.example.features.rahaa_ai_engine.providers.AIEngineProvider.buildContext(userProfile, todayStatus)
      val aiProfile = com.example.features.rahaa_ai_engine.providers.AIEngineProvider.buildAIProfile(userProfile, todayStatus)
      val aiDecision = com.example.features.rahaa_ai_engine.providers.AIEngineProvider.process(recoveryContext)
      val enginePrompt = com.example.features.rahaa_ai_engine.providers.AIEngineProvider.buildPrompt(recoveryContext)
      val profilePrompt = com.example.features.rahaa_ai_engine.providers.AIEngineProvider.buildPromptFromProfile(aiProfile, userPrompt)

      val fullPrompt = """
        $systemPrompt

        $profilePrompt

        $enginePrompt

        [تصمیم جاری Recovery AI Engine]
        سطح خطر: ${aiDecision.level}
        پیام راهنما: ${aiDecision.message}
        اقدام پیشنهادی: ${aiDecision.action}
        تمرین فیزیکی: ${aiDecision.exercise}
        مدیتیشن: ${aiDecision.meditation}
      """.trimIndent()

      sysPartsArray.put(JSONObject().put("text", fullPrompt))
      sysInstructionObj.put("parts", sysPartsArray)
      jsonRequest.put("systemInstruction", sysInstructionObj)

      // Contents array (History + New prompt)
      val contentsArray = JSONArray()

      // Limit history to last 10 messages for context
      val recentHistory = history.takeLast(10)
      for (chat in recentHistory) {
        val role = if (chat.sender == "USER") "user" else "model"
        val contentObj = JSONObject()
        contentObj.put("role", role)
        val partsArray = JSONArray()
        partsArray.put(JSONObject().put("text", chat.message))
        contentObj.put("parts", partsArray)
        contentsArray.put(contentObj)
      }

      // Append current user message
      val currentMsgObj = JSONObject()
      currentMsgObj.put("role", "user")
      val currentParts = JSONArray()
      currentParts.put(JSONObject().put("text", userPrompt))
      currentMsgObj.put("parts", currentParts)
      contentsArray.put(currentMsgObj)

      jsonRequest.put("contents", contentsArray)

      // Generation config
      val configObj = JSONObject()
      configObj.put("temperature", 0.7)
      jsonRequest.put("generationConfig", configObj)

      val mediaType = "application/json; charset=utf-8".toMediaType()
      val body = jsonRequest.toString().toRequestBody(mediaType)

      val request = Request.Builder()
        .url(url)
        .post(body)
        .build()

      val response = client.newCall(request).execute()
      val responseBodyStr = response.body?.string()

      if (response.isSuccessful && responseBodyStr != null) {
        val rootObj = JSONObject(responseBodyStr)
        val candidates = rootObj.optJSONArray("candidates")
        if (candidates != null && candidates.length() > 0) {
          val firstCand = candidates.getJSONObject(0)
          val content = firstCand.getJSONObject("content")
          val parts = content.getJSONArray("parts")
          if (parts.length() > 0) {
            val replyText = parts.getJSONObject(0).optString("text", "")
            if (replyText.isNotBlank()) {
              return@withContext replyText.trim()
            }
          }
        }
      }

      Log.e("AIService", "API error response: $responseBodyStr")
      getFallbackChatResponse(userPrompt)

    } catch (e: Exception) {
      Log.e("AIService", "Failed to contact Gemini API", e)
      getFallbackChatResponse(userPrompt)
    }
  }

  /**
   * Analyzes emotion of a journal note
   */
  suspend fun analyzeEmotion(journalNote: String): EmotionAnalysisResult = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY.ifBlank { "" }

    if (apiKey.isBlank() || journalNote.isBlank()) {
      return@withContext EmotionAnalysisResult(
        dominantEmotion = "امیدوار",
        summaryAdvice = "ثبت روزانه احساسات به شما دید شفاف‌تری نسبت به درونتان می‌دهد. به راهت ادامه بده 🌱"
      )
    }

    try {
      val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
      val prompt = """
        متن دفترچه روزانه زیر را تحلیل کن و احساس غالب کاربر را از بین این کلمات انتخاب کن: [آرامش, اضطراب, امید, خشم, خستگی, وسوسه].
        سپس یک جمله کوتاه و صمیمانه حمایتی برای او بنویس.
        
        پاسخ را دقیقا به این فرمت JSON برگردان:
        {"dominantEmotion": "نام_احساس", "summaryAdvice": "جمله_حمایتی"}
        
        متن کاربر: "$journalNote"
      """.trimIndent()

      val jsonRequest = JSONObject()
      val contentsArray = JSONArray()
      val msgObj = JSONObject().put("role", "user")
      msgObj.put("parts", JSONArray().put(JSONObject().put("text", prompt)))
      contentsArray.put(msgObj)
      jsonRequest.put("contents", contentsArray)

      val mediaType = "application/json; charset=utf-8".toMediaType()
      val body = jsonRequest.toString().toRequestBody(mediaType)
      val request = Request.Builder().url(url).post(body).build()

      val response = client.newCall(request).execute()
      val responseBodyStr = response.body?.string()

      if (response.isSuccessful && responseBodyStr != null) {
        val rootObj = JSONObject(responseBodyStr)
        val candidates = rootObj.optJSONArray("candidates")
        if (candidates != null && candidates.length() > 0) {
          val text = candidates.getJSONObject(0).getJSONObject("content").getJSONArray("parts").getJSONObject(0).optString("text", "")
          val cleanedJson = text.replace("```json", "").replace("```", "").trim()
          val resultJson = JSONObject(cleanedJson)
          val emotion = resultJson.optString("dominantEmotion", "امیدوار")
          val advice = resultJson.optString("summaryAdvice", "ثبت احساسات گام مهمی در خودشناسی است.")
          return@withContext EmotionAnalysisResult(dominantEmotion = emotion, summaryAdvice = advice)
        }
      }
    } catch (e: Exception) {
      Log.e("AIService", "Emotion analysis error", e)
    }

    EmotionAnalysisResult(
      dominantEmotion = "امیدوار",
      summaryAdvice = "نوشتن احساسات به ذهن شما نظم می‌بخشد. آفرین بر اراده‌ات!"
    )
  }

  /**
   * Smart fallback generator when API is unreachable or key is missing
   */
  private fun getFallbackChatResponse(
    prompt: String,
    todayStatus: com.example.data.local.JournalEntryEntity? = null
  ): String {
    val lower = prompt.lowercase()
    
    // Tailored guidance if today's craving is high
    if (todayStatus != null && todayStatus.urgeIntensity >= 8 && (lower.contains("وسوسه") || lower.contains("کمک") || lower.contains("حالم"))) {
      return "🌿 الان زمان تصمیم بزرگ نیست؛ فقط این لحظه را مدیریت کنیم.\n\nاول:\n✓ از محیط محرک فاصله بگیر\n✓ چند نفس آرام (۴-۴-۶)\n✓ یک لیوان آب خنک بنوش\n\nمن کنار تو هستم."
    }

    if (todayStatus != null && todayStatus.sleepHours < 4 && (lower.contains("خسته") || lower.contains("انرژی") || lower.contains("کمک"))) {
      return "امروز بدن تو خسته است. اولویت امروز: استراحت، نوشیدن آب کافی و فشار کاری کمتر. من کنار توام 🌱"
    }

    return when {
      lower.contains("وسوسه") || lower.contains("شدید") ->
        "می‌فهمم که این لحظه چقدر سخت و حساسی است. یادتباشد موج وسوسه فقط چند دقیقه طول می‌کشد و فروکش می‌کند. همین الان یک لیوان آب خنک بنوش، ۵ بار تنفس عمیق (۴ ثانیه دم، ۶ ثانیه بازدم) انجام بده و موقعیت فیزیکی‌ات را تغییر بده. تو بسیار قوی‌تر از این احساس زودگذر هستی 🌱"

      lower.contains("استرس") || lower.contains("اضطراب") || lower.contains("ترس") ->
        "اضطراب نشانه هوشیاری ذهن توست، اما لازم نیست کنترل تو را به دست بگیرد. دست‌هایت را روی سینه‌ات بگذار، چشم‌هایت را ببند و ۳ تنفس بسیار عمیق بکش. تو در جای امنی هستی و همه چیز درست خواهد شد."

      lower.contains("انگیزه") || lower.contains("خسته") || lower.contains("ناامید") ->
        "خستگی بخشی از مسیر رشد است، نه نشانه شکست! تا همین‌جا قدم‌های بزرگی برداشته‌ای. به یاد بیاور چرا این مسیر رهایی را شروع کردی. تو لایق یک زندگی روشن، باانضباط و پر از آرامش هستی ✨"

      lower.contains("سلام") || lower.contains("درود") ->
        "سلام دوست عزیز من 🌱 خوشحالم که اینجا هستی. امروز حالت چطوره؟ چطور می‌تونم در مسیر رهایی و آرامش بهت کمک کنم؟"

      else ->
        "ممنونم که احساساتت را با من درمیان گذاشتی. یادمان باشد تغییر یک مسیر تدریجی است و هر قدم کوچک تو ارزشمند است. اگر نیاز داری، تمرین‌های تنفس صفحه آرامش را امتحان کن یا کمی پیاده‌روی کن. من همیشه اینجایم تا همراهت باشم 🌱"
    }
  }
}
