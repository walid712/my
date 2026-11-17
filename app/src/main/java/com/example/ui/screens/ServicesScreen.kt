package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.ThemePreference
import com.example.ui.components.InfoBadge
import com.example.ui.components.MizanTopHeader
import com.example.ui.components.SectionTitleRow
import com.example.ui.components.dialPhoneNumber
import com.example.ui.components.openOfficialUrl
import com.example.viewmodel.MizanUiState
import com.example.viewmodel.MizanViewModel

@Composable
fun ServicesScreen(
    uiState: MizanUiState,
    viewModel: MizanViewModel,
    modifier: Modifier = Modifier
) {
    val lang = uiState.language
    val isAr = lang == AppLanguage.AR
    val context = LocalContext.current
    var showAllCities by remember { mutableStateOf(false) }

    val displayedCities = remember(uiState.filteredCities, showAllCities, uiState.citySearchQuery) {
        if (uiState.citySearchQuery.isNotBlank() || showAllCities) {
            uiState.filteredCities
        } else {
            uiState.filteredCities.take(8)
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 760.dp)
                .testTag("services_lazy_list"),
            contentPadding = PaddingValues(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                MizanTopHeader(
                    title = if (isAr) "المدن، الطوارئ والمصادر" else "Villes, Urgences & Sources",
                    subtitle = if (isAr) "ترقيم السيارات، الرموز البريدية، والأرقام الرسمية"
                    else "Matricules, codes postaux, numéros utiles & réglages",
                    language = lang,
                    onToggleLanguage = viewModel::toggleLanguage
                )
            }

            // Moroccan Cities, Car Plates & Postal Codes Directory
            item {
                SectionTitleRow(
                    title = if (isAr) "دليل ترقيم السيارات والرمز البريدي" else "Matricules Auto & Codes Postaux",
                    icon = Icons.Default.DirectionsCar,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { openOfficialUrl(context, "https://www.codepostal.ma") }
                        .padding(vertical = 4.dp)
                        .testTag("cities_directory_source"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAr) "روابط مرجعية: بريد المغرب وNARSA — تحقق من الرمز لدى الجهة"
                        else "Références : Barid Al-Maghrib & NARSA — confirmez auprès du service",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = uiState.citySearchQuery,
                    onValueChange = viewModel::updateCitySearchQuery,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .testTag("city_search_input"),
                    placeholder = {
                        Text(
                            text = if (isAr) "ابحث بالمدينة أو رقم السيارة (مثلاً: 26، فاس، 80000)..."
                            else "Ville, n° plaque (ex: 26, Fès, 80000)...",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    },
                    trailingIcon = {
                        if (uiState.citySearchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.updateCitySearchQuery("") }) {
                                Icon(Icons.Default.Close, contentDescription = if (isAr) "مسح" else "Effacer")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    )
                )
            }

            items(displayedCities, key = { it.id }) { city ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .testTag("city_item_${city.id}"),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = city.plateCode,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = city.name.get(lang),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = city.region.get(lang),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        InfoBadge(
                            text = if (isAr) "بريد: ${city.postalCode}" else "CP ${city.postalCode}",
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f),
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            if (uiState.citySearchQuery.isBlank() && uiState.filteredCities.size > 8) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(
                            onClick = { showAllCities = !showAllCities },
                            modifier = Modifier.testTag("toggle_all_cities_button")
                        ) {
                            Text(
                                text = if (showAllCities) {
                                    if (isAr) "إظهار أقل" else "Afficher moins"
                                } else {
                                    if (isAr) "عرض جميع المدن (${uiState.filteredCities.size})"
                                    else "Voir toutes les villes (${uiState.filteredCities.size})"
                                }
                            )
                        }
                    }
                }
            }

            // Emergency & Public Shortcodes
            item {
                Spacer(modifier = Modifier.height(6.dp))
                SectionTitleRow(
                    title = if (isAr) "أرقام الطوارئ والخدمات العمومية" else "Numéros d'urgence & services",
                    icon = Icons.Default.PhoneInTalk,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            items(uiState.emergencyServices, key = { it.id }) { service ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .minimumInteractiveComponentSize()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { dialPhoneNumber(context, service.number) }
                        .testTag("emergency_item_${service.id}"),
                    color = MaterialTheme.colorScheme.surfaceContainerLow
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = service.number,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = service.name.get(lang),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = service.description.get(lang),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isAr) "المصدر: ${service.sourceName.get(lang)} • ${service.verifiedAt.get(lang)} • آخر تحقق مستقل: ${service.lastIndependentlyVerified ?: "لم يتم بعد"}"
                                else "Source : ${service.sourceName.get(lang)} • ${service.verifiedAt.get(lang)} • Dernière vérification indépendante : ${service.lastIndependentlyVerified ?: "non effectuée"}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = if (isAr) "اتصال بالرقم ${service.number}" else "Composer ${service.number}",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Consumer & Civic Rights
            item {
                Spacer(modifier = Modifier.height(6.dp))
                SectionTitleRow(
                    title = if (isAr) "حقوق المواطن والمستهلك والأجير" else "Vos droits essentiels au Maroc",
                    icon = Icons.Default.Gavel,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            items(uiState.civicTips, key = { it.id }) { tip ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = tip.category.get(lang),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tip.title.get(lang),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tip.summary.get(lang),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { openOfficialUrl(context, tip.sourceUrl) }
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAr) "المصدر: ${tip.sourceName.get(lang)} • ${tip.verifiedAt.get(lang)} • آخر تحقق مستقل: ${tip.lastIndependentlyVerified ?: "لم يتم بعد"}"
                                else "Source : ${tip.sourceName.get(lang)} • ${tip.verifiedAt.get(lang)} • Dernière vérification indépendante : ${tip.lastIndependentlyVerified ?: "non effectuée"}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }

            // Preferences & Theme
            item {
                Spacer(modifier = Modifier.height(6.dp))
                SectionTitleRow(
                    title = if (isAr) "تفضيلات التطبيق" else "Préférences",
                    icon = Icons.Default.Palette,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = if (isAr) "اللغة (Langue)" else "Langue de l'interface",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = uiState.language == AppLanguage.AR,
                                onClick = { viewModel.setLanguage(AppLanguage.AR) },
                                label = { Text("العربية") },
                                modifier = Modifier.testTag("settings_lang_ar")
                            )
                            FilterChip(
                                selected = uiState.language == AppLanguage.FR,
                                onClick = { viewModel.setLanguage(AppLanguage.FR) },
                                label = { Text("Français") },
                                modifier = Modifier.testTag("settings_lang_fr")
                            )
                        }

                        Text(
                            text = if (isAr) "المظهر (الثيم)" else "Thème visuel",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = uiState.themePreference == ThemePreference.SYSTEM,
                                onClick = { viewModel.setThemePreference(ThemePreference.SYSTEM) },
                                label = { Text(if (isAr) "تلقائي" else "Système") }
                            )
                            FilterChip(
                                selected = uiState.themePreference == ThemePreference.LIGHT,
                                onClick = { viewModel.setThemePreference(ThemePreference.LIGHT) },
                                label = { Text(if (isAr) "نهاري" else "Clair") }
                            )
                            FilterChip(
                                selected = uiState.themePreference == ThemePreference.DARK,
                                onClick = { viewModel.setThemePreference(ThemePreference.DARK) },
                                label = { Text(if (isAr) "ليلي" else "Sombre") }
                            )
                        }

                        if (uiState.checkedDocuments.isNotEmpty()) {
                            OutlinedButton(
                                onClick = viewModel::clearAllChecklists,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("clear_checklists_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteSweep,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isAr) "إعادة ضبط قوائم الوثائق (${uiState.checkedDocuments.size})"
                                    else "Réinitialiser les dossiers (${uiState.checkedDocuments.size})"
                                )
                            }
                        }
                    }
                }
            }

            // Official Sources & Last Updated Date
            item {
                Spacer(modifier = Modifier.height(6.dp))
                SectionTitleRow(
                    title = if (isAr) "المصادر الرسمية وآخر تحديث" else "Sources officielles & Mise à jour",
                    icon = Icons.Default.Public,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .testTag("last_updated_card"),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.65f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Update,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isAr) "تاريخ آخر تحديث للمعلومات" else "Dernière mise à jour",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                            )
                            Text(
                                text = uiState.lastUpdated.get(lang),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
            }

            items(uiState.officialSources, key = { it.id }) { source ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .minimumInteractiveComponentSize()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { openOfficialUrl(context, source.url) }
                        .testTag("official_source_${source.id}"),
                    color = MaterialTheme.colorScheme.surfaceContainerLow
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = source.title.get(lang),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = source.scope.get(lang),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = source.domain,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = if (isAr) "آخر تحقق مستقل: ${source.lastIndependentlyVerified ?: "لم يتم بعد"}" else "Dernière vérification indépendante : ${source.lastIndependentlyVerified ?: "non effectuée"}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = if (isAr) "فتح الموقع الرسمي ${source.domain}" else "Ouvrir ${source.domain}",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Google Play Anti-Impersonation Disclaimer Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .testTag("disclaimer_card"),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.VerifiedUser,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isAr) "إخلاء مسؤولية: ميزان المغرب تطبيق خدماتي مستقل غير تابع لأي جهة حكومية أو إدارة عمومية. النماذج وقوائم الوثائق استرشادية ولا تتضمن أي حسابات مالية أو ضريبية؛ يُرجى دائماً تأكيد المستجدات عبر البوابات الرسمية أعلاه."
                            else "Avertissement : Mizan Maroc est une application citoyenne indépendante, non affiliée à aucune administration publique. Les modèles et listes de pièces sont fournis à titre indicatif sans calcul fiscal ni financier ; vérifiez toujours les informations sur les portails officiels ci-dessus.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
