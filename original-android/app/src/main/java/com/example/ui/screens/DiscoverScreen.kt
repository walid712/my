package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.core.AppLanguage
import com.example.core.AppStrings
import com.example.core.StringsCatalog
import com.example.data.model.Continent
import com.example.data.model.ServiceKind
import com.example.data.model.ServiceProvider
import com.example.data.model.TradeCategory
import com.example.data.model.UiState
import com.example.ui.components.BookServiceDialog
import com.example.ui.components.LanguageSwitcherRow
import com.example.ui.components.ProviderCard
import com.example.ui.components.PublishProviderDialog
import com.example.ui.components.tradeCategoryIcon
import com.example.ui.viewmodel.MarketplaceFilters
import com.example.ui.viewmodel.TtsUiState

@Composable
fun DiscoverScreen(
    providersState: UiState<List<ServiceProvider>>,
    filters: MarketplaceFilters,
    favoriteIds: Set<String>,
    ttsState: TtsUiState,
    strings: AppStrings,
    language: AppLanguage,
    isSubmitting: Boolean,
    userCoordinates: Pair<Double, Double>?,
    onSelectLanguage: (AppLanguage) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSelectContinent: (Continent?) -> Unit,
    onSelectCategory: (TradeCategory?) -> Unit,
    onSelectServiceKind: (ServiceKind?) -> Unit,
    onProviderClick: (ServiceProvider) -> Unit,
    onToggleFavorite: (ServiceProvider, Boolean) -> Unit,
    onSpeakBio: (ServiceProvider) -> Unit,
    onCreateBooking: (ServiceProvider, String, String, String, () -> Unit) -> Unit,
    onPublishProvider: (
        String, TradeCategory, ServiceKind, String, String, String,
        Continent, Double, Double, Double, String, List<String>, Long, () -> Unit
    ) -> Unit
) {
    var bookingTarget by remember { mutableStateOf<ServiceProvider?>(null) }
    var showPublishDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showPublishDialog = true },
                icon = { Icon(Icons.Filled.Add, contentDescription = strings.becomeProvider) },
                text = { Text(strings.becomeProvider) },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.testTag("publish_provider_fab")
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            val isWide = maxWidth >= 680.dp

            val headerContent: @Composable () -> Unit = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = strings.appName,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = strings.tagline,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    LanguageSwitcherRow(
                        selectedLanguage = language,
                        onSelectLanguage = onSelectLanguage
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(MaterialTheme.shapes.large)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_trust_banner),
                            contentDescription = strings.verifiedProsBannerTitle,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xD91B4332),
                                            Color(0x661B4332)
                                        )
                                    )
                                )
                                .padding(16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = strings.verifiedProsBannerTitle,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = strings.verifiedProsBannerSub,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = filters.query,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text(strings.searchPlaceholder) },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        trailingIcon = {
                            if (filters.query.isNotEmpty()) {
                                IconButton(onClick = { onSearchQueryChange("") }) {
                                    Icon(Icons.Filled.Clear, contentDescription = strings.cancel)
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(50),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("discover_search_input")
                    )

                    val kindOptions = listOf<ServiceKind?>(null, ServiceKind.ARTISAN, ServiceKind.HOME_SERVICE)
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        kindOptions.forEachIndexed { idx, kind ->
                            SegmentedButton(
                                selected = filters.selectedServiceKind == kind,
                                onClick = { onSelectServiceKind(kind) },
                                shape = SegmentedButtonDefaults.itemShape(index = idx, count = kindOptions.size),
                                label = {
                                    Text(
                                        text = when (kind) {
                                            null -> strings.allServices
                                            ServiceKind.ARTISAN -> strings.artisansOnly
                                            ServiceKind.HOME_SERVICE -> strings.homeServicesOnly
                                        },
                                        maxLines = 1
                                    )
                                }
                            )
                        }
                    }

                    // Global Continent Chips
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = filters.selectedContinent == null,
                            onClick = { onSelectContinent(null) },
                            leadingIcon = { Icon(Icons.Filled.Public, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            label = { Text(strings.allWorld) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                        Continent.entries.forEach { continent ->
                            FilterChip(
                                selected = filters.selectedContinent == continent,
                                onClick = {
                                    onSelectContinent(if (filters.selectedContinent == continent) null else continent)
                                },
                                label = { Text(StringsCatalog.continentName(continent.key, language)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    }

                    // Trade Category Chips
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = filters.selectedCategory == null,
                            onClick = { onSelectCategory(null) },
                            label = { Text(strings.allCategories) }
                        )
                        TradeCategory.entries.forEach { category ->
                            FilterChip(
                                selected = filters.selectedCategory == category,
                                onClick = {
                                    onSelectCategory(if (filters.selectedCategory == category) null else category)
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = tradeCategoryIcon(category.key),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                label = { Text(StringsCatalog.categoryName(category.key, language)) }
                            )
                        }
                    }
                }
            }

            when (providersState) {
                is UiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .widthIn(max = 900.dp)
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        headerContent()
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is UiState.Error -> {
                    Column(
                        modifier = Modifier
                            .widthIn(max = 900.dp)
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        headerContent()
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.errorContainer
                        ) {
                            Text(
                                text = providersState.message,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                is UiState.Success -> {
                    val list = providersState.data
                    if (isWide) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 96.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.widthIn(max = 1040.dp).fillMaxSize()
                        ) {
                            item(span = { GridItemSpan(2) }) {
                                headerContent()
                            }
                            items(list, key = { it.id }) { provider ->
                                val isFav = favoriteIds.contains(provider.id)
                                ProviderCard(
                                    provider = provider,
                                    isFavorite = isFav,
                                    ttsState = ttsState,
                                    strings = strings,
                                    language = language,
                                    onCardClick = { onProviderClick(provider) },
                                    onToggleFavorite = { onToggleFavorite(provider, isFav) },
                                    onBookClick = { bookingTarget = provider },
                                    onSpeakClick = { onSpeakBio(provider) }
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.widthIn(max = 600.dp).fillMaxSize()
                        ) {
                            item {
                                headerContent()
                            }
                            items(list, key = { it.id }) { provider ->
                                val isFav = favoriteIds.contains(provider.id)
                                ProviderCard(
                                    provider = provider,
                                    isFavorite = isFav,
                                    ttsState = ttsState,
                                    strings = strings,
                                    language = language,
                                    onCardClick = { onProviderClick(provider) },
                                    onToggleFavorite = { onToggleFavorite(provider, isFav) },
                                    onBookClick = { bookingTarget = provider },
                                    onSpeakClick = { onSpeakBio(provider) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    bookingTarget?.let { target ->
        BookServiceDialog(
            provider = target,
            strings = strings,
            language = language,
            isSubmitting = isSubmitting,
            onDismiss = { bookingTarget = null },
            onSubmit = { title, date, notes ->
                onCreateBooking(target, title, date, notes) {
                    bookingTarget = null
                }
            }
        )
    }

    if (showPublishDialog) {
        PublishProviderDialog(
            strings = strings,
            language = language,
            isSubmitting = isSubmitting,
            defaultCoordinates = userCoordinates,
            onDismiss = { showPublishDialog = false },
            onSubmit = { name, cat, kind, bio, city, country, cont, lat, lng, rate, curr, langs, yrs ->
                onPublishProvider(name, cat, kind, bio, city, country, cont, lat, lng, rate, curr, langs, yrs) {
                    showPublishDialog = false
                }
            }
        )
    }
}
