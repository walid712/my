package com.example.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.BuildConfig
import com.example.core.AppLanguage
import com.example.core.AppStrings
import com.example.core.StringsCatalog
import com.example.data.model.Continent
import com.example.data.model.ServiceProvider
import com.example.data.model.UiState
import com.example.ui.components.VerificationBadge
import com.example.ui.theme.RatingAmber
import com.example.ui.viewmodel.AiQueryUiState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MapDiscoveryScreen(
    providersState: UiState<List<ServiceProvider>>,
    selectedContinent: Continent?,
    mapsInsightsState: AiQueryUiState,
    strings: AppStrings,
    language: AppLanguage,
    onSelectContinent: (Continent?) -> Unit,
    onUpdateCoordinates: (Double, Double) -> Unit,
    onProviderClick: (ServiceProvider) -> Unit,
    onRequestMapsInsights: (Double, Double, String, String) -> Unit
) {
    val context = LocalContext.current
    val providers = (providersState as? UiState.Success)?.data.orEmpty()
    var selectedProvider by remember(providers) { mutableStateOf(providers.firstOrNull()) }
    var locationNotice by remember { mutableStateOf<String?>(null) }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    @SuppressLint("MissingPermission")
    fun fetchGpsLocation(onResult: (Double, Double) -> Unit) {
        val fused = LocationServices.getFusedLocationProviderClient(context)
        fused.lastLocation.addOnSuccessListener { loc ->
            if (loc != null) {
                onUpdateCoordinates(loc.latitude, loc.longitude)
                onResult(loc.latitude, loc.longitude)
            }
        }
    }

    val hasValidMapsApiKey = remember {
        val key = BuildConfig.MAPS_API_KEY
        key.isNotBlank() && key != "YOUR_GOOGLE_MAPS_API_KEY"
    }

    val initialLatLng = selectedProvider?.let { LatLng(it.latitude, it.longitude) }
        ?: LatLng(33.5731, -7.5898)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLatLng, 2.5f)
    }

    val locationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
        if (granted) {
            locationNotice = null
            fetchGpsLocation { lat, lng ->
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 10f))
            }
        } else {
            locationNotice = strings.locationPermissionDenied
        }
    }

    LaunchedEffect(selectedProvider?.id) {
        selectedProvider?.let { p ->
            if (hasValidMapsApiKey) {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(LatLng(p.latitude, p.longitude), 6f)
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column(
            modifier = Modifier.widthIn(max = 800.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.mapTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                FilledTonalButton(
                    onClick = {
                        if (hasLocationPermission) {
                            fetchGpsLocation { lat, lng ->
                                if (hasValidMapsApiKey) {
                                    cameraPositionState.move(
                                        CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 11f)
                                    )
                                }
                            }
                        } else {
                            locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    },
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("map_near_me_button")
                ) {
                    Icon(Icons.Filled.MyLocation, contentDescription = strings.nearMeGps, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(strings.nearMeGps, style = MaterialTheme.typography.labelMedium)
                }
            }

            if (!locationNotice.isNullOrBlank()) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = locationNotice.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedContinent == null,
                    onClick = { onSelectContinent(null) },
                    label = { Text(strings.allWorld) }
                )
                Continent.entries.forEach { cont ->
                    FilterChip(
                        selected = selectedContinent == cont,
                        onClick = { onSelectContinent(if (selectedContinent == cont) null else cont) },
                        label = { Text(StringsCatalog.continentName(cont.key, language)) }
                    )
                }
            }

            // Live Google Maps Platform SDK when MAPS_API_KEY is configured, or interactive global explorer fallback
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(310.dp)
                    .clip(MaterialTheme.shapes.large)
            ) {
                if (hasValidMapsApiKey) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(isMyLocationEnabled = hasLocationPermission)
                    ) {
                        providers.forEach { provider ->
                            Marker(
                                state = MarkerState(position = LatLng(provider.latitude, provider.longitude)),
                                title = provider.businessName,
                                snippet = "${provider.city} · ${provider.hourlyRate.toInt()} ${provider.currency}",
                                onClick = {
                                    selectedProvider = provider
                                    false
                                }
                            )
                        }
                    }
                } else {
                    WorldExplorerMapCanvas(
                        providers = providers,
                        selectedProvider = selectedProvider,
                        onSelectProvider = { selectedProvider = it }
                    )
                }
            }

            if (!hasValidMapsApiKey) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Map,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = strings.mapsKeyNoticeTitle,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = strings.mapsKeyNoticeBody,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            selectedProvider?.let { provider ->
                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = provider.businessName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${provider.city}, ${provider.country} · ${StringsCatalog.categoryName(provider.tradeCategory, language)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                "—",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        VerificationBadge(tierKey = provider.verificationTier, language = language)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val geoUri = Uri.parse(
                                        "geo:${provider.latitude},${provider.longitude}?q=${
                                            Uri.encode("${provider.businessName}, ${provider.city}, ${provider.country}")
                                        }"
                                    )
                                    val intent = Intent(Intent.ACTION_VIEW, geoUri)
                                    runCatching { context.startActivity(intent) }
                                },
                                modifier = Modifier.weight(1f).minimumInteractiveComponentSize()
                            ) {
                                Icon(Icons.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(strings.openInGoogleMaps, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }

                            Button(
                                onClick = { onProviderClick(provider) },
                                modifier = Modifier.weight(1f).minimumInteractiveComponentSize()
                            ) {
                                Text(strings.viewDetails)
                            }
                        }

                        FilledTonalButton(
                            onClick = {
                                onRequestMapsInsights(
                                    provider.latitude,
                                    provider.longitude,
                                    provider.city,
                                    StringsCatalog.categoryName(provider.tradeCategory, language)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .minimumInteractiveComponentSize()
                                .testTag("maps_ai_grounding_btn")
                        ) {
                            Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(strings.mapsAiDiscoveryBtn)
                        }
                    }
                }
            }

            when (mapsInsightsState) {
                is AiQueryUiState.Idle -> Unit
                is AiQueryUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is AiQueryUiState.Error -> {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.errorContainer
                    ) {
                        Text(
                            text = mapsInsightsState.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
                is AiQueryUiState.Success -> {
                    Card(
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Required Google Maps text attribution (untranslated, Roboto/Sans-Serif)
                            Text(
                                text = "Google Maps",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = mapsInsightsState.answer.text,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            if (mapsInsightsState.answer.sources.isNotEmpty()) {
                                Text(
                                    text = strings.verifiedSources,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    mapsInsightsState.answer.sources.forEach { src ->
                                        AssistChip(
                                            onClick = {
                                                runCatching {
                                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(src.uri)))
                                                }
                                            },
                                            leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                            label = { Text(src.title, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WorldExplorerMapCanvas(
    providers: List<ServiceProvider>,
    selectedProvider: ServiceProvider?,
    onSelectProvider: (ServiceProvider) -> Unit
) {
    val density = LocalDensity.current
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF133126),
                        Color(0xFF1B4332),
                        Color(0xFF0F261D)
                    )
                )
            )
    ) {
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridColor = Color.White.copy(alpha = 0.12f)
            val dash = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            for (i in 1..5) {
                val y = size.height * (i / 6f)
                drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.5f, pathEffect = dash)
            }
            for (i in 1..7) {
                val x = size.width * (i / 8f)
                drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1.5f, pathEffect = dash)
            }
            // Equator & Prime Meridian highlights
            drawLine(
                Color(0x55B58328),
                Offset(0f, size.height * 0.5f),
                Offset(size.width, size.height * 0.5f),
                strokeWidth = 2f
            )
            drawLine(
                Color(0x55B58328),
                Offset(size.width * 0.5f, 0f),
                Offset(size.width * 0.5f, size.height),
                strokeWidth = 2f
            )
        }

        providers.forEach { provider ->
            val normX = ((provider.longitude + 180.0) / 360.0).toFloat().coerceIn(0.06f, 0.94f)
            val normY = ((90.0 - provider.latitude) / 180.0).toFloat().coerceIn(0.10f, 0.88f)
            val pinSizePx = with(density) { 44.dp.toPx() }
            val xPx = (normX * widthPx - pinSizePx / 2f).roundToInt()
            val yPx = (normY * heightPx - pinSizePx / 2f).roundToInt()
            val isSelected = selectedProvider?.id == provider.id

            Box(
                modifier = Modifier
                    .offset { IntOffset(xPx, yPx) }
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.secondary else Color(0xFFFDFBF7)
                    )
                    .clickable { onSelectProvider(provider) }
                    .testTag("world_pin_${provider.id}"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = provider.city,
                    tint = if (isSelected) Color.White else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(if (isSelected) 26.dp else 20.dp)
                )
            }
        }
    }
}
