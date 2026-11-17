package com.example.model

enum class AppLanguage {
    AR,
    FR
}

enum class ThemePreference {
    SYSTEM,
    LIGHT,
    DARK
}

data class BilingualText(
    val ar: String,
    val fr: String
) {
    fun get(lang: AppLanguage): String = if (lang == AppLanguage.AR) ar else fr
}

enum class ProcedureCategory {
    IDENTITY,
    WORK_BUSINESS,
    FAMILY_CIVIL,
    TRANSPORT_HEALTH
}

data class DocumentItem(
    val id: String,
    val label: BilingualText
)

data class ProcedureGuide(
    val id: String,
    val category: ProcedureCategory,
    val title: BilingualText,
    val subtitle: BilingualText,
    val feeLabel: BilingualText,
    val processingTime: BilingualText,
    val officeLocation: BilingualText,
    val onlinePortal: String?,
    val sourceName: BilingualText,
    val sourceUrl: String,
    val legalReference: BilingualText,
    val verifiedAt: BilingualText,
    val documents: List<DocumentItem>,
    val tips: List<BilingualText>,
    /** ISO-8601 date of an independently completed review; null means not yet verified. */
    val lastIndependentlyVerified: String? = null
)

enum class TemplateCategory {
    ADMIN_REQUEST,
    ATTESTATION,
    WORK_STUDY,
    COMPLAINT
}

data class LetterTemplate(
    val id: String,
    val category: TemplateCategory,
    val title: BilingualText,
    val summary: BilingualText,
    val needsTargetEntity: Boolean,
    val bodyTemplateAr: String,
    val bodyTemplateFr: String
)

data class MoroccanCityInfo(
    val id: String,
    val name: BilingualText,
    val region: BilingualText,
    val plateCode: String,
    val postalCode: String
)

enum class EmergencyCategory {
    SECURITY_RESCUE,
    CONSUMER_RIGHTS,
    PUBLIC_UTILITY
}

data class EmergencyService(
    val id: String,
    val number: String,
    val name: BilingualText,
    val description: BilingualText,
    val category: EmergencyCategory,
    val sourceName: BilingualText,
    val sourceUrl: String,
    val verifiedAt: BilingualText,
    val lastIndependentlyVerified: String? = null
)

data class CivicRightTip(
    val id: String,
    val category: BilingualText,
    val title: BilingualText,
    val summary: BilingualText,
    val sourceName: BilingualText,
    val sourceUrl: String,
    val verifiedAt: BilingualText,
    val lastIndependentlyVerified: String? = null
)

data class OfficialSource(
    val id: String,
    val title: BilingualText,
    val domain: String,
    val url: String,
    val scope: BilingualText,
    /** ISO-8601 date of an independently completed source review; null means not yet verified. */
    val lastIndependentlyVerified: String? = null
)

/** Never present draft fees as confirmed before an individual legal review. */
fun ProcedureGuide.feeForDisplay(language: AppLanguage): String =
    if (lastIndependentlyVerified == null) {
        if (language == AppLanguage.AR) "غير متحقق مستقلاً — راجع الجهة الرسمية" else "Non vérifié indépendamment — confirmer auprès du service"
    } else feeLabel.get(language)

fun ProcedureGuide.processingTimeForDisplay(language: AppLanguage): String =
    if (lastIndependentlyVerified == null) {
        if (language == AppLanguage.AR) "غير متحقق مستقلاً — راجع الجهة الرسمية" else "Non vérifié indépendamment — confirmer auprès du service"
    } else processingTime.get(language)
