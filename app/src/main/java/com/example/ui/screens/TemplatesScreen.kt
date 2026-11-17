package com.example.ui.screens

import android.content.Context
import android.content.Intent
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.TemplateCategory
import com.example.ui.components.MizanTopHeader
import com.example.ui.components.SectionTitleRow
import com.example.viewmodel.MizanUiState
import com.example.viewmodel.MizanViewModel

@Composable
fun TemplatesScreen(
    uiState: MizanUiState,
    viewModel: MizanViewModel,
    modifier: Modifier = Modifier
) {
    val lang = uiState.language
    val isAr = lang == AppLanguage.AR
    val draft = uiState.templateDraft
    val activeTemplate = uiState.activeTemplate
    val renderedText = uiState.renderedLetterText
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    var copiedFeedback by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 760.dp)
                .testTag("templates_lazy_list"),
            contentPadding = PaddingValues(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                MizanTopHeader(
                    title = if (isAr) "الطلبات والالتزامات الجاهزة" else "Modèles de Lettres & Actes",
                    subtitle = if (isAr) "املأ معطياتك وانسخ أو شارك النص فوراً" else "Remplissez vos infos, copiez ou partagez",
                    language = lang,
                    onToggleLanguage = viewModel::toggleLanguage
                )
            }

            // Category Filter Chips
            item {
                val categories = listOf(
                    null to (if (isAr) "الكل" else "Tout"),
                    TemplateCategory.WORK_STUDY to (if (isAr) "العمل والتدريب" else "Emploi & Stage"),
                    TemplateCategory.ATTESTATION to (if (isAr) "تصاريح والتزامات" else "Attestations"),
                    TemplateCategory.ADMIN_REQUEST to (if (isAr) "طلبات إدارية" else "Administratif"),
                    TemplateCategory.COMPLAINT to (if (isAr) "شكايات" else "Réclamations")
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { (cat, label) ->
                        FilterChip(
                            selected = draft.selectedCategory == cat,
                            onClick = { viewModel.selectTemplateCategory(cat) },
                            label = { Text(label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.testTag("tmpl_cat_${cat?.name ?: "ALL"}")
                        )
                    }
                }
            }

            // Template Selector Row
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.filteredTemplates, key = { it.id }) { tmpl ->
                        val selected = tmpl.id == activeTemplate?.id
                        Surface(
                            modifier = Modifier
                                .width(220.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    copiedFeedback = false
                                    viewModel.selectTemplate(tmpl.id)
                                }
                                .testTag("tmpl_card_${tmpl.id}"),
                            color = if (selected) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerLow
                            },
                            tonalElevation = if (selected) 4.dp else 1.dp
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = tmpl.title.get(lang),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) {
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    },
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = tmpl.summary.get(lang),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (selected) {
                                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // Fill-in Form Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SectionTitleRow(
                                title = if (isAr) "تعبئة المعطيات" else "Vos informations",
                                icon = Icons.Default.EditNote,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = {
                                    copiedFeedback = false
                                    viewModel.clearDraftFields()
                                },
                                modifier = Modifier.testTag("tmpl_clear_fields")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ClearAll,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isAr) "مسح" else "Vider")
                            }
                        }

                        OutlinedTextField(
                            value = draft.fullName,
                            onValueChange = viewModel::updateDraftFullName,
                            label = { Text(if (isAr) "الاسم الكامل" else "Nom et Prénom") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("tmpl_input_name"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = draft.cin,
                                onValueChange = viewModel::updateDraftCin,
                                label = { Text(if (isAr) "رقم البطاقة الوطنية" else "N° CNIE") },
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("tmpl_input_cin"),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = draft.city,
                                onValueChange = viewModel::updateDraftCity,
                                label = { Text(if (isAr) "المدينة" else "Ville") },
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("tmpl_input_city"),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        OutlinedTextField(
                            value = draft.phone,
                            onValueChange = viewModel::updateDraftPhone,
                            label = { Text(if (isAr) "رقم الهاتف" else "Téléphone") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("tmpl_input_phone"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        if (activeTemplate?.needsTargetEntity == true) {
                            OutlinedTextField(
                                value = draft.targetEntity,
                                onValueChange = viewModel::updateDraftTargetEntity,
                                label = {
                                    Text(
                                        if (isAr) "الجهة المرسل إليها / الشخص المعني"
                                        else "Destinataire / Organisme"
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("tmpl_input_target"),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        OutlinedTextField(
                            value = draft.details,
                            onValueChange = viewModel::updateDraftDetails,
                            label = {
                                Text(
                                    if (isAr) "تفاصيل الطلب / المنصب / الغرض"
                                    else "Détails / Poste / Objet précis"
                                )
                            },
                            minLines = 2,
                            maxLines = 3,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("tmpl_input_details"),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            // Live Letter Preview Card + Copy & Share Actions
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .testTag("tmpl_preview_card"),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SectionTitleRow(
                            title = if (isAr) "معاينة النص الجاهز" else "Aperçu du document prêt",
                            icon = Icons.Default.Description
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.background,
                            tonalElevation = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = renderedText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .padding(14.dp)
                                    .testTag("tmpl_preview_text")
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(renderedText))
                                    copiedFeedback = true
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("tmpl_copy_button")
                            ) {
                                Icon(
                                    imageVector = if (copiedFeedback) Icons.Default.Check else Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (copiedFeedback) {
                                        if (isAr) "تم النسخ!" else "Copié !"
                                    } else {
                                        if (isAr) "نسخ النص" else "Copier"
                                    }
                                )
                            }

                            OutlinedButton(
                                onClick = { shareLetterText(context, activeTemplate?.title?.get(lang).orEmpty(), renderedText) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("tmpl_share_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isAr) "مشاركة" else "Partager")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun shareLetterText(context: Context, subject: String, body: String) {
    runCatching {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        context.startActivity(Intent.createChooser(intent, subject))
    }
}
// دالة لتوليد ومشاركة الطلب كملف PDF
private fun shareRequestAsPdf(context: Context, title: String, content: String, fullName: String, idCard: String, phone: String) {
    val pdfDocument = android.graphics.pdf.PdfDocument()
    val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    val paint = android.graphics.paint.Paint().apply {
        color = android.graphics.Color.BLACK
        textSize = 14f
    }

    var y = 50f
    canvas.drawText("المملكة المغربية", 50f, y, paint)
    y += 40f
    canvas.drawText("الموضوع: $title", 50f, y, paint)
    y += 30f
    canvas.drawText("الاسم الكامل: $fullName", 50f, y, paint)
    y += 25f
    canvas.drawText("رقم البطاقة الوطنية: $idCard", 50f, y, paint)
    y += 25f
    canvas.drawText("رقم الهاتف: $phone", 50f, y, paint)
    y += 40f
    
    content.lines().forEach { line ->
        if (y < 800f) {
            canvas.drawText(line, 50f, y, paint)
            y += 20f
        }
    }

    pdfDocument.finishPage(page)

    val file = java.io.File(context.cacheDir, "demande_mizan.pdf")
    try {
        val fos = java.io.FileOutputStream(file)
        pdfDocument.writeTo(fos)
        fos.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    pdfDocument.close()

    val uri = androidx.core.content.FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "مشاركة الطلب عبر"))
}
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "مشاركة الطلب عبر"))
}
