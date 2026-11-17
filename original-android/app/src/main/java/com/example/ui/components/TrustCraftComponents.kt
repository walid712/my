package com.example.ui.components

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Carpenter
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Hvac
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.core.AppLanguage
import com.example.core.AppStrings
import com.example.core.StringsCatalog
import com.example.data.model.Continent
import com.example.data.model.ServiceKind
import com.example.data.model.ServiceProvider
import com.example.data.model.TradeCategory
import com.example.ui.theme.BrassContainer
import com.example.ui.theme.OnBrassContainer
import com.example.ui.theme.RatingAmber
import com.example.ui.theme.VerifiedEmerald
import com.example.ui.viewmodel.TtsUiState

@Composable
fun LanguageSwitcherRow(
    selectedLanguage: AppLanguage,
    onSelectLanguage: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppLanguage.entries.forEach { lang ->
            val selected = lang == selectedLanguage
            Surface(
                shape = RoundedCornerShape(50),
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                border = if (selected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .clip(RoundedCornerShape(50))
                    .clickable { onSelectLanguage(lang) }
                    .testTag("lang_chip_${lang.code}")
            ) {
                Text(
                    text = "${lang.shortBadge} · ${lang.nativeLabel}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}

fun tradeCategoryIcon(categoryKey: String): ImageVector = when (categoryKey) {
    TradeCategory.PLUMBING.key -> Icons.Filled.Plumbing
    TradeCategory.ELECTRICAL.key -> Icons.Filled.ElectricalServices
    TradeCategory.CARPENTRY.key -> Icons.Filled.Carpenter
    TradeCategory.CLEANING.key -> Icons.Filled.CleaningServices
    TradeCategory.HVAC.key -> Icons.Filled.Hvac
    TradeCategory.APPLIANCE.key -> Icons.Filled.Kitchen
    TradeCategory.PAINTING.key -> Icons.Filled.FormatPaint
    TradeCategory.ZELLIGE.key -> Icons.Filled.GridView
    TradeCategory.METALWORK.key -> Icons.Filled.Construction
    TradeCategory.GARDENING.key -> Icons.Filled.Yard
    TradeCategory.MOVING.key -> Icons.Filled.LocalShipping
    TradeCategory.SMART_HOME.key -> Icons.Filled.Sensors
    else -> Icons.Filled.Handyman
}

@Composable
fun VerificationBadge(
    tierKey: String,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val label = StringsCatalog.verificationName(tierKey, language)
    val isMaster = tierKey == "master_artisan"
    val isUnverified = tierKey == "unverified"
    val bgColor = when {
        isMaster -> BrassContainer
        isUnverified -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.primaryContainer
    }
    val fgColor = when {
        isMaster -> OnBrassContainer
        isUnverified -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = bgColor,
        contentColor = fgColor,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = when {
                    isMaster -> Icons.Filled.AutoAwesome
                    isUnverified -> Icons.Filled.Info
                    else -> Icons.Filled.Verified
                },
                contentDescription = label,
                modifier = Modifier.size(14.dp),
                tint = if (isMaster) OnBrassContainer else if (isUnverified) fgColor else VerifiedEmerald
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TtsActionButton(
    tag: String,
    labelIdle: String,
    strings: AppStrings,
    ttsState: TtsUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isGenerating = ttsState is TtsUiState.Generating && ttsState.activeTag == tag
    val isPlaying = ttsState is TtsUiState.Playing && ttsState.activeTag == tag

    OutlinedButton(
        onClick = onClick,
        enabled = !isGenerating,
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
        modifier = modifier
            .minimumInteractiveComponentSize()
            .testTag("tts_btn_$tag")
    ) {
        if (isGenerating) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp
            )
            Spacer(Modifier.width(8.dp))
            Text(strings.generatingVoice, style = MaterialTheme.typography.labelMedium)
        } else if (isPlaying) {
            Icon(
                imageVector = Icons.Filled.Stop,
                contentDescription = strings.stopAudio,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = strings.stopAudio,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = labelIdle,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(labelIdle, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProviderCard(
    provider: ServiceProvider,
    isFavorite: Boolean,
    ttsState: TtsUiState,
    strings: AppStrings,
    language: AppLanguage,
    onCardClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onBookClick: () -> Unit,
    onSpeakClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPlayingThis = ttsState is TtsUiState.Playing && ttsState.activeTag == "provider_${provider.id}"
    val isGeneratingThis = ttsState is TtsUiState.Generating && ttsState.activeTag == "provider_${provider.id}"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick)
            .testTag("provider_card_${provider.id}"),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = tradeCategoryIcon(provider.tradeCategory),
                            contentDescription = StringsCatalog.categoryName(provider.tradeCategory, language),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = provider.businessName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${provider.city}, ${provider.country}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("fav_btn_${provider.id}")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = strings.savedFavorites,
                        tint = if (isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                VerificationBadge(tierKey = provider.verificationTier, language = language)
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = StringsCatalog.categoryName(provider.tradeCategory, language),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                if (provider.availableToday) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color(0xFFE1F5EB)
                    ) {
                        Text(
                            text = strings.availableToday,
                            style = MaterialTheme.typography.labelSmall,
                            color = VerifiedEmerald,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Text(
                text = provider.bio,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "—",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text("·", color = MaterialTheme.colorScheme.outline)

                    Text(
                        text = "${provider.yearsExperience} ${strings.yearsExp}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "${provider.hourlyRate.toInt()} ${provider.currency} ${strings.perHour}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onSpeakClick,
                    enabled = !isGeneratingThis,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("card_tts_${provider.id}")
                ) {
                    if (isGeneratingThis) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(
                            imageVector = if (isPlayingThis) Icons.Filled.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = strings.listenBio,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = if (isPlayingThis) strings.stopAudio else strings.listenBio,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1
                    )
                }

                Button(
                    onClick = onBookClick,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .testTag("card_book_${provider.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(strings.bookService, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
fun BookServiceDialog(
    provider: ServiceProvider,
    strings: AppStrings,
    language: AppLanguage,
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (serviceTitle: String, scheduledDate: String, notes: String) -> Unit
) {
    var serviceTitle by remember {
        mutableStateOf(StringsCatalog.categoryName(provider.tradeCategory, language))
    }
    var scheduledDate by remember {
        mutableStateOf(
            Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }.let {
                SimpleDateFormat("yyyy-MM-dd", Locale.US).format(it.time) + " 10:00"
            }
        )
    }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { if (!isSubmitting) onDismiss() },
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(strings.bookingDialogTitle, style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = "${provider.businessName} · ${provider.city}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = serviceTitle,
                    onValueChange = { serviceTitle = it.take(120) },
                    label = { Text(strings.serviceNeededLabel) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = scheduledDate,
                    onValueChange = { scheduledDate = it.take(40) },
                    label = { Text(strings.preferredDateLabel) },
                    placeholder = { Text("YYYY-MM-DD HH:mm") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it.take(400) },
                    label = { Text(strings.notesLabel) },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(serviceTitle, scheduledDate, notes) },
                enabled = !isSubmitting && serviceTitle.trim().length >= 2 && scheduledDate.trim().length >= 4,
                modifier = Modifier.testTag("confirm_booking_submit")
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(strings.submitBooking)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSubmitting) {
                Text(strings.cancel)
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PublishProviderDialog(
    strings: AppStrings,
    language: AppLanguage,
    isSubmitting: Boolean,
    defaultCoordinates: Pair<Double, Double>?,
    onDismiss: () -> Unit,
    onSubmit: (
        businessName: String,
        tradeCategory: TradeCategory,
        serviceKind: ServiceKind,
        bio: String,
        city: String,
        country: String,
        continent: Continent,
        latitude: Double,
        longitude: Double,
        hourlyRate: Double,
        currency: String,
        languages: List<String>,
        yearsExperience: Long
    ) -> Unit
) {
    var businessName by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var hourlyRateText by remember { mutableStateOf("45") }
    var currency by remember { mutableStateOf("USD") }
    var yearsExpText by remember { mutableStateOf("5") }
    var selectedCategory by remember { mutableStateOf(TradeCategory.PLUMBING) }
    var selectedKind by remember { mutableStateOf(ServiceKind.ARTISAN) }
    var selectedContinent by remember { mutableStateOf(Continent.AFRICA) }

    val defaultCoordsForContinent = when (selectedContinent) {
        Continent.AFRICA -> 33.5731 to -7.5898
        Continent.EUROPE -> 40.4168 to -3.7038
        Continent.ASIA -> 25.2048 to 55.2708
        Continent.NORTH_AMERICA -> 40.7128 to -74.0060
        Continent.SOUTH_AMERICA -> 4.7110 to -74.0721
        Continent.OCEANIA -> -33.8688 to 151.2093
    }

    AlertDialog(
        onDismissRequest = { if (!isSubmitting) onDismiss() },
        title = {
            Text(strings.publishListingTitle, style = MaterialTheme.typography.headlineSmall)
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = businessName,
                    onValueChange = { businessName = it.take(80) },
                    label = { Text(strings.businessNameLabel) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(strings.serviceKindLabel, style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ServiceKind.entries.forEach { kind ->
                        FilterChip(
                            selected = selectedKind == kind,
                            onClick = { selectedKind = kind },
                            label = {
                                Text(
                                    if (kind == ServiceKind.ARTISAN) strings.artisansOnly
                                    else strings.homeServicesOnly
                                )
                            }
                        )
                    }
                }

                Text(strings.tradeCategoryLabel, style = MaterialTheme.typography.labelMedium)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TradeCategory.entries.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(StringsCatalog.categoryName(cat.key, language)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }

                Text(strings.continentLabel, style = MaterialTheme.typography.labelMedium)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Continent.entries.forEach { cont ->
                        FilterChip(
                            selected = selectedContinent == cont,
                            onClick = { selectedContinent = cont },
                            label = { Text(StringsCatalog.continentName(cont.key, language)) }
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it.take(80) },
                        label = { Text(strings.cityLabel) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = country,
                        onValueChange = { country = it.take(80) },
                        label = { Text(strings.countryLabel) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = hourlyRateText,
                        onValueChange = { hourlyRateText = it.filter { c -> c.isDigit() || c == '.' }.take(7) },
                        label = { Text(strings.hourlyRateLabel) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = currency,
                        onValueChange = { currency = it.uppercase().take(4) },
                        label = { Text(strings.currencyLabel) },
                        singleLine = true,
                        modifier = Modifier.weight(0.7f)
                    )
                    OutlinedTextField(
                        value = yearsExpText,
                        onValueChange = { yearsExpText = it.filter { c -> c.isDigit() }.take(2) },
                        label = { Text(strings.experienceYearsLabel) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(0.7f)
                    )
                }

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it.take(500) },
                    label = { Text(strings.bioLabel) },
                    minLines = 3,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            val rate = hourlyRateText.toDoubleOrNull() ?: 0.0
            val years = yearsExpText.toLongOrNull() ?: 1L
            val canSubmit = !isSubmitting &&
                businessName.trim().length >= 2 &&
                city.trim().length >= 2 &&
                country.trim().length >= 2 &&
                bio.trim().length >= 5 &&
                rate in 1.0..10000.0

            Button(
                onClick = {
                    val coords = defaultCoordinates ?: defaultCoordsForContinent
                    onSubmit(
                        businessName,
                        selectedCategory,
                        selectedKind,
                        bio,
                        city,
                        country,
                        selectedContinent,
                        coords.first,
                        coords.second,
                        rate,
                        currency.ifBlank { "USD" },
                        listOf(language.code, "en").distinct(),
                        years.coerceIn(0L, 80L)
                    )
                },
                enabled = canSubmit,
                modifier = Modifier.testTag("publish_provider_submit")
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(strings.publishButton)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSubmitting) {
                Text(strings.cancel)
            }
        }
    )
}
