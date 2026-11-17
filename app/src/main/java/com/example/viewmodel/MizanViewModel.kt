package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.MoroccanCivicRepository
import com.example.data.UserPreferencesRepository
import com.example.model.AppLanguage
import com.example.model.BilingualText
import com.example.model.CivicRightTip
import com.example.model.EmergencyService
import com.example.model.LetterTemplate
import com.example.model.MoroccanCityInfo
import com.example.model.OfficialSource
import com.example.model.ProcedureCategory
import com.example.model.ProcedureGuide
import com.example.model.TemplateCategory
import com.example.model.ThemePreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun currentFormattedDate(): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.US).format(Date())
}

data class TemplateDraftState(
    val selectedCategory: TemplateCategory? = null,
    val selectedTemplateId: String = "demande_emploi",
    val fullName: String = "",
    val cin: String = "",
    val city: String = "",
    val phone: String = "",
    val targetEntity: String = "",
    val details: String = "",
    val dateText: String = currentFormattedDate()
)

data class MizanUiState(
    val language: AppLanguage = AppLanguage.AR,
    val themePreference: ThemePreference = ThemePreference.SYSTEM,
    val checkedDocuments: Set<String> = emptySet(),
    val homeSearchQuery: String = "",
    val procedureSearchQuery: String = "",
    val selectedProcedureCategory: ProcedureCategory? = null,
    val citySearchQuery: String = "",
    val templateDraft: TemplateDraftState = TemplateDraftState(),
    val allProcedures: List<ProcedureGuide> = emptyList(),
    val letterTemplates: List<LetterTemplate> = emptyList(),
    val moroccanCities: List<MoroccanCityInfo> = emptyList(),
    val emergencyServices: List<EmergencyService> = emptyList(),
    val civicTips: List<CivicRightTip> = emptyList(),
    val officialSources: List<OfficialSource> = emptyList(),
    val lastUpdated: BilingualText = BilingualText("", "")
) {
    val filteredProcedures: List<ProcedureGuide>
        get() = allProcedures.filter { guide ->
            val matchesCat = selectedProcedureCategory == null || guide.category == selectedProcedureCategory
            val q = procedureSearchQuery.trim()
            val matchesQuery = q.isEmpty() ||
                guide.title.ar.contains(q, ignoreCase = true) ||
                guide.title.fr.contains(q, ignoreCase = true) ||
                guide.subtitle.ar.contains(q, ignoreCase = true) ||
                guide.subtitle.fr.contains(q, ignoreCase = true) ||
                guide.documents.any {
                    it.label.ar.contains(q, ignoreCase = true) || it.label.fr.contains(q, ignoreCase = true)
                }
            matchesCat && matchesQuery
        }

    val filteredTemplates: List<LetterTemplate>
        get() = letterTemplates.filter {
            templateDraft.selectedCategory == null || it.category == templateDraft.selectedCategory
        }

    val activeTemplate: LetterTemplate?
        get() = letterTemplates.find { it.id == templateDraft.selectedTemplateId }
            ?: letterTemplates.firstOrNull()

    val renderedLetterText: String
        get() {
            val tmpl = activeTemplate ?: return ""
            val isAr = language == AppLanguage.AR
            val raw = if (isAr) tmpl.bodyTemplateAr else tmpl.bodyTemplateFr

            val nameVal = templateDraft.fullName.ifBlank { if (isAr) "[الاسم الكامل]" else "[Nom & Prénom]" }
            val cinVal = templateDraft.cin.ifBlank { if (isAr) "[رقم البطاقة الوطنية]" else "[N° CNIE]" }
            val cityVal = templateDraft.city.ifBlank { if (isAr) "[المدينة]" else "[Ville]" }
            val phoneVal = templateDraft.phone.ifBlank { if (isAr) "[رقم الهاتف]" else "[Téléphone]" }
            val targetVal = templateDraft.targetEntity.ifBlank { if (isAr) "[الجهة أو الشخص المعني]" else "[Destinataire / Organisme]" }
            val detailsVal = templateDraft.details.ifBlank { if (isAr) "[تفاصيل الطلب أو الموضوع]" else "[Précisions / Objet]" }
            val dateVal = templateDraft.dateText.ifBlank { currentFormattedDate() }

            return raw
                .replace("{FULL_NAME}", nameVal)
                .replace("{CIN}", cinVal)
                .replace("{CITY}", cityVal)
                .replace("{PHONE}", phoneVal)
                .replace("{TARGET_ENTITY}", targetVal)
                .replace("{DETAILS}", detailsVal)
                .replace("{DATE}", dateVal)
        }

    val filteredCities: List<MoroccanCityInfo>
        get() {
            val q = citySearchQuery.trim()
            if (q.isEmpty()) return moroccanCities
            return moroccanCities.filter { city ->
                city.name.ar.contains(q, ignoreCase = true) ||
                    city.name.fr.contains(q, ignoreCase = true) ||
                    city.region.ar.contains(q, ignoreCase = true) ||
                    city.region.fr.contains(q, ignoreCase = true) ||
                    city.plateCode.contains(q, ignoreCase = true) ||
                    city.postalCode.contains(q, ignoreCase = true)
            }
        }
}

class MizanViewModel(
    private val prefsRepo: UserPreferencesRepository,
    private val civicRepo: MoroccanCivicRepository,
    private val dateProvider: () -> String = { currentFormattedDate() }
) : ViewModel() {

    private val templateDraftState = MutableStateFlow(TemplateDraftState(dateText = dateProvider()))
    private val homeQuery = MutableStateFlow("")
    private val procQuery = MutableStateFlow("")
    private val procCategory = MutableStateFlow<ProcedureCategory?>(null)
    private val cityQuery = MutableStateFlow("")

    val uiState: StateFlow<MizanUiState> = combine(
        prefsRepo.languageFlow,
        prefsRepo.themeFlow,
        prefsRepo.checkedDocumentsFlow,
        templateDraftState,
        combine(homeQuery, procQuery, procCategory, cityQuery) { h, p, c, cityQ ->
            FilterParams(h, p, c, cityQ)
        }
    ) { lang, theme, checkedDocs, draft, filters ->
        MizanUiState(
            language = lang,
            themePreference = theme,
            checkedDocuments = checkedDocs,
            homeSearchQuery = filters.homeQuery,
            procedureSearchQuery = filters.procQuery,
            selectedProcedureCategory = filters.procCat,
            citySearchQuery = filters.cityQuery,
            templateDraft = draft,
            allProcedures = civicRepo.procedures,
            letterTemplates = civicRepo.letterTemplates,
            moroccanCities = civicRepo.moroccanCities,
            emergencyServices = civicRepo.emergencyServices,
            civicTips = civicRepo.civicRightsTips,
            officialSources = civicRepo.officialSources,
            lastUpdated = civicRepo.lastUpdated
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MizanUiState(
            templateDraft = TemplateDraftState(dateText = dateProvider()),
            allProcedures = civicRepo.procedures,
            letterTemplates = civicRepo.letterTemplates,
            moroccanCities = civicRepo.moroccanCities,
            emergencyServices = civicRepo.emergencyServices,
            civicTips = civicRepo.civicRightsTips,
            officialSources = civicRepo.officialSources,
            lastUpdated = civicRepo.lastUpdated
        )
    )

    private data class FilterParams(
        val homeQuery: String,
        val procQuery: String,
        val procCat: ProcedureCategory?,
        val cityQuery: String
    )

    fun toggleLanguage() {
        val next = if (uiState.value.language == AppLanguage.AR) AppLanguage.FR else AppLanguage.AR
        viewModelScope.launch { prefsRepo.setLanguage(next) }
    }

    fun setLanguage(lang: AppLanguage) {
        viewModelScope.launch { prefsRepo.setLanguage(lang) }
    }

    fun setThemePreference(theme: ThemePreference) {
        viewModelScope.launch { prefsRepo.setThemePreference(theme) }
    }

    fun toggleDocumentChecked(docId: String) {
        viewModelScope.launch { prefsRepo.toggleDocumentChecked(docId) }
    }

    fun clearAllChecklists() {
        viewModelScope.launch { prefsRepo.clearAllChecklists() }
    }

    fun updateHomeSearchQuery(query: String) {
        homeQuery.value = query
    }

    fun updateProcedureSearchQuery(query: String) {
        procQuery.value = query
    }

    fun selectProcedureCategory(category: ProcedureCategory?) {
        procCategory.value = category
    }

    fun updateCitySearchQuery(query: String) {
        cityQuery.value = query
    }

    fun selectTemplateCategory(category: TemplateCategory?) {
        templateDraftState.update { state ->
            val matching = civicRepo.letterTemplates.filter { category == null || it.category == category }
            val nextTemplateId = if (matching.any { it.id == state.selectedTemplateId }) {
                state.selectedTemplateId
            } else {
                matching.firstOrNull()?.id ?: state.selectedTemplateId
            }
            state.copy(selectedCategory = category, selectedTemplateId = nextTemplateId)
        }
    }

    fun selectTemplate(templateId: String) {
        templateDraftState.update { it.copy(selectedTemplateId = templateId) }
    }

    fun updateDraftFullName(value: String) {
        templateDraftState.update { it.copy(fullName = value) }
    }

    fun updateDraftCin(value: String) {
        templateDraftState.update { it.copy(cin = value) }
    }

    fun updateDraftCity(value: String) {
        templateDraftState.update { it.copy(city = value) }
    }

    fun updateDraftPhone(value: String) {
        templateDraftState.update { it.copy(phone = value) }
    }

    fun updateDraftTargetEntity(value: String) {
        templateDraftState.update { it.copy(targetEntity = value) }
    }

    fun updateDraftDetails(value: String) {
        templateDraftState.update { it.copy(details = value) }
    }

    fun clearDraftFields() {
        templateDraftState.update {
            it.copy(
                fullName = "",
                cin = "",
                city = "",
                phone = "",
                targetEntity = "",
                details = "",
                dateText = dateProvider()
            )
        }
    }

    fun getProcedureById(id: String): ProcedureGuide? {
        return civicRepo.procedures.find { it.id == id }
    }

    class Factory(
        private val prefsRepo: UserPreferencesRepository,
        private val civicRepo: MoroccanCivicRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MizanViewModel(prefsRepo, civicRepo) as T
        }
    }
}
