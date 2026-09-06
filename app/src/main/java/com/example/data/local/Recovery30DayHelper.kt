package com.example.data.local

data class RecoveryDay30Model(
  val day: Int,
  val title: String,
  val spiritual: String,       // 🌿 ۱. توکل و شروع روز
  val prayer: String,          // 🙏 ۲. دعای کوتاه روز
  val verse: String,           // 📖 ۳. آیه یا پیام امید
  val commitment: String,      // ❤️ ۴. عهد امروز با خدا
  val meditationTitle: String, // 🧘 ۵. مدیتیشن
  val meditationDuration: Int, // e.g. 3
  val meditationAudio: String,
  val exerciseTitle: String,   // 🏃 ۶. حرکت بدن
  val exerciseDuration: Int,   // e.g. 5
  val exerciseImage: String,
  val exerciseSteps: List<String>,
  val action: String,          // 🎯 ۷. اقدام کوچک امروز
  val lesson: String,          // 🧠 ۸. آموزش روز
  val practice: String,        // ✍ ۹. تمرین عملی
  val motivation: String       // 💬 ۱۰. جمله انگیزشی
)

object Recovery30DayHelper {

  fun getDayModel(day: Int, entity: DailyContentEntity?): RecoveryDay30Model {
    val exerciseInfo = DailyExerciseMeditationData.getExerciseForDay(day)
    val meditationInfo = DailyExerciseMeditationData.getMeditationForDay(day)

    val defaultSpiritual = entity?.quote1Text?.ifBlank { null }
      ?: when (day % 4) {
        1 -> "امروز را با توکل به ذات پاک پروردگار آغاز می‌کنم و برای ساختن آینده‌ای روشن گام برمی‌دارم."
        2 -> "هر بامداد، عطیه‌ای از جانب خدای مهربان است تا صفای دل و استقامت روح را تجدید کنم."
        3 -> "با ایمان قلبی به توانایی‌های درونی‌ام، تمام نگرانی‌ها را به تسلیم و توکل تبدیل می‌کنم."
        else -> "امروز با آرامش، صبوری و هوشیاری کامل در مسیر نور قدم می‌گذارم."
      }

    val defaultPrayer = entity?.quote2Text?.ifBlank { null }
      ?: when (day % 4) {
        1 -> "خدایا! به من آرامشی عطا فرما تا بپذیرم آنچه را که نمی‌توانم تغییر دهم، و شجاعتی تا تغییر دهم آنچه را که می‌توانم."
        2 -> "پروردگارا! قلبم را از وسوسه‌ها پاک گردان و گام‌هایم را در راه راست استوار بدار."
        3 -> "الهی! نوری از هدایت خویش در دلم بتابان تا در تاریکی‌ها راه سلامت را گم نکنم."
        else -> "ای بخشنده مهربان! اراده‌ام را قوی‌تر از تمام عادات گذشته قرار ده."
      }

    val defaultVerse = entity?.quote3Text?.ifBlank { null }
      ?: when (day % 4) {
        1 -> "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا — همانا پس از هر سختی، آسانی و گشایش است."
        2 -> "وَتَوَكَّلْ عَلَى الْحَيِّ الَّذِي لَا يَمُوتُ — و بر آن زنده‌ای که هرگز نمیرد توکل کن."
        3 -> "إِنَّ اللَّهَ مَعَ الصَّابِرِينَ — همانا خداوند با شکیبان و استواران است."
        else -> "أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ — آگاه باشید که با یاد خدا دل‌ها آرام می‌گیرد."
      }

    val defaultCommitment = when (day % 4) {
      1 -> "امروز عهد می‌بندم که فقط برای همین ۲۴ ساعت، پاک و هوشیار زندگی کنم."
      2 -> "عهد می‌بندم در برابر اولین نشانه وسوسه، ۵ نفس عمیق بکشم و از موقعیت فاصله بگیرم."
      3 -> "امروز عهد می‌بندم با ذهن و جسمم مهربان باشم و تمرینات روزانه را کامل کنم."
      else -> "عهد می‌بندم ارزش‌های جدیدم را بر عادات کهنه ترجیح دهم و صبور باشم."
    }

    val defaultAction = entity?.storyMainMessage?.ifBlank { null }
      ?: when (day % 4) {
        1 -> "یک دلیل شخصی و مهم برای رهایی بنویس و آن را در دسترس بگذار."
        2 -> "تمام محرک‌های منفی اطراف خود را شناسایی کرده و یکی را امروز حذف کن."
        3 -> "یک لیوان آب خنک بنوش و ۳ دقیقه تنفس عمیق شکمی انجام بده."
        else -> "به یک دوست پاک و قدرشناس پیام شکرگزاری بفرست."
      }

    val defaultLesson = entity?.educationalText?.ifBlank { null }
      ?: "تغییر و رهایی از عادات گذشته با قدم‌های کوچک اما مستمر انجام می‌شود. مغز انسان خاصیت پلاستیسیته دارد و می‌تواند مسیرهای عصبی جدید بسازد."

    val defaultPractice = entity?.exerciseText?.ifBlank { null }
      ?: "امروز احساس درونی، سطح انرژی و میزان آرامش خود را در کادر یادداشت ثبت کن."

    val defaultMotivation = entity?.supportMessage?.ifBlank { null }
      ?: "تو توانایی ساختن یک زندگی تازه، سالم و سرشار از فخر و آرامش را داری. من کنار تو هستم 🌱"

    return RecoveryDay30Model(
      day = day,
      title = entity?.titleFa ?: "روز $day رهایی",
      spiritual = defaultSpiritual,
      prayer = defaultPrayer,
      verse = defaultVerse,
      commitment = defaultCommitment,
      meditationTitle = meditationInfo.title,
      meditationDuration = try { meditationInfo.duration.filter { it.isDigit() }.toInt() } catch (e: Exception) { 3 },
      meditationAudio = "meditation/day$day.mp3",
      exerciseTitle = exerciseInfo.title,
      exerciseDuration = try { exerciseInfo.duration.filter { it.isDigit() }.toInt() } catch (e: Exception) { 5 },
      exerciseImage = "exercise/day$day.png",
      exerciseSteps = exerciseInfo.steps.map { it.instructionText },
      action = defaultAction,
      lesson = defaultLesson,
      practice = defaultPractice,
      motivation = defaultMotivation
    )
  }
}
