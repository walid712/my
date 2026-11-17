package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.OfflineBolt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.feeForDisplay
import com.example.ui.components.MizanTopHeader
import com.example.ui.components.SectionTitleRow
import com.example.ui.components.dialPhoneNumber
import com.example.viewmodel.MizanUiState

@Composable
fun HomeScreen(
    uiState: MizanUiState,
    onToggleLanguage: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onOpenTemplates: () -> Unit,
    onOpenProcedureDetail: (String) -> Unit,
    onOpenAllProcedures: () -> Unit,
    onOpenCitiesAndServices: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = uiState.language
    val context = LocalContext.current
    val isAr = lang == AppLanguage.AR

    val procMatches = remember(uiState.homeSearchQuery, uiState.allProcedures) {
        val q = uiState.homeSearchQuery.trim()
        if (q.isEmpty()) emptyList()
        else uiState.allProcedures.filter {
            it.title.ar.contains(q, true) || it.title.fr.contains(q, true) ||
                it.subtitle.ar.contains(q, true) || it.subtitle.fr.contains(q, true)
        }
    }

    val cityMatches = remember(uiState.homeSearchQuery, uiState.moroccanCities) {
        val q = uiState.homeSearchQuery.trim()
        if (q.isEmpty()) emptyList()
        else uiState.moroccanCities.filter {
            it.name.ar.contains(q, true) || it.name.fr.contains(q, true) ||
                it.plateCode == q || it.postalCode.contains(q, true)
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
                .testTag("home_lazy_list"),
            contentPadding = PaddingValues(bottom = 28.dp)
        ) {
            item {
                MizanTopHeader(
                    title = if (isAr) "ميزان المغرب" else "Mizan Maroc",
                    subtitle = if (isAr) "نماذج، وثائق، ورموز المدن دون أنترنت" else "Modèles, démarches & repères hors-ligne",
                    language = lang,
                    onToggleLanguage = onToggleLanguage
                )
            }

            // Hero Card with custom illustration
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(176.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_hero_morocco),
                            contentDescription = if (isAr) "صورة معمارية مغربية" else "Illustration architecturale marocaine",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.18f),
                                            Color(0xFF073A2F).copy(alpha = 0.88f)
                                        )
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                color = Color(0xFFF4B942).copy(alpha = 0.92f),
                                shape = RoundedCornerShape(50)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.OfflineBolt,
                                        contentDescription = null,
                                        tint = Color(0xFF281A00),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = if (isAr) "يعمل بدون أنترنت 100%" else "100% Hors-ligne",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color(0xFF281A00)
                                    )
                                }
                            }

                            Column {
                                Text(
                                    text = if (isAr) "دليلك العملي لكل معاملاتك اليومية" else "Vos démarches et modèles prêts en poche",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isAr) "نماذج طلبات جاهزة • دليل الوثائق • ترقيم السيارات والمدن • الطوارئ"
                                    else "Lettres prêtes • Pièces officielles • Matricules & Codes postaux • Urgences",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.88f)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Quick Search Bar
            item {
                OutlinedTextField(
                    value = uiState.homeSearchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .testTag("home_search_input"),
                    placeholder = {
                        Text(
                            text = if (isAr) "ابحث: البطاقة الوطنية، جواز السفر، رقم سيارة 26، مراكش..."
                            else "Chercher : CNIE, Passeport, 26, Marrakech...",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        if (uiState.homeSearchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = if (isAr) "مسح" else "Effacer"
                                )
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

                AnimatedVisibility(visible = procMatches.isNotEmpty() || cityMatches.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        procMatches.take(3).forEach { guide ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onOpenProcedureDetail(guide.id) },
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = guide.title.get(lang),
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = guide.feeForDisplay(lang),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        cityMatches.take(3).forEach { city ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onOpenCitiesAndServices() },
                                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.65f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = city.name.get(lang),
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = if (isAr) "ترقيم السيارة: ${city.plateCode} • الرمز البريدي: ${city.postalCode}"
                                            else "Matricule : ${city.plateCode} • Code postal : ${city.postalCode}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.DirectionsCar,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Core Tools Grid (2x2) — 100% Calculation-Free
            item {
                SectionTitleRow(
                    title = if (isAr) "الخدمات العملية للمواطن" else "Outils pratiques du quotidien",
                    icon = Icons.Default.EditNote,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickToolCard(
                            title = if (isAr) "نماذج الطلبات والالتزامات" else "Modèles de Lettres",
                            subtitle = if (isAr) "اكتب طلبك أو التزامك وشاركه جاهزاً" else "Générez, copiez et partagez vos actes",
                            icon = Icons.Default.EditNote,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            testTag = "home_card_templates",
                            onClick = onOpenTemplates,
                            modifier = Modifier.weight(1f)
                        )
                        QuickToolCard(
                            title = if (isAr) "دليل الوثائق والمساطر" else "Pièces & Démarches",
                            subtitle = if (isAr) "البطاقة، جواز السفر، السجل العدلي..." else "CNIE, Passeport, Casier, Carte grise...",
                            icon = Icons.Default.Description,
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            testTag = "home_card_procedures",
                            onClick = onOpenAllProcedures,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickToolCard(
                            title = if (isAr) "رموز السيارات والمدن" else "Matricules & Villes",
                            subtitle = if (isAr) "ترقيم اللوحات (1-87) والرمز البريدي" else "Codes plaques auto & codes postaux",
                            icon = Icons.Default.LocationCity,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            testTag = "home_card_cities",
                            onClick = onOpenCitiesAndServices,
                            modifier = Modifier.weight(1f)
                        )
                        QuickToolCard(
                            title = if (isAr) "الطوارئ والمصادر الرسمية" else "Urgences & Sources",
                            subtitle = if (isAr) "19، 15، 177، 112 والمواقع الرسمية" else "Numéros utiles & portails officiels",
                            icon = Icons.Default.PhoneInTalk,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            testTag = "home_card_services",
                            onClick = onOpenCitiesAndServices,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(22.dp))
            }

            // Document Checklists Banner
            item {
                SectionTitleRow(
                    title = if (isAr) "تتبع أوراق ملفاتك الإدارية" else "Suivi de vos dossiers",
                    icon = Icons.Default.Description,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.allProcedures.take(5), key = { it.id }) { guide ->
                        val checkedCount = guide.documents.count { uiState.checkedDocuments.contains(it.id) }
                        Card(
                            modifier = Modifier
                                .width(228.dp)
                                .clickable { onOpenProcedureDetail(guide.id) }
                                .testTag("home_proc_${guide.id}"),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = guide.title.get(lang),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = guide.feeForDisplay(lang),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isAr) "جاهزية الملف: $checkedCount / ${guide.documents.size}"
                                    else "Pièces prêtes : $checkedCount / ${guide.documents.size}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier
                                .width(150.dp)
                                .height(114.dp)
                                .clickable(onClick = onOpenAllProcedures)
                                .testTag("home_view_all_procedures"),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = if (isAr) "عرض جميع الوثائق" else "Voir tout",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(22.dp))
            }

            // Emergency Quick Dial Strip
            item {
                SectionTitleRow(
                    title = if (isAr) "أرقام الاستعجال وحماية المستهلك" else "Urgences & Consommateur",
                    icon = Icons.Default.Call,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val quickEmergency = uiState.emergencyServices.filter {
                        it.id in listOf("police", "protection_civile", "gendarmerie", "consumer", "poison")
                    }
                    items(quickEmergency, key = { it.id }) { service ->
                        Surface(
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { dialPhoneNumber(context, service.number) }
                                .testTag("home_dial_${service.id}"),
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                            tonalElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.14f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = service.number,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.secondary,
                                        maxLines = 1
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = service.name.get(lang).substringBefore(" ("),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(22.dp))
            }

            // Highlighted Citizen Right Card
            val featuredTip = uiState.civicTips.firstOrNull()
            if (featuredTip != null) {
                item {
                    SectionTitleRow(
                        title = if (isAr) "معلومة قانونية تهمك" else "Bon à savoir (Vos droits)",
                        icon = Icons.Default.Gavel,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clickable(onClick = onOpenCitiesAndServices),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = featuredTip.category.get(lang),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = featuredTip.title.get(lang),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = featuredTip.summary.get(lang),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isAr) "معلومة إرشادية غير متحقق منها مستقلاً • المصدر: ${featuredTip.sourceName.get(lang)} • آخر تحقق: ${featuredTip.lastIndependentlyVerified ?: "لم يتم بعد"}"
                                else "Information indicative non vérifiée indépendamment • Source : ${featuredTip.sourceName.get(lang)} • Dernière vérification : ${featuredTip.lastIndependentlyVerified ?: "non effectuée"}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickToolCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(contentColor.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor.copy(alpha = 0.85f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
