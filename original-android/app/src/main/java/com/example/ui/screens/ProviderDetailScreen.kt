package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.AppLanguage
import com.example.core.AppStrings
import com.example.core.StringsCatalog
import com.example.data.model.ProviderReview
import com.example.data.model.ServiceProvider
import com.example.data.model.UiState
import com.example.ui.components.BookServiceDialog
import com.example.ui.components.TtsActionButton
import com.example.ui.components.VerificationBadge
import com.example.ui.components.tradeCategoryIcon
import com.example.ui.theme.RatingAmber
import com.example.ui.viewmodel.TtsUiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProviderDetailScreen(
    provider: ServiceProvider,
    reviewsState: UiState<List<ProviderReview>>,
    isFavorite: Boolean,
    ttsState: TtsUiState,
    strings: AppStrings,
    language: AppLanguage,
    isSubmitting: Boolean,
    onSelectForReviews: (String) -> Unit,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onSpeakBio: () -> Unit,
    onCreateBooking: (ServiceProvider, String, String, String, () -> Unit) -> Unit,
    onSubmitReview: (String, Long, String, AppLanguage, () -> Unit) -> Unit
) {
    val context = LocalContext.current
    val verifiedReviews = (reviewsState as? UiState.Success)?.data.orEmpty()
    val verifiedRating = verifiedReviews.takeIf { it.isNotEmpty() }?.map { it.rating }?.average()
    var showBookDialog by remember { mutableStateOf(false) }
    var ratingInput by remember { mutableLongStateOf(5L) }
    var commentInput by remember { mutableStateOf("") }

    BackHandler(onBack = onBack)

    LaunchedEffect(provider.id) {
        onSelectForReviews(provider.id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(provider.businessName, maxLines = 1) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.minimumInteractiveComponentSize().testTag("detail_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.back)
                    }
                },
                actions = {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = strings.savedFavorites,
                            tint = if (isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 680.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = tradeCategoryIcon(provider.tradeCategory),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = provider.businessName,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.LocationOn,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "${provider.city}, ${provider.country}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
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
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "${provider.yearsExperience} ${strings.yearsExp}",
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Text(
                            text = provider.bio,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = "${strings.languagesSpoken}: ${
                                provider.languages.joinToString(" · ") { AppLanguage.fromCode(it).nativeLabel }
                            }",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (verifiedRating != null) {
                                    Icon(Icons.Filled.Star, contentDescription = null, tint = RatingAmber)
                                    Text(
                                        text = "${String.format("%.1f", verifiedRating)} (${verifiedReviews.size} ${strings.reviewsLabel})",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Text(strings.noReviewsYet, style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Text(
                                text = "${provider.hourlyRate.toInt()} ${provider.currency} ${strings.perHour}",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            TtsActionButton(
                                tag = "provider_${provider.id}",
                                labelIdle = strings.listenBio,
                                strings = strings,
                                ttsState = ttsState,
                                onClick = onSpeakBio,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedButton(
                                onClick = {
                                    val geoUri = Uri.parse(
                                        "geo:${provider.latitude},${provider.longitude}?q=${
                                            Uri.encode("${provider.businessName}, ${provider.city}")
                                        }"
                                    )
                                    runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, geoUri)) }
                                },
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.minimumInteractiveComponentSize()
                            ) {
                                Icon(Icons.Filled.OpenInNew, contentDescription = strings.openInGoogleMaps, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Maps")
                            }
                        }

                        Button(
                            onClick = { showBookDialog = true },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .minimumInteractiveComponentSize()
                                .testTag("detail_book_now_btn")
                        ) {
                            Text(strings.bookService, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }

                // Write Review Card
                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(strings.writeReviewTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            (1L..5L).forEach { star ->
                                IconButton(
                                    onClick = { ratingInput = star },
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(
                                        imageVector = if (star <= ratingInput) Icons.Filled.Star else Icons.Filled.StarOutline,
                                        contentDescription = "$star",
                                        tint = RatingAmber
                                    )
                                }
                            }
                        }
                        OutlinedTextField(
                            value = commentInput,
                            onValueChange = { commentInput = it.take(400) },
                            placeholder = { Text(strings.reviewCommentHint) },
                            minLines = 2,
                            maxLines = 4,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = {
                                onSubmitReview(provider.id, ratingInput, commentInput, language) {
                                    commentInput = ""
                                }
                            },
                            enabled = !isSubmitting && commentInput.trim().length >= 2,
                            modifier = Modifier.align(Alignment.End).minimumInteractiveComponentSize()
                        ) {
                            Text(strings.submitReview)
                        }
                    }
                }

                Text(
                    text = strings.verifiedReviews,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                when (reviewsState) {
                    is UiState.Loading -> CircularProgressIndicator()
                    is UiState.Error -> Text(reviewsState.message, color = MaterialTheme.colorScheme.error)
                    is UiState.Success -> {
                        if (reviewsState.data.isEmpty()) {
                            Text(
                                text = strings.noReviewsYet,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            reviewsState.data.forEach { review ->
                                Card(
                                    shape = MaterialTheme.shapes.medium,
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(review.authorName, fontWeight = FontWeight.SemiBold)
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Filled.Star, contentDescription = null, tint = RatingAmber, modifier = Modifier.size(16.dp))
                                                Spacer(Modifier.width(4.dp))
                                                Text("${review.rating}", fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        Text(review.comment, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showBookDialog) {
        BookServiceDialog(
            provider = provider,
            strings = strings,
            language = language,
            isSubmitting = isSubmitting,
            onDismiss = { showBookDialog = false },
            onSubmit = { title, date, notes ->
                onCreateBooking(provider, title, date, notes) {
                    showBookDialog = false
                }
            }
        )
    }
}
