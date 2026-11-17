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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.ProcedureGuide
import com.example.model.feeForDisplay
import com.example.model.processingTimeForDisplay
import com.example.ui.components.SectionTitleRow
import com.example.ui.components.openOfficialUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcedureDetailScreen(
    guide: ProcedureGuide,
    language: AppLanguage,
    checkedDocuments: Set<String>,
    onToggleDocument: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAr = language == AppLanguage.AR
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = guide.title.get(language),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("procedure_detail_back")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = if (isAr) "رجوع" else "Retour"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 760.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Metadata Overview Card
                item {
                    Card(
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            DetailMetaItem(
                                icon = Icons.Default.Payments,
                                label = if (isAr) "الرسوم / التمبر — تحتاج تأكيداً" else "Frais / timbre — à confirmer",
                                value = guide.feeForDisplay(language)
                            )
                            DetailMetaItem(
                                icon = Icons.Default.AccessTime,
                                label = if (isAr) "المدة التقديرية — تحتاج تأكيداً" else "Délai indicatif — à confirmer",
                                value = guide.processingTimeForDisplay(language)
                            )
                            DetailMetaItem(
                                icon = Icons.Default.AccountBalance,
                                label = if (isAr) "مكان الإيداع" else "Lieu de dépôt",
                                value = guide.officeLocation.get(language)
                            )
                            if (guide.onlinePortal != null) {
                                DetailMetaItem(
                                    icon = Icons.Default.Language,
                                    label = if (isAr) "البوابة الرسمية" else "Portail officiel",
                                    value = guide.onlinePortal
                                )
                            }
                            DetailMetaItem(
                                icon = Icons.Default.Gavel,
                                label = if (isAr) "المرجع القانوني" else "Référence légale",
                                value = guide.legalReference.get(language)
                            )
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { openOfficialUrl(context, guide.sourceUrl) }
                                    .testTag("procedure_detail_source"),
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.55f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Public,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (isAr) "راجع المصدر الرسمي لتأكيد الرسوم والآجال والوثائق قبل الإيداع" else "Confirmez frais, délais et pièces sur la source officielle avant dépôt",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = guide.sourceName.get(language),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                        contentDescription = if (isAr) "فتح المصدر الرسمي" else "Ouvrir la source officielle",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Text(
                                text = if (isAr) "ملاحظة: دليل إرشادي. ${guide.verifiedAt.get(language)}\nتاريخ آخر تحقق مستقل: ${guide.lastIndependentlyVerified ?: "لم يتم بعد"}. لا تعتمد الرسوم والآجال والوثائق قبل تأكيدها لدى الجهة."
                                else "Note : guide indicatif. ${guide.verifiedAt.get(language)}\nDernière vérification indépendante : ${guide.lastIndependentlyVerified ?: "non effectuée"}. Confirmez frais, délais et pièces auprès du service.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                                modifier = Modifier.testTag("procedure_detail_verified_date")
                            )
                        }
                    }
                }

                // Interactive Checklist Header
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    SectionTitleRow(
                        title = if (isAr) "وثائق إرشادية غير متحقق منها (اضغط لتحديد ما جهّزته)" else "Pièces indicatives non vérifiées (cochez votre avancement)",
                        icon = Icons.Default.Checklist
                    )
                }

                // Interactive Checklist Items (Persisted in DataStore)
                items(guide.documents, key = { it.id }) { doc ->
                    val isChecked = checkedDocuments.contains(doc.id)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onToggleDocument(doc.id) }
                            .testTag("doc_checkbox_${doc.id}"),
                        color = if (isChecked) {
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                        } else {
                            MaterialTheme.colorScheme.surfaceContainerLow
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { onToggleDocument(doc.id) }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = doc.label.get(language),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                                ),
                                color = if (isChecked) {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }

                // Practical Tips
                if (guide.tips.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                        SectionTitleRow(
                            title = if (isAr) "ملاحظات إرشادية غير متحقق منها مستقلاً" else "Conseils indicatifs non vérifiés indépendamment",
                            icon = Icons.Default.Lightbulb
                        )
                    }
                    items(guide.tips) { tip ->
                        Card(
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.55f)
                            )
                        ) {
                            Text(
                                text = tip.get(language),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(18.dp)) }
            }
        }
    }
}

@Composable
private fun DetailMetaItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
