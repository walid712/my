package com.example.data.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

enum class TradeCategory(val key: String) {
    PLUMBING("plumbing"),
    ELECTRICAL("electrical"),
    CARPENTRY("carpentry"),
    CLEANING("cleaning"),
    HVAC("hvac"),
    APPLIANCE("appliance"),
    PAINTING("painting"),
    ZELLIGE("zellige"),
    METALWORK("metalwork"),
    GARDENING("gardening"),
    MOVING("moving"),
    SMART_HOME("smart_home");

    companion object {
        fun fromKey(raw: String?): TradeCategory =
            entries.firstOrNull { it.key == raw } ?: PLUMBING
    }
}

enum class ServiceKind(val key: String) {
    ARTISAN("artisan"),
    HOME_SERVICE("home_service");

    companion object {
        fun fromKey(raw: String?): ServiceKind =
            entries.firstOrNull { it.key == raw } ?: ARTISAN
    }
}

enum class Continent(val key: String) {
    AFRICA("africa"),
    EUROPE("europe"),
    ASIA("asia"),
    NORTH_AMERICA("north_america"),
    SOUTH_AMERICA("south_america"),
    OCEANIA("oceania");

    companion object {
        fun fromKey(raw: String?): Continent =
            entries.firstOrNull { it.key == raw } ?: AFRICA
    }
}

enum class VerificationTier(val key: String) {
    UNVERIFIED("unverified"),
    ID_VERIFIED("id_verified"),
    MASTER_ARTISAN("master_artisan"),
    INSURED_PRO("insured_pro");

    companion object {
        fun fromKey(raw: String?): VerificationTier =
            entries.firstOrNull { it.key == raw } ?: UNVERIFIED
    }
}

enum class BookingStatus(val key: String) {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    COMPLETION_PENDING("completion_pending"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    companion object {
        fun fromKey(raw: String?): BookingStatus =
            entries.firstOrNull { it.key == raw } ?: PENDING
    }
}

@Keep
@IgnoreExtraProperties
data class ServiceProvider(
    @DocumentId val id: String = "",
    val ownerId: String = "",
    val businessName: String = "",
    val tradeCategory: String = TradeCategory.PLUMBING.key,
    val serviceKind: String = ServiceKind.ARTISAN.key,
    val bio: String = "",
    val city: String = "",
    val country: String = "",
    val continent: String = Continent.AFRICA.key,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val hourlyRate: Double = 0.0,
    val currency: String = "USD",
    val languages: List<String> = listOf("en"),
    val rating: Double = 0.0,
    val reviewCount: Long = 0L,
    val yearsExperience: Long = 1L,
    val verificationTier: String = VerificationTier.UNVERIFIED.key,
    val availableToday: Boolean = true,
    val published: Boolean = true,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class ProviderReview(
    @DocumentId val id: String = "",
    val providerId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val rating: Long = 0L,
    val comment: String = "",
    val language: String = "en",
    val published: Boolean = true,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class ServiceBooking(
    @DocumentId val id: String = "",
    val clientId: String = "",
    val clientName: String = "",
    val clientPhone: String = "",
    val serviceAddress: String = "",
    val providerId: String = "",
    val providerName: String = "",
    val providerOwnerId: String = "",
    val tradeCategory: String = TradeCategory.PLUMBING.key,
    val serviceTitle: String = "",
    val scheduledDate: String = "",
    val scheduledAt: Timestamp? = null,
    val city: String = "",
    val notes: String = "",
    val estimatedPrice: Double = 0.0,
    val currency: String = "USD",
    val status: String = BookingStatus.PENDING.key,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class UserSummary(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "",
    val preferredLanguage: String = "en",
    val preferredCity: String = "",
    val preferredCountry: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class UserPrivateInfo(
    @DocumentId val id: String = "",
    val userId: String = "",
    val email: String = "",
    val phone: String = "",
    val homeAddress: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class FavoriteProvider(
    @DocumentId val id: String = "",
    val userId: String = "",
    val providerId: String = "",
    val providerName: String = "",
    val tradeCategory: String = TradeCategory.PLUMBING.key,
    val city: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)
