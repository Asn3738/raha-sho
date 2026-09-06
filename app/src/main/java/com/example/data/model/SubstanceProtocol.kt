package com.example.data.model

data class SubstanceProtocol(
  val categoryId: String,
  val categoryName: String,
  val subTypes: List<String>,
  val protocolTitle: String,
  val withdrawalSymptoms: String,
  val peakWindow: String,
  val nutritionAdvice: String,
  val emergencyCopingTechnique: String,
  val keyAdvice: String,
  val medicalWarning: String,
  val dailyFocusPoints: List<String>
)

object SubstanceProtocolManager {

  val ALL_PROTOCOLS = listOf(
    SubstanceProtocol(
      categoryId = "stimulants",
      categoryName = "مواد محرک (شیشه، کوکائین، آمفتامین، ریتالین)",
      subTypes = listOf("شیشه (آیس)", "کوکائین", "آمفتامین / کاپتاگون", "ریتالین / مت‌آمفتامین"),
      protocolTitle = "پروتکل ماتریس (Matrix Model) و بازسازی سیستم دوپامین",
      withdrawalSymptoms = "افت شدید انرژی و کسالت، خواب‌آلودگی سنگین اولیه، افت خلق و نوسان احساسات، اشتهای کاذب، وسوسه ذهنی شدید.",
      peakWindow = "روزهای ۱ تا ۷ (فاز سقوط) و روزهای ۴۵ تا ۶۰ (فاز دیوار/Wall)",
      nutritionAdvice = "مصرف ویتامین‌های B-Complex (به‌ویژه B1 و B6)، منیزیم، L-Tyrosine طبیعی، آبمیوه طبیعی تازه، پروتئین کافی و کاهش کافئین.",
      emergencyCopingTechnique = "تکنیک توقف افکار (Thought Stopping)، دوش آب خنک و پیاده‌روی سریع برای آزادسازی اندورفین.",
      keyAdvice = "محرک‌ها سیستم پاداش دوپامین را به شدت تخلیه کرده‌اند. ۵ تا ۷ روز اول فاز ترمیم عصبی است. خستگی و کسالت اولیه نشانه هوشمندانه بازسازی مغز شماست؛ صبور باشید.",
      medicalWarning = "در صورت بروز افت خلق شدید یا افکار ناامیدکننده، حتماً با مشاور یا روانپزشک حوزه اعتیاد تماس بگیرید.",
      dailyFocusPoints = listOf(
        "نوشیدن حداقل ۱۰ لیوان آب برای پاکسازی کلیوی",
        "پیاده‌روی یا ورزش سبک برای تحریک دوپامین طبیعی",
        "استراحت کافی و عدم اتخاذ تصمیمات بزرگ در هفته اول",
        "اجتناب کامل از اشخاص و محیط‌های مرتبط با مصرف"
      )
    ),
    SubstanceProtocol(
      categoryId = "opioids",
      categoryName = "مواد افیونی (تریاک، شیره، هروئین، متادون، ترامادول)",
      subTypes = listOf("تریاک و شیره", "هروئین و کراک", "قرص متادون / شربت", "ترامادول / ب۲ (بوپرنورفین)"),
      protocolTitle = "پروتکل مدیریت سم‌پاتیک و سم‌زدایی جسمی-روانی",
      withdrawalSymptoms = "درد عضلانی و استخوانی، بی‌قراری پاها (RLS)، تعریق، بی‌خوابی، اسهال و آبریزش.",
      peakWindow = "روزهای ۳ تا ۵ (مواد کوتاه‌اثر) یا روزهای ۵ تا ۱۰ (متادون و بوپرنورفین)",
      nutritionAdvice = "دمنوش‌های آرام‌بخش (بابونه، گل‌گاوزبان، بادرنجبویه)، مایعات فراوان و الکترولیت (ORS)، غذای گرم و نرم مانند سوپ، ویتامین C و کلسیم-منیزیم.",
      emergencyCopingTechnique = "دوش آب گرم (Hydrotherapy)، ماساژ عضلانی، تنفس عمیق ۴-۷-۸ و پیاده‌روی نرم.",
      keyAdvice = "عوارض جسمی افیون‌ها موقتی است و پس از اوج اولیه سیر نزولی سریع دارد. سم‌زدایی تدریجی یا نگهدارنده تحت نظر پزشک ایمن‌ترین روش است.",
      medicalWarning = "قطع ناگهانی متادون یا ترامادول در دوزهای بالا بدون نظارت پزشکی پیشنهاد نمی‌شود. در صورت نیاز از مشاوره پزشکی استفاده کنید.",
      dailyFocusPoints = listOf(
        "استفاده از دوش آب گرم ۲ تا ۳ بار در روز برای تسکین درد",
        "نوشیدن مایعات گرم و دمنوش‌های بابونه و گل‌گاوزبان",
        "کشش عضلانی و تمرینات تنفسی آرام‌بخش قبل از خواب",
        "پرهیز از ماندن در حالت سکون مطلق؛ حرکت‌های ملایم به بهبود خونرسانی کمک می‌کند"
      )
    ),
    SubstanceProtocol(
      categoryId = "alcohol",
      categoryName = "الکل و مشروبات الکلی",
      subTypes = listOf("مشروبات الکلی سنگین", "مصرف مداوم الکل", "مصرف آخر هفته‌ها"),
      protocolTitle = "پروتکل ایمنی عصبی، تعادل الکترولیتی و تنظیم GABA",
      withdrawalSymptoms = "لرزش دست‌ها، اضطراب شدید، تعریق شبانه، بی‌خوابی، نوسان ضربان قلب.",
      peakWindow = "۲۴ تا ۷۲ ساعت اولیه پس از آخرین مصرف",
      nutritionAdvice = "مصرف حتمی ویتامین B1 (تیامین)، سرم‌های خوراکی (ORS)، آبمیوه‌های طبیعی، موز و مغزیجات برای تثبیت قند و پتاسیم.",
      emergencyCopingTechnique = "تنفس شکمی، استراحت در محیط کم‌نور، نوشیدن آب و هیدراتاسیون مداوم.",
      keyAdvice = "الکل توازن انتقال‌دهنده‌های عصبی GABA و گلوتامات را برهم می‌زند. مصرف B1 و آب‌رسانی مداوم، بازسازی گیرنده‌های مغز را سرعت می‌بخشد.",
      medicalWarning = "در صورت بروز لرزش شدید، توهم، یا تشنج (Delirium Tremens)، فوراً به مراکز اورژانس درمانی مراجعه کنید.",
      dailyFocusPoints = listOf(
        "مصرف مکمل ویتامین B1 پس از وعده غذایی",
        "نوشیدن مداوم آب و محلول‌های الکترولیتی",
        "تنظیم ساعت خواب و اجتناب از کافئین و سیگار زیاد",
        "یادداشت روزانه احساسات برای تخلیه اضطراب"
      )
    ),
    SubstanceProtocol(
      categoryId = "nicotine",
      categoryName = "سیگار، ویپ و نیکوتین",
      subTypes = listOf("سیگار", "ویپ و سیگار الکترونیکی", "قلیان", "ناس و تنباکو"),
      protocolTitle = "پروتکل جایگزینی رفتاری و پاکسازی ریوی (5D Framework)",
      withdrawalSymptoms = "ولع‌های حاد ۳ تا ۵ دقیقه‌ای، بی‌حوصلگی و زودرنجی، سردرد خفیف، سرفه به دلیل پاکسازی ریه.",
      peakWindow = "۴۸ تا ۷۲ ساعت اولیه (خروج کامل نیکوتین از سیستم گردش خون)",
      nutritionAdvice = "نوشیدن آب یخ هنگام موج ولع، چای سبز، المركبات و ویتامین C فراوان، سبزیجات تازه و دانه‌های آفتابگردان.",
      emergencyCopingTechnique = "قانون ۵ دوش (Delay, Deep breath, Drink water, Distract, Discuss) - تاخیر ۵ دقیقه‌ای و نوشیدن آب یخ.",
      keyAdvice = "هر موج ولع نیکوتین حداکثر ۳ تا ۵ دقیقه طول می‌کشد. اگر در این ۵ دقیقه ذهن خود را منحرف کنید، موج ولع فروکش خواهد کرد.",
      medicalWarning = "استفاده از جایگزین‌های نیکوتین (آدامس یا چسب) طبق دستور داروساز می‌تواند شانس موفقیت شما را دو برابر کند.",
      dailyFocusPoints = listOf(
        "دور کردن تمام زیرسیگاری‌ها، فندک‌ها و وسایل مرتبط از خانه و خودرو",
        "همراه داشتن آدامس، چوب دارچین یا میان‌وعده سالم جهت جایگزینی شفاهی",
        "تنفس عمیق ۱۰ تایی هنگام احساس فشار عصبی",
        "تمرکز بر صرفه‌جویی مالی روزانه و ثبت آن در اپلیکیشن"
      )
    ),
    SubstanceProtocol(
      categoryId = "cannabis",
      categoryName = "کانابیس، گل و حشیش",
      subTypes = listOf("گل (ماری‌جوانا)", "حشیش", "کمیکال / اسپایس", "روغن THC / ویپ گل"),
      protocolTitle = "پروتکل تنظیم فاز خواب و شفافیت شناختی",
      withdrawalSymptoms = "بی‌خوابی و رویابینی شدید (REM Rebound)، بی‌اشتهایی اولیه، تعریق کف دست و پا، نوسان خلقی.",
      peakWindow = "روزهای ۳ تا ۷ پس از قطع مصرف",
      nutritionAdvice = "ورزش هوازی برای تعریق و دفع ذخایر چربی، غذاهای سبک و مقوی، دمنوش سنبل‌الطیب قبل از خواب.",
      emergencyCopingTechnique = "تغییر سریع محیط، پیاده‌روی در فضای باز، دوش آب ولرم و تکنیک‌های زمین‌گیری (Grounding 5-4-3-2-1).",
      keyAdvice = "شفافیت ذهنی و تمرکز از هفته دوم پدیدار می‌شود. خواب‌های عجیب روزهای اول نشانه مبارکِ بازسازی فاز خواب عمیق مغز شماست.",
      medicalWarning = "در صورت بروز اضطراب شدید یا حمله پانیک، تمرکز خود را روی تنفس کند و لمس اجسام پیرامون بگذارید.",
      dailyFocusPoints = listOf(
        "ورزش هوازی ۲۰ دقیقه‌ای جهت تعریق و پاکسازی بدنی",
        "رعایت بهداشت خواب (دور کردن گوشی ۱ ساعت قبل خواب)",
        "نوشیدن دمنوش بابونه یا سنبل‌الطیب قبل خواب",
        "یادداشت روزانه برای تثبیت تمرکز و حافظه"
      )
    ),
    SubstanceProtocol(
      categoryId = "behavioral",
      categoryName = "عادات رفتاری و فضای مجازی",
      subTypes = listOf("پورنوگرافی و رفتارهای جنسی مخرب", "قمار و شرط‌بندی آنلاین", "بازی‌های ویدئویی شدید", "شبکه‌های اجتماعی و گوشی"),
      protocolTitle = "پروتکل بازتنظیمی گیرنده‌های دوپامین (Dopamine Detox)",
      withdrawalSymptoms = "احساس کسالت و بی‌حوصلگی (Flatline)، بی‌قراری ذهنی، وسوسه در زمان‌های تنهایی یا خستگی.",
      peakWindow = "روزهای ۷ تا ۱۴ (زمان بازسازی گیرنده‌های D2 دوپامین)",
      nutritionAdvice = "مکمل‌های امگا ۳، پروتئین‌های حاوی تریپتوفان (موز، گردو، تخم‌مرغ)، ورزش روزانه برای ترشح طبیعی اندورفین.",
      emergencyCopingTechnique = "قانون خروج از اتاق (Leave the Room)، فعال کردن ابزارهای مسدودکننده، قرار دادن گوشی بیرون از اتاق خواب.",
      keyAdvice = "مغز شما در حال عادت کردن به سطوح طبیعی و واقعی دوپامین است. فاز بی‌حوصلگی اولیه طبیعی است و نشان‌دهنده درمان مغز است.",
      medicalWarning = "محرک‌های محیطی (Triggers) را مسدود کنید. انزوا و خلوت‌های طولانی در زمان خستگی مهم‌ترین عامل لغزش است.",
      dailyFocusPoints = listOf(
        "بیرون گذاشتن گوشی از اتاق خواب در هنگام شب",
        "فعال‌سازی فیلترهای برنامه‌ای و مسدودکننده‌های تبلیغات",
        "جایگزین کردن ۲ ساعت زمان آزاد با ورزش یا مطالعه کتاب",
        "برقرار کردن ارتباط تلفنی یا حضوری با دوستان و خانواده"
      )
    )
  )

  fun getProtocol(habitTypeStr: String?): SubstanceProtocol {
    return getProtocolsForHabits(habitTypeStr).first()
  }

  fun getProtocolsForHabits(habitTypeStr: String?): List<SubstanceProtocol> {
    val habitType = habitTypeStr?.trim() ?: ""
    if (habitType.isEmpty()) return listOf(ALL_PROTOCOLS.last())

    val typeLower = habitType.lowercase()
    val matched = mutableListOf<SubstanceProtocol>()

    if (typeLower.contains("محرک") || typeLower.contains("شیشه") || typeLower.contains("کوکائین") || typeLower.contains("آمفتامین") || typeLower.contains("آیس") || typeLower.contains("ریتالین")) {
      matched.add(ALL_PROTOCOLS[0])
    }
    if (typeLower.contains("افیون") || typeLower.contains("تریاک") || typeLower.contains("شیره") || typeLower.contains("هروئین") || typeLower.contains("متادون") || typeLower.contains("ترامادول") || typeLower.contains("ب۲") || typeLower.contains("کرک")) {
      matched.add(ALL_PROTOCOLS[1])
    }
    if (typeLower.contains("الکل") || typeLower.contains("مشروب") || typeLower.contains("شراب") || typeLower.contains("آبجو")) {
      matched.add(ALL_PROTOCOLS[2])
    }
    if (typeLower.contains("سیگار") || typeLower.contains("نیکوتین") || typeLower.contains("ویپ") || typeLower.contains("قلیان") || typeLower.contains("تنباکو")) {
      matched.add(ALL_PROTOCOLS[3])
    }
    if (typeLower.contains("کانابیس") || typeLower.contains("گل") || typeLower.contains("حشیش") || typeLower.contains("ماری") || typeLower.contains("کمیکال")) {
      matched.add(ALL_PROTOCOLS[4])
    }
    if (typeLower.contains("رفتار") || typeLower.contains("مجازی") || typeLower.contains("پورن") || typeLower.contains("قمار") || typeLower.contains("بازی") || typeLower.contains("گوشی")) {
      matched.add(ALL_PROTOCOLS[5])
    }

    if (matched.isEmpty()) {
      val found = ALL_PROTOCOLS.filter { protocol ->
        protocol.categoryName.contains(habitType, ignoreCase = true) ||
                protocol.subTypes.any { habitType.contains(it, ignoreCase = true) }
      }
      if (found.isNotEmpty()) return found
      return listOf(ALL_PROTOCOLS[1])
    }

    return matched.distinctBy { it.categoryId }
  }
}
