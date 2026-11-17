package com.example.core

import androidx.compose.ui.unit.LayoutDirection

enum class AppLanguage(
    val code: String,
    val nativeLabel: String,
    val shortBadge: String,
    val ttsLocale: String,
    val isRtl: Boolean
) {
    AR("ar", "العربية", "AR", "ar-XA", true),
    EN("en", "English", "EN", "en-US", false),
    FR("fr", "Français", "FR", "fr-FR", false),
    ES("es", "Español", "ES", "es-ES", false);

    val layoutDirection: LayoutDirection
        get() = if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    companion object {
        fun fromCode(code: String?): AppLanguage =
            entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: EN
    }
}

data class AppStrings(
    val appName: String,
    val tagline: String,
    val signInTitle: String,
    val signInSubtitle: String,
    val signInWithGoogle: String,
    val pillarGlobalTitle: String,
    val pillarGlobalDesc: String,
    val pillarVerifiedTitle: String,
    val pillarVerifiedDesc: String,
    val pillarVoiceAiTitle: String,
    val pillarVoiceAiDesc: String,
    val navDiscover: String,
    val navMap: String,
    val navConcierge: String,
    val navBookings: String,
    val navProfile: String,
    val searchPlaceholder: String,
    val allWorld: String,
    val allServices: String,
    val artisansOnly: String,
    val homeServicesOnly: String,
    val allCategories: String,
    val verifiedProsBannerTitle: String,
    val verifiedProsBannerSub: String,
    val becomeProvider: String,
    val availableToday: String,
    val perHour: String,
    val yearsExp: String,
    val reviewsLabel: String,
    val listenBio: String,
    val stopAudio: String,
    val generatingVoice: String,
    val bookService: String,
    val viewDetails: String,
    val mapTitle: String,
    val nearMeGps: String,
    val openInGoogleMaps: String,
    val mapsKeyNoticeTitle: String,
    val mapsKeyNoticeBody: String,
    val mapsAiDiscoveryBtn: String,
    val conciergeTitle: String,
    val conciergeSubtitle: String,
    val conciergeInputHint: String,
    val conciergeSend: String,
    val searchGroundedBadge: String,
    val mapsGroundedBadge: String,
    val verifiedSources: String,
    val listenAnswer: String,
    val quickPrompts: List<String>,
    val bookingsTitle: String,
    val myBookingsTab: String,
    val providerJobsTab: String,
    val emptyBookingsTitle: String,
    val emptyBookingsDesc: String,
    val confirmBooking: String,
    val markCompleted: String,
    val cancelBooking: String,
    val listenSummary: String,
    val profileTitle: String,
    val languageSection: String,
    val savedFavorites: String,
    val privateContactSection: String,
    val phoneLabel: String,
    val addressLabel: String,
    val saveContact: String,
    val crossPlatformNoteTitle: String,
    val crossPlatformNoteBody: String,
    val signOut: String,
    val publishListingTitle: String,
    val businessNameLabel: String,
    val cityLabel: String,
    val countryLabel: String,
    val continentLabel: String,
    val tradeCategoryLabel: String,
    val serviceKindLabel: String,
    val bioLabel: String,
    val hourlyRateLabel: String,
    val currencyLabel: String,
    val experienceYearsLabel: String,
    val publishButton: String,
    val cancel: String,
    val bookingDialogTitle: String,
    val serviceNeededLabel: String,
    val preferredDateLabel: String,
    val notesLabel: String,
    val submitBooking: String,
    val writeReviewTitle: String,
    val reviewCommentHint: String,
    val submitReview: String,
    val languagesSpoken: String,
    val verifiedReviews: String,
    val noReviewsYet: String,
    val back: String,
    val retry: String,
    val locationPermissionDenied: String,
    val loadingText: String
)

object StringsCatalog {
    fun get(lang: AppLanguage): AppStrings = when (lang) {
        AppLanguage.AR -> arStrings
        AppLanguage.EN -> enStrings
        AppLanguage.FR -> frStrings
        AppLanguage.ES -> esStrings
    }

    fun categoryName(key: String, lang: AppLanguage): String = when (key) {
        "plumbing" -> when (lang) {
            AppLanguage.AR -> "السباكة"
            AppLanguage.FR -> "Plomberie"
            AppLanguage.ES -> "Fontanería"
            AppLanguage.EN -> "Plumbing"
        }
        "electrical" -> when (lang) {
            AppLanguage.AR -> "الكهرباء"
            AppLanguage.FR -> "Électricité"
            AppLanguage.ES -> "Electricidad"
            AppLanguage.EN -> "Electrical"
        }
        "carpentry" -> when (lang) {
            AppLanguage.AR -> "النجارة"
            AppLanguage.FR -> "Menuiserie"
            AppLanguage.ES -> "Carpintería"
            AppLanguage.EN -> "Carpentry"
        }
        "cleaning" -> when (lang) {
            AppLanguage.AR -> "تنظيف المنازل"
            AppLanguage.FR -> "Ménage"
            AppLanguage.ES -> "Limpieza"
            AppLanguage.EN -> "Home Cleaning"
        }
        "hvac" -> when (lang) {
            AppLanguage.AR -> "التكييف والتدفئة"
            AppLanguage.FR -> "Climatisation"
            AppLanguage.ES -> "Climatización"
            AppLanguage.EN -> "HVAC & AC"
        }
        "appliance" -> when (lang) {
            AppLanguage.AR -> "إصلاح الأجهزة"
            AppLanguage.FR -> "Électroménager"
            AppLanguage.ES -> "Electrodomésticos"
            AppLanguage.EN -> "Appliance Repair"
        }
        "painting" -> when (lang) {
            AppLanguage.AR -> "الصباغة والديكور"
            AppLanguage.FR -> "Peinture"
            AppLanguage.ES -> "Pintura"
            AppLanguage.EN -> "Painting"
        }
        "zellige" -> when (lang) {
            AppLanguage.AR -> "الزليج والفسيفساء"
            AppLanguage.FR -> "Zellige & Mosaïque"
            AppLanguage.ES -> "Azulejo y Mosaico"
            AppLanguage.EN -> "Tile & Zellige"
        }
        "metalwork" -> when (lang) {
            AppLanguage.AR -> "الحدادة الفنية"
            AppLanguage.FR -> "Ferronnerie"
            AppLanguage.ES -> "Herrería"
            AppLanguage.EN -> "Metalwork"
        }
        "gardening" -> when (lang) {
            AppLanguage.AR -> "البستنة"
            AppLanguage.FR -> "Jardinage"
            AppLanguage.ES -> "Jardinería"
            AppLanguage.EN -> "Gardening"
        }
        "moving" -> when (lang) {
            AppLanguage.AR -> "النقل والتركيب"
            AppLanguage.FR -> "Déménagement"
            AppLanguage.ES -> "Mudanzas"
            AppLanguage.EN -> "Moving & Assembly"
        }
        "smart_home" -> when (lang) {
            AppLanguage.AR -> "المنزل الذكي"
            AppLanguage.FR -> "Domotique"
            AppLanguage.ES -> "Hogar Inteligente"
            AppLanguage.EN -> "Smart Home"
        }
        else -> key
    }

    fun continentName(key: String, lang: AppLanguage): String = when (key) {
        "africa" -> when (lang) {
            AppLanguage.AR -> "أفريقيا"
            AppLanguage.FR -> "Afrique"
            AppLanguage.ES -> "África"
            AppLanguage.EN -> "Africa"
        }
        "europe" -> when (lang) {
            AppLanguage.AR -> "أوروبا"
            AppLanguage.FR -> "Europe"
            AppLanguage.ES -> "Europa"
            AppLanguage.EN -> "Europe"
        }
        "asia" -> when (lang) {
            AppLanguage.AR -> "آسيا والشرق الأوسط"
            AppLanguage.FR -> "Asie & Moyen-Orient"
            AppLanguage.ES -> "Asia y Medio Oriente"
            AppLanguage.EN -> "Asia & Middle East"
        }
        "north_america" -> when (lang) {
            AppLanguage.AR -> "أمريكا الشمالية"
            AppLanguage.FR -> "Amérique du Nord"
            AppLanguage.ES -> "Norteamérica"
            AppLanguage.EN -> "North America"
        }
        "south_america" -> when (lang) {
            AppLanguage.AR -> "أمريكا الجنوبية"
            AppLanguage.FR -> "Amérique du Sud"
            AppLanguage.ES -> "Sudamérica"
            AppLanguage.EN -> "South America"
        }
        "oceania" -> when (lang) {
            AppLanguage.AR -> "أوقيانوسيا"
            AppLanguage.FR -> "Océanie"
            AppLanguage.ES -> "Oceanía"
            AppLanguage.EN -> "Oceania"
        }
        else -> key
    }

    fun verificationName(key: String, lang: AppLanguage): String = when (key) {
        "unverified" -> when (lang) {
            AppLanguage.AR -> "غير موثق بعد"
            AppLanguage.FR -> "Non vérifié"
            AppLanguage.ES -> "Sin verificar"
            AppLanguage.EN -> "Not verified"
        }
        "master_artisan" -> when (lang) {
            AppLanguage.AR -> "معلم حرفي معتمد"
            AppLanguage.FR -> "Maître Artisan Certifié"
            AppLanguage.ES -> "Maestro Artesano Certificado"
            AppLanguage.EN -> "Certified Master Artisan"
        }
        "insured_pro" -> when (lang) {
            AppLanguage.AR -> "محترف مؤمَّن"
            AppLanguage.FR -> "Pro Assuré"
            AppLanguage.ES -> "Profesional Asegurado"
            AppLanguage.EN -> "Insured Pro"
        }
        else -> when (lang) {
            AppLanguage.AR -> "هوية موثقة"
            AppLanguage.FR -> "Identité Vérifiée"
            AppLanguage.ES -> "Identidad Verificada"
            AppLanguage.EN -> "ID Verified"
        }
    }

    fun bookingStatusName(key: String, lang: AppLanguage): String = when (key) {
        "confirmed" -> when (lang) {
            AppLanguage.AR -> "مؤكد"
            AppLanguage.FR -> "Confirmé"
            AppLanguage.ES -> "Confirmado"
            AppLanguage.EN -> "Confirmed"
        }
        "completion_pending" -> when (lang) {
            AppLanguage.AR -> "في انتظار تأكيد الزبون"
            AppLanguage.FR -> "En attente de confirmation"
            AppLanguage.ES -> "Pendiente de confirmación"
            AppLanguage.EN -> "Awaiting customer confirmation"
        }
        "completed" -> when (lang) {
            AppLanguage.AR -> "مكتمل"
            AppLanguage.FR -> "Terminé"
            AppLanguage.ES -> "Completado"
            AppLanguage.EN -> "Completed"
        }
        "cancelled" -> when (lang) {
            AppLanguage.AR -> "ملغى"
            AppLanguage.FR -> "Annulé"
            AppLanguage.ES -> "Cancelado"
            AppLanguage.EN -> "Cancelled"
        }
        else -> when (lang) {
            AppLanguage.AR -> "قيد الانتظار"
            AppLanguage.FR -> "En attente"
            AppLanguage.ES -> "Pendiente"
            AppLanguage.EN -> "Pending"
        }
    }

    private val arStrings = AppStrings(
        appName = "TrustCraft",
        tagline = "السوق العالمي للحرفيين والخدمات المنزلية الموثوقة",
        signInTitle = "حرفيون موثوقون أينما كنت في العالم",
        signInSubtitle = "اكتشف أفضل الحرفيين وخبراء الصيانة المنزلية في مدينتك أو في أي قارة مع خرائط جوجل والمساعد الذكي بـ 4 لغات.",
        signInWithGoogle = "تسجيل الدخول عبر Google",
        pillarGlobalTitle = "تغطية عالمية بدون حدود",
        pillarGlobalDesc = "من الدار البيضاء ومدريد وباريس إلى دبي ونيويورك ومكسيكو سيتي.",
        pillarVerifiedTitle = "ثقة وتقييمات موثقة",
        pillarVerifiedDesc = "تحقق من الشارات، الخبرة، والتعليقات الحقيقية قبل الحجز.",
        pillarVoiceAiTitle = "بحث فوري وقراءة صوتية ذكية",
        pillarVoiceAiDesc = "أسعار محلية محدثة عبر بحث جوجل وقراءة صوتية بـ 4 لغات.",
        navDiscover = "استكشاف",
        navMap = "الخريطة",
        navConcierge = "المساعد",
        navBookings = "الحجوزات",
        navProfile = "حسابي",
        searchPlaceholder = "ابحث عن سباك، زليج، كهربائي، مدينة...",
        allWorld = "كل العالم",
        allServices = "الكل",
        artisansOnly = "حرفيون",
        homeServicesOnly = "خدمات منزلية",
        allCategories = "كل التخصصات",
        verifiedProsBannerTitle = "حرفيون وخدمات مضمونة عالمياً",
        verifiedProsBannerSub = "احجز فوراً أو انشر خدماتك باللغات الأربع",
        becomeProvider = "سجّل كحرفي",
        availableToday = "متاح اليوم",
        perHour = "/ ساعة",
        yearsExp = "سنة خبرة",
        reviewsLabel = "تقييم",
        listenBio = "استماع للتعريف",
        stopAudio = "إيقاف الصوت",
        generatingVoice = "جاري توليد الصوت...",
        bookService = "احجز الآن",
        viewDetails = "التفاصيل",
        mapTitle = "خريطة الخدمات العالمية",
        nearMeGps = "موقعي الحالي",
        openInGoogleMaps = "فتح في خرائط Google",
        mapsKeyNoticeTitle = "معاينة الخريطة التفاعلية",
        mapsKeyNoticeBody = "أضف MAPS_API_KEY في إعدادات الأسرار (Secrets) في AI Studio لتفعيل خرائط Google التفاعلية الكاملة.",
        mapsAiDiscoveryBtn = "اقتراحات أماكن وموردين قريبين (AI)",
        conciergeTitle = "مستشار TrustCraft الذكي",
        conciergeSubtitle = "مدعوم ببحث Google المحدث لتقدير الأسعار واللوائح المحلية عالمياً",
        conciergeInputHint = "اسأل عن متوسط تكلفة سباك في الدار البيضاء أو شروط التصريح في مدريد...",
        conciergeSend = "بحث ذكي",
        searchGroundedBadge = "مؤكد ببحث Google",
        mapsGroundedBadge = "مؤكد بخرائط Google",
        verifiedSources = "المصادر الموثقة",
        listenAnswer = "استماع للإجابة",
        quickPrompts = listOf(
            "ما متوسط أسعار السباكة والكهرباء في الدار البيضاء ومدريد وباريس حالياً؟",
            "نصائح صيانة المكيف المنزلي قبل الصيف وتقدير التكلفة في دبي والرياض",
            "متطلبات ترميم الزليج المغربي التقليدي والوقت المتوقع للعمل"
        ),
        bookingsTitle = "إدارة الحجوزات والطلبات",
        myBookingsTab = "حجوزاتي",
        providerJobsTab = "طلبات الحرفيين",
        emptyBookingsTitle = "لا توجد حجوزات بعد",
        emptyBookingsDesc = "تصفح الحرفيين وخدمات المنزل في أي مدينة واحجز خدمتك الأولى بكل ثقة.",
        confirmBooking = "تأكيد الطلب",
        markCompleted = "إتمام الخدمة",
        cancelBooking = "إلغاء",
        listenSummary = "استماع للملخص",
        profileTitle = "الملف الشخصي والإعدادات",
        languageSection = "لغة التطبيق والاتجاه",
        savedFavorites = "الحرفيون المفضلون",
        privateContactSection = "معلومات التواصل الخاصة (محمية)",
        phoneLabel = "رقم الهاتف",
        addressLabel = "عنوان الخدمة",
        saveContact = "حفظ المعلومات الخاصة",
        crossPlatformNoteTitle = "جاهز للتشغيل عبر المنصات (Android & iOS)",
        crossPlatformNoteBody = "هذه النسخة مبنية لنظام Android (Kotlin & Compose) مع قاعدة بيانات Firebase Firestore سحابية موحدة يمكن ربطها مباشرة بتطبيق iPhone (SwiftUI).",
        signOut = "تسجيل الخروج",
        publishListingTitle = "انشر خدمتك في السوق العالمي",
        businessNameLabel = "اسم الورشة أو المزود",
        cityLabel = "المدينة",
        countryLabel = "الدولة",
        continentLabel = "القارة",
        tradeCategoryLabel = "التخصص",
        serviceKindLabel = "نوع النشاط",
        bioLabel = "وصف الخبرة والخدمات",
        hourlyRateLabel = "السعر بالساعة",
        currencyLabel = "العملة",
        experienceYearsLabel = "سنوات الخبرة",
        publishButton = "نشر في السوق العالمي",
        cancel = "إلغاء",
        bookingDialogTitle = "حجز خدمة موثوقة",
        serviceNeededLabel = "الخدمة المطلوبة",
        preferredDateLabel = "الموعد المفضل (مثال: 2026-11-22 10:00)",
        notesLabel = "ملاحظات إضافية",
        submitBooking = "إرسال طلب الحجز",
        writeReviewTitle = "أضف تقييمك",
        reviewCommentHint = "شارك تجربتك مع هذا الحرفي...",
        submitReview = "نشر التقييم",
        languagesSpoken = "اللغات المتحدث بها",
        verifiedReviews = "تقييمات العملاء",
        noReviewsYet = "لا توجد تعليقات إضافية بعد. كن أول من يشارك تجربته!",
        back = "رجوع",
        retry = "إعادة المحاولة",
        locationPermissionDenied = "تم رفض إذن الموقع — يمكنك الاختيار يدوياً من الخريطة العالمية.",
        loadingText = "جاري التحميل..."
    )

    private val enStrings = AppStrings(
        appName = "TrustCraft",
        tagline = "Global Trusted Artisans & Home Services Marketplace",
        signInTitle = "Trusted craftspeople & home pros, everywhere",
        signInSubtitle = "Discover vetted artisans and home service specialists across every continent with Google Maps, live Search-grounded pricing, and 4-language voice support.",
        signInWithGoogle = "Sign in with Google",
        pillarGlobalTitle = "Truly Global Coverage",
        pillarGlobalDesc = "Casablanca, Madrid, Paris, Dubai, New York, Tokyo, Mexico City & beyond.",
        pillarVerifiedTitle = "Verified Trust & Reviews",
        pillarVerifiedDesc = "Inspect craftsmanship badges, experience, and authentic client ratings.",
        pillarVoiceAiTitle = "Live Search AI & Voice Reader",
        pillarVoiceAiDesc = "Up-to-date local cost estimates via Google Search & natural TTS audio.",
        navDiscover = "Discover",
        navMap = "Map",
        navConcierge = "Concierge",
        navBookings = "Bookings",
        navProfile = "Profile",
        searchPlaceholder = "Search plumber, zellige, electrician, city...",
        allWorld = "All World",
        allServices = "All",
        artisansOnly = "Artisans",
        homeServicesOnly = "Home Services",
        allCategories = "All Trades",
        verifiedProsBannerTitle = "Vetted Artisans & Home Pros Worldwide",
        verifiedProsBannerSub = "Book trusted experts or publish your craft globally",
        becomeProvider = "List Service",
        availableToday = "Available Today",
        perHour = "/ hr",
        yearsExp = "yrs exp",
        reviewsLabel = "reviews",
        listenBio = "Listen to Bio",
        stopAudio = "Stop Audio",
        generatingVoice = "Generating voice...",
        bookService = "Book Now",
        viewDetails = "View Details",
        mapTitle = "Global Service Map",
        nearMeGps = "Near Me",
        openInGoogleMaps = "Open in Google Maps",
        mapsKeyNoticeTitle = "Interactive World Explorer",
        mapsKeyNoticeBody = "Add MAPS_API_KEY in AI Studio Secrets to enable full live Google Maps vector tiles.",
        mapsAiDiscoveryBtn = "Find Nearby Suppliers & Insights (AI)",
        conciergeTitle = "TrustCraft AI Concierge",
        conciergeSubtitle = "Powered by Google Search Grounding for live global rates & repair guides",
        conciergeInputHint = "Ask about plumber rates in Madrid, zellige care, or HVAC checks in Dubai...",
        conciergeSend = "Ask AI",
        searchGroundedBadge = "Google Search Grounded",
        mapsGroundedBadge = "Google Maps Grounded",
        verifiedSources = "Verified Sources",
        listenAnswer = "Listen to Answer",
        quickPrompts = listOf(
            "What are typical hourly rates for licensed electricians in Madrid, Paris, and Casablanca?",
            "Best seasonal HVAC maintenance checklist and pricing in Dubai or New York",
            "How to evaluate authentic Moroccan Zellige tile craftsmanship before hiring"
        ),
        bookingsTitle = "Bookings & Jobs",
        myBookingsTab = "My Bookings",
        providerJobsTab = "Incoming Jobs",
        emptyBookingsTitle = "No bookings yet",
        emptyBookingsDesc = "Explore verified artisans and home pros across any continent to book your first service.",
        confirmBooking = "Confirm",
        markCompleted = "Complete",
        cancelBooking = "Cancel",
        listenSummary = "Read Summary",
        profileTitle = "Profile & Settings",
        languageSection = "App Language (4 Languages)",
        savedFavorites = "Saved Artisans",
        privateContactSection = "Private Contact Info (Owner-Only)",
        phoneLabel = "Phone Number",
        addressLabel = "Service Address",
        saveContact = "Save Private Info",
        crossPlatformNoteTitle = "Cross-Platform Ready (Android & iOS)",
        crossPlatformNoteBody = "This workspace builds the native Android Jetpack Compose app backed by a shared Firebase Firestore cloud schema ready for an iOS SwiftUI companion.",
        signOut = "Sign Out",
        publishListingTitle = "Publish to Global Marketplace",
        businessNameLabel = "Studio or Business Name",
        cityLabel = "City",
        countryLabel = "Country",
        continentLabel = "Continent",
        tradeCategoryLabel = "Trade Category",
        serviceKindLabel = "Service Type",
        bioLabel = "Experience & Craftsmanship Bio",
        hourlyRateLabel = "Hourly Rate",
        currencyLabel = "Currency",
        experienceYearsLabel = "Years of Experience",
        publishButton = "Publish Listing",
        cancel = "Cancel",
        bookingDialogTitle = "Book Trusted Service",
        serviceNeededLabel = "Service Needed",
        preferredDateLabel = "Preferred Date & Time (e.g. 2026-11-22 10:00)",
        notesLabel = "Job Notes",
        submitBooking = "Confirm Request",
        writeReviewTitle = "Leave a Review",
        reviewCommentHint = "Share your experience with this professional...",
        submitReview = "Post Review",
        languagesSpoken = "Languages Spoken",
        verifiedReviews = "Client Reviews",
        noReviewsYet = "No written reviews yet. Be the first to review!",
        back = "Back",
        retry = "Retry",
        locationPermissionDenied = "Location permission denied — browse any global city from the map.",
        loadingText = "Loading..."
    )

    private val frStrings = AppStrings(
        appName = "TrustCraft",
        tagline = "Place de marché mondiale d'artisans et services à domicile",
        signInTitle = "Artisans et experts fiables partout dans le monde",
        signInSubtitle = "Trouvez des artisans qualifiés et des services à domicile sur chaque continent avec Google Maps, recherche IA en direct et audio en 4 langues.",
        signInWithGoogle = "Se connecter avec Google",
        pillarGlobalTitle = "Couverture Mondiale Sans Limite",
        pillarGlobalDesc = "Casablanca, Madrid, Paris, Dubaï, New York, Montréal, Tokyo et plus.",
        pillarVerifiedTitle = "Confiance & Avis Vérifiés",
        pillarVerifiedDesc = "Consultez les badges de certification, l'expérience et les avis clients.",
        pillarVoiceAiTitle = "IA Google Search & Lecture Vocale",
        pillarVoiceAiDesc = "Tarifs locaux actualisés via Google Search et synthèse vocale TTS.",
        navDiscover = "Explorer",
        navMap = "Carte",
        navConcierge = "Assistant",
        navBookings = "Réservations",
        navProfile = "Profil",
        searchPlaceholder = "Chercher plombier, zellige, électricien, ville...",
        allWorld = "Monde entier",
        allServices = "Tout",
        artisansOnly = "Artisans",
        homeServicesOnly = "Services maison",
        allCategories = "Tous métiers",
        verifiedProsBannerTitle = "Artisans et Pros Vérifiés dans le Monde",
        verifiedProsBannerSub = "Réservez un expert ou publiez votre savoir-faire",
        becomeProvider = "Publier offre",
        availableToday = "Dispo aujourd'hui",
        perHour = "/ h",
        yearsExp = "ans d'exp.",
        reviewsLabel = "avis",
        listenBio = "Écouter la bio",
        stopAudio = "Arrêter",
        generatingVoice = "Génération vocale...",
        bookService = "Réserver",
        viewDetails = "Voir détails",
        mapTitle = "Carte Mondiale des Services",
        nearMeGps = "Autour de moi",
        openInGoogleMaps = "Ouvrir dans Google Maps",
        mapsKeyNoticeTitle = "Explorateur Mondial Interactif",
        mapsKeyNoticeBody = "Ajoutez MAPS_API_KEY dans AI Studio Secrets pour activer les cartes vectorielles Google Maps.",
        mapsAiDiscoveryBtn = "Fournisseurs & Conseils Locaux (IA)",
        conciergeTitle = "Concierge IA TrustCraft",
        conciergeSubtitle = "Propulsé par Google Search pour des tarifs locaux et conseils à jour",
        conciergeInputHint = "Demandez le tarif moyen d'un plombier à Paris ou Casablanca...",
        conciergeSend = "Rechercher",
        searchGroundedBadge = "Source Google Search",
        mapsGroundedBadge = "Source Google Maps",
        verifiedSources = "Sources Vérifiées",
        listenAnswer = "Écouter la réponse",
        quickPrompts = listOf(
            "Quel est le tarif horaire moyen d'un électricien à Paris, Madrid et Casablanca ?",
            "Entretien de climatisation avant l'été et tarifs moyens à Dubaï ou Lyon",
            "Comment vérifier la qualité d'un maître artisan en zellige traditionnel ?"
        ),
        bookingsTitle = "Réservations & Missions",
        myBookingsTab = "Mes réservations",
        providerJobsTab = "Missions reçues",
        emptyBookingsTitle = "Aucune réservation",
        emptyBookingsDesc = "Explorez les artisans certifiés partout dans le monde et réservez votre premier service.",
        confirmBooking = "Confirmer",
        markCompleted = "Terminer",
        cancelBooking = "Annuler",
        listenSummary = "Écouter résumé",
        profileTitle = "Profil & Paramètres",
        languageSection = "Langue de l'application",
        savedFavorites = "Artisans favoris",
        privateContactSection = "Coordonnées privées (protégées)",
        phoneLabel = "Téléphone",
        addressLabel = "Adresse d'intervention",
        saveContact = "Enregistrer",
        crossPlatformNoteTitle = "Architecture Multiplateforme (Android & iOS)",
        crossPlatformNoteBody = "Cette application native Android (Kotlin & Compose) utilise un backend cloud Firebase Firestore prêt à être partagé avec une application iPhone (SwiftUI).",
        signOut = "Se déconnecter",
        publishListingTitle = "Publier sur la Place de Marché",
        businessNameLabel = "Nom de l'atelier ou entreprise",
        cityLabel = "Ville",
        countryLabel = "Pays",
        continentLabel = "Continent",
        tradeCategoryLabel = "Catégorie",
        serviceKindLabel = "Type de service",
        bioLabel = "Description et savoir-faire",
        hourlyRateLabel = "Tarif horaire",
        currencyLabel = "Devise",
        experienceYearsLabel = "Années d'expérience",
        publishButton = "Publier l'annonce",
        cancel = "Annuler",
        bookingDialogTitle = "Réserver un Service",
        serviceNeededLabel = "Service souhaité",
        preferredDateLabel = "Date et heure (ex: 2026-11-22 10:00)",
        notesLabel = "Précisions",
        submitBooking = "Envoyer la demande",
        writeReviewTitle = "Laisser un avis",
        reviewCommentHint = "Partagez votre expérience avec cet artisan...",
        submitReview = "Publier l'avis",
        languagesSpoken = "Langues parlées",
        verifiedReviews = "Avis clients",
        noReviewsYet = "Aucun avis rédigé pour l'instant. Soyez le premier !",
        back = "Retour",
        retry = "Réessayer",
        locationPermissionDenied = "Localisation refusée — explorez les villes directement sur la carte.",
        loadingText = "Chargement..."
    )

    private val esStrings = AppStrings(
        appName = "TrustCraft",
        tagline = "Mercado global de artesanos y servicios del hogar de confianza",
        signInTitle = "Artesanos y profesionales de confianza en todo el mundo",
        signInSubtitle = "Descubre maestros artesanos y expertos del hogar en cualquier continente con Google Maps, precios en tiempo real y voz en 4 idiomas.",
        signInWithGoogle = "Iniciar sesión con Google",
        pillarGlobalTitle = "Cobertura Global Sin Fronteras",
        pillarGlobalDesc = "Madrid, Casablanca, París, Ciudad de México, Nueva York, Dubái y más.",
        pillarVerifiedTitle = "Confianza y Reseñas Verificadas",
        pillarVerifiedDesc = "Consulta insignias de verificación, experiencia y valoraciones reales.",
        pillarVoiceAiTitle = "IA con Búsqueda Google y Voz",
        pillarVoiceAiDesc = "Tarifas locales actualizadas con Google Search y lectura en voz alta.",
        navDiscover = "Explorar",
        navMap = "Mapa",
        navConcierge = "Asistente",
        navBookings = "Reservas",
        navProfile = "Perfil",
        searchPlaceholder = "Buscar fontanero, azulejo, electricista, ciudad...",
        allWorld = "Todo el mundo",
        allServices = "Todos",
        artisansOnly = "Artesanos",
        homeServicesOnly = "Servicios hogar",
        allCategories = "Todas las áreas",
        verifiedProsBannerTitle = "Artesanos y Expertos Verificados",
        verifiedProsBannerSub = "Reserva al instante o publica tu oficio globalmente",
        becomeProvider = "Publicar oficio",
        availableToday = "Disponible hoy",
        perHour = "/ h",
        yearsExp = "años exp.",
        reviewsLabel = "reseñas",
        listenBio = "Escuchar bio",
        stopAudio = "Detener audio",
        generatingVoice = "Generando voz...",
        bookService = "Reservar",
        viewDetails = "Ver detalles",
        mapTitle = "Mapa Global de Servicios",
        nearMeGps = "Cerca de mí",
        openInGoogleMaps = "Abrir en Google Maps",
        mapsKeyNoticeTitle = "Explorador Mundial Interactivo",
        mapsKeyNoticeBody = "Agrega MAPS_API_KEY en AI Studio Secrets para activar los mapas vectoriales en vivo de Google Maps.",
        mapsAiDiscoveryBtn = "Proveedores y Guías Cercanas (IA)",
        conciergeTitle = "Asistente IA TrustCraft",
        conciergeSubtitle = "Con Google Search Grounding para tarifas locales y consejos actualizados",
        conciergeInputHint = "Pregunta por tarifas de fontanería en Madrid o Ciudad de México...",
        conciergeSend = "Consultar",
        searchGroundedBadge = "Verificado con Google Search",
        mapsGroundedBadge = "Verificado con Google Maps",
        verifiedSources = "Fuentes Verificadas",
        listenAnswer = "Escuchar respuesta",
        quickPrompts = listOf(
            "¿Cuáles son las tarifas por hora de electricistas en Madrid, Ciudad de México y Casablanca?",
            "Mantenimiento de aire acondicionado antes del verano y precios estimados",
            "Consejos para contratar restauración artesanal de azulejos y madera"
        ),
        bookingsTitle = "Reservas y Trabajos",
        myBookingsTab = "Mis reservas",
        providerJobsTab = "Trabajos recibidos",
        emptyBookingsTitle = "Aún no hay reservas",
        emptyBookingsDesc = "Explora artesanos verificados en cualquier continente y reserva tu primer servicio.",
        confirmBooking = "Confirmar",
        markCompleted = "Completar",
        cancelBooking = "Cancelar",
        listenSummary = "Escuchar resumen",
        profileTitle = "Perfil y Ajustes",
        languageSection = "Idioma de la aplicación",
        savedFavorites = "Artesanos favoritos",
        privateContactSection = "Contacto privado (Solo propietario)",
        phoneLabel = "Teléfono",
        addressLabel = "Dirección del servicio",
        saveContact = "Guardar datos privados",
        crossPlatformNoteTitle = "Listo para Multiplataforma (Android e iOS)",
        crossPlatformNoteBody = "Esta app nativa Android (Kotlin y Compose) comparte un esquema cloud en Firebase Firestore listo para conectarse con iPhone (SwiftUI).",
        signOut = "Cerrar sesión",
        publishListingTitle = "Publicar en el Mercado Global",
        businessNameLabel = "Nombre del taller o servicio",
        cityLabel = "Ciudad",
        countryLabel = "País",
        continentLabel = "Continente",
        tradeCategoryLabel = "Categoría",
        serviceKindLabel = "Tipo de servicio",
        bioLabel = "Descripción de experiencia",
        hourlyRateLabel = "Tarifa por hora",
        currencyLabel = "Moneda",
        experienceYearsLabel = "Años de experiencia",
        publishButton = "Publicar anuncio",
        cancel = "Cancelar",
        bookingDialogTitle = "Reservar Servicio",
        serviceNeededLabel = "Servicio requerido",
        preferredDateLabel = "Fecha y hora (ej. 2026-11-22 10:00)",
        notesLabel = "Notas del trabajo",
        submitBooking = "Enviar solicitud",
        writeReviewTitle = "Escribir una reseña",
        reviewCommentHint = "Comparte tu experiencia con este profesional...",
        submitReview = "Publicar reseña",
        languagesSpoken = "Idiomas hablados",
        verifiedReviews = "Reseñas de clientes",
        noReviewsYet = "Aún no hay reseñas escritas. ¡Sé el primero!",
        back = "Volver",
        retry = "Reintentar",
        locationPermissionDenied = "Permiso de ubicación denegado — explora ciudades desde el mapa global.",
        loadingText = "Cargando..."
    )
}
