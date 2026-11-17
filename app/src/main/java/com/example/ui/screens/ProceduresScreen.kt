package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FolderOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.ProcedureCategory
import com.example.model.ProcedureGuide
import com.example.model.feeForDisplay
import com.example.ui.components.InfoBadge
import com.example.ui.components.MizanTopHeader
import com.example.ui.theme.SuccessGreen
import com.example.viewmodel.MizanUiState

@Composable
fun ProceduresScreen(
    uiState: MizanUiState,
    onToggleLanguage: () -> Unit,
    onSearchChange: (String) -> Unit,
    onSelectCategory: (ProcedureCategory?) -> Unit,
    onOpenProcedureDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = uiState.language
    val isAr = lang == AppLanguage.AR

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 760.dp),
            contentPadding = PaddingValues(bottom = 28.dp)
        ) {
            item {
                MizanTopHeader(
                    title = if (isAr) "دليل الوثائق والمساطر" else "Démarches & Dossiers",
                    subtitle = if (isAr) "دليل إرشادي مع روابط المصادر الرسمية" else "Guides indicatifs et sources officielles",
                    language = lang,
                    onToggleLanguage = onToggleLanguage
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.procedureSearchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .testTag("procedures_search_input"),
                    placeholder = {
                        Text(
                            text = if (isAr) "ابحث عن وثيقة أو مسطرة..." else "Chercher une démarche...",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    },
                    trailingIcon = {
                        if (uiState.procedureSearchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
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
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                val categories = listOf(
                    null to (if (isAr) "الكل" else "Tout"),
                    ProcedureCategory.IDENTITY to (if (isAr) "الهوية" else "Identité"),
                    ProcedureCategory.WORK_BUSINESS to (if (isAr) "العمل والمقاول" else "Travail & Pro"),
                    ProcedureCategory.FAMILY_CIVIL to (if (isAr) "الأسرة والحالة المدنية" else "État civil"),
                    ProcedureCategory.TRANSPORT_HEALTH to (if (isAr) "السيارة والصحة" else "Auto & Santé")
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { (cat, label) ->
                        FilterChip(
                            selected = uiState.selectedProcedureCategory == cat,
                            onClick = { onSelectCategory(cat) },
                            label = { Text(label) },
                            modifier = Modifier.testTag("proc_cat_${cat?.name ?: "ALL"}")
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (uiState.filteredProcedures.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FolderOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isAr) "لم يتم العثور على نتائج مطابقة" else "Aucune démarche trouvée",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            } else {
                items(uiState.filteredProcedures, key = { it.id }) { guide ->
                    ProcedureListCard(
                        guide = guide,
                        lang = lang,
                        checkedDocIds = uiState.checkedDocuments,
                        onClick = { onOpenProcedureDetail(guide.id) },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProcedureListCard(
    guide: ProcedureGuide,
    lang: AppLanguage,
    checkedDocIds: Set<String>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAr = lang == AppLanguage.AR
    val total = guide.documents.size.coerceAtLeast(1)
    val checked = guide.documents.count { checkedDocIds.contains(it.id) }
    val complete = checked == total

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("procedure_card_${guide.id}"),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = guide.title.get(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = guide.subtitle.get(lang),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoBadge(
                    text = guide.feeForDisplay(lang),
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f),
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (complete) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = if (isAr) "$checked / $total وثائق جاهزة" else "$checked / $total pièces prêtes",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (complete) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { checked.toFloat() / total.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50)),
                color = if (complete) SuccessGreen else MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isAr) "مرجع: ${guide.sourceName.get(lang)} • تأكد من الشروط الحالية قبل الإيداع"
                    else "Référence : ${guide.sourceName.get(lang)} • confirmez les conditions actuelles avant dépôt",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
