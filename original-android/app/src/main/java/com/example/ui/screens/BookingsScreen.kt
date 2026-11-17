package com.example.ui.screens

import android.content.Intent
import android.net.Uri

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.core.AppLanguage
import com.example.core.AppStrings
import com.example.core.StringsCatalog
import com.example.data.model.BookingStatus
import com.example.data.model.ServiceBooking
import com.example.data.model.UiState
import com.example.ui.components.TtsActionButton
import com.example.ui.theme.BrassContainer
import com.example.ui.theme.OnBrassContainer
import com.example.ui.theme.VerifiedEmerald
import com.example.ui.viewmodel.TtsUiState

@Composable
fun BookingsScreen(
    myBookingsState: UiState<List<ServiceBooking>>,
    providerJobsState: UiState<List<ServiceBooking>>,
    ttsState: TtsUiState,
    strings: AppStrings,
    language: AppLanguage,
    onUpdateStatus: (String, BookingStatus) -> Unit,
    onSpeakSummary: (String, String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val activeState = if (selectedTab == 0) myBookingsState else providerJobsState

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 720.dp)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = strings.bookingsTitle,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            val tabs = listOf(strings.myBookingsTab, strings.providerJobsTab)
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                tabs.forEachIndexed { idx, label ->
                    SegmentedButton(
                        selected = selectedTab == idx,
                        onClick = { selectedTab = idx },
                        shape = SegmentedButtonDefaults.itemShape(index = idx, count = tabs.size),
                        label = { Text(label, maxLines = 1) }
                    )
                }
            }

            when (activeState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Error -> {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.errorContainer
                    ) {
                        Text(
                            text = activeState.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
                is UiState.Success -> {
                    val list = activeState.data
                    if (list.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_empty_bookings),
                                contentDescription = strings.emptyBookingsTitle,
                                modifier = Modifier
                                    .size(210.dp)
                                    .clip(MaterialTheme.shapes.large),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = strings.emptyBookingsTitle,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = strings.emptyBookingsDesc,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(list, key = { it.id }) { booking ->
                                BookingCard(
                                    booking = booking,
                                    isProviderView = selectedTab == 1,
                                    ttsState = ttsState,
                                    strings = strings,
                                    language = language,
                                    onUpdateStatus = { newStatus ->
                                        onUpdateStatus(booking.id, newStatus)
                                    },
                                    onSpeak = {
                                        val summary = "${booking.serviceTitle}. ${booking.providerName}, ${booking.city}. ${booking.scheduledDate}. ${
                                            StringsCatalog.bookingStatusName(booking.status, language)
                                        }."
                                        onSpeakSummary("booking_${booking.id}", summary)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookingCard(
    booking: ServiceBooking,
    isProviderView: Boolean,
    ttsState: TtsUiState,
    strings: AppStrings,
    language: AppLanguage,
    onUpdateStatus: (BookingStatus) -> Unit,
    onSpeak: () -> Unit
) {
    val context = LocalContext.current
    val statusLabel = StringsCatalog.bookingStatusName(booking.status, language)
    val (statusBg, statusFg) = when (booking.status) {
        BookingStatus.CONFIRMED.key -> Color(0xFFE1F5EB) to VerifiedEmerald
        BookingStatus.COMPLETED.key -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.primary
        BookingStatus.CANCELLED.key -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.error
        else -> BrassContainer to OnBrassContainer
    }
    val isTerminal = booking.status == BookingStatus.COMPLETED.key || booking.status == BookingStatus.CANCELLED.key

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = booking.serviceTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Surface(shape = RoundedCornerShape(50), color = statusBg) {
                    Text(
                        text = statusLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = statusFg,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = if (isProviderView) booking.clientName else booking.providerName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                    Text(booking.scheduledDate, style = MaterialTheme.typography.bodySmall)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.secondary)
                    Text(booking.city, style = MaterialTheme.typography.bodySmall)
                }
            }

            if (booking.notes.isNotBlank()) {
                Text(
                    text = booking.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isProviderView && booking.serviceAddress.isNotBlank()) {
                Text(
                    text = "Service address: ${booking.serviceAddress}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isProviderView && booking.clientPhone.isNotBlank()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Uri.encode(booking.clientPhone)}")))
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text("Call customer") }
                    OutlinedButton(
                        onClick = {
                            val digits = booking.clientPhone.filter { it.isDigit() }
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$digits")))
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text("WhatsApp") }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${booking.estimatedPrice.toInt()} ${booking.currency}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                TtsActionButton(
                    tag = "booking_${booking.id}",
                    labelIdle = strings.listenSummary,
                    strings = strings,
                    ttsState = ttsState,
                    onClick = onSpeak
                )
            }

            if (!isTerminal) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isProviderView && booking.status == BookingStatus.PENDING.key) {
                        Button(
                            onClick = { onUpdateStatus(BookingStatus.CONFIRMED) },
                            modifier = Modifier.weight(1f).minimumInteractiveComponentSize()
                        ) {
                            Text(strings.confirmBooking)
                        }
                    }
                    if (isProviderView && booking.status == BookingStatus.CONFIRMED.key) {
                        Button(
                            onClick = { onUpdateStatus(BookingStatus.COMPLETION_PENDING) },
                            modifier = Modifier.weight(1f).minimumInteractiveComponentSize()
                        ) {
                            Text(strings.markCompleted)
                        }
                    }
                    if (!isProviderView && booking.status == BookingStatus.COMPLETION_PENDING.key) {
                        Button(
                            onClick = { onUpdateStatus(BookingStatus.COMPLETED) },
                            modifier = Modifier.weight(1f).minimumInteractiveComponentSize()
                        ) {
                            Text("Confirm work received")
                        }
                    }
                    OutlinedButton(
                        onClick = { onUpdateStatus(BookingStatus.CANCELLED) },
                        modifier = Modifier.weight(1f).minimumInteractiveComponentSize()
                    ) {
                        Text(strings.cancelBooking)
                    }
                }
            }
        }
    }
}
