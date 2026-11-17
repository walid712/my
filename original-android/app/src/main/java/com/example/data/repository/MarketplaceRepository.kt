package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.core.AppLanguage
import com.example.core.OperationType
import com.example.core.handleFirestoreError
import com.example.data.model.BookingStatus
import com.example.data.model.Continent
import com.example.data.model.FavoriteProvider
import com.example.data.model.ProviderReview
import com.example.data.model.ServiceBooking
import com.example.data.model.ServiceKind
import com.example.data.model.ServiceProvider
import com.example.data.model.TradeCategory
import com.example.data.model.UserPrivateInfo
import com.example.data.model.UserSummary
import com.example.data.model.VerificationTier
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class MarketplaceRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private fun requireUserId(): String =
        auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")

    private fun handleStreamError(error: Throwable, uid: String, path: String): Nothing {
        val firestoreError = generateSequence(error) { it.cause }
            .filterIsInstance<FirebaseFirestoreException>()
            .firstOrNull()
        if (firestoreError == null && error is CancellationException) throw error
        val exceptionToLog = firestoreError ?: (error as? Exception)
        if (exceptionToLog != null && auth.currentUser?.uid == uid) {
            handleFirestoreError(exceptionToLog, OperationType.LIST, path)
        }
        throw error
    }

    fun observeProviders(): Flow<List<ServiceProvider>> = flow {
        val uid = requireUserId()
        val path = "providers"
        emitAll(
            db.collection(path)
                .whereEqualTo("published", true)
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        ServiceProvider::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    ).sortedBy { it.businessName.lowercase() }
                }
                .catch { error -> handleStreamError(error, uid, path) }
        )
    }

    fun observeProviderReviews(providerId: String): Flow<List<ProviderReview>> = flow {
        val uid = requireUserId()
        val path = "providers/$providerId/reviews"
        emitAll(
            db.collection("providers").document(providerId).collection("reviews")
                .whereEqualTo("published", true)
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        ProviderReview::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    ).sortedByDescending { it.createdAt?.seconds ?: 0L }
                }
                .catch { error -> handleStreamError(error, uid, path) }
        )
    }

    fun observeMyBookings(userId: String): Flow<List<ServiceBooking>> = flow {
        val uid = requireUserId()
        check(uid == userId) { "Authenticated UID mismatch." }
        val path = "bookings"
        emitAll(
            db.collection(path)
                .whereEqualTo("clientId", userId)
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        ServiceBooking::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    ).sortedByDescending { it.createdAt?.seconds ?: 0L }
                }
                .catch { error -> handleStreamError(error, uid, path) }
        )
    }

    fun observeProviderJobs(userId: String): Flow<List<ServiceBooking>> = flow {
        val uid = requireUserId()
        check(uid == userId) { "Authenticated UID mismatch." }
        val path = "bookings"
        emitAll(
            db.collection(path)
                .whereEqualTo("providerOwnerId", userId)
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        ServiceBooking::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    ).sortedByDescending { it.createdAt?.seconds ?: 0L }
                }
                .catch { error -> handleStreamError(error, uid, path) }
        )
    }

    fun observeFavorites(userId: String): Flow<List<FavoriteProvider>> = flow {
        val uid = requireUserId()
        check(uid == userId) { "Authenticated UID mismatch." }
        val path = "users/$userId/favorites"
        emitAll(
            db.collection("users").document(userId).collection("favorites")
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        FavoriteProvider::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    )
                }
                .catch { error -> handleStreamError(error, uid, path) }
        )
    }

    fun observeUserSummary(userId: String): Flow<UserSummary?> = flow {
        val uid = requireUserId()
        check(uid == userId) { "Authenticated UID mismatch." }
        val path = "users/$userId"
        emitAll(
            db.collection("users").document(userId)
                .snapshots()
                .map { snapshot ->
                    if (snapshot.exists()) {
                        snapshot.toObject(
                            UserSummary::class.java,
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )
                    } else null
                }
                .catch { error -> handleStreamError(error, uid, path) }
        )
    }

    fun observeUserPrivateInfo(userId: String): Flow<UserPrivateInfo?> = flow {
        val uid = requireUserId()
        check(uid == userId) { "Authenticated UID mismatch." }
        val path = "users/$userId/private/info"
        emitAll(
            db.collection("users").document(userId)
                .collection("private").document("info")
                .snapshots()
                .map { snapshot ->
                    if (snapshot.exists()) {
                        snapshot.toObject(
                            UserPrivateInfo::class.java,
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )
                    } else null
                }
                .catch { error -> handleStreamError(error, uid, path) }
        )
    }

    suspend fun ensureUserInitialized(language: AppLanguage): Result<Unit> {
        var path = "users"
        return try {
            val user = auth.currentUser ?: throw IllegalStateException("Not signed in")
            val uid = user.uid
            val summaryRef = db.collection("users").document(uid)
            path = summaryRef.path
            val summarySnap = summaryRef.get().await()
            val displayName = (user.displayName ?: "Craft Member").trim().take(80).ifBlank { "Craft Member" }
            if (!summarySnap.exists()) {
                summaryRef.set(
                    mapOf(
                        "userId" to uid,
                        "displayName" to displayName,
                        "preferredLanguage" to language.code,
                        "preferredCity" to "Casablanca",
                        "preferredCountry" to "Morocco",
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            }
            val privateRef = summaryRef.collection("private").document("info")
            path = privateRef.path
            if (!privateRef.get().await().exists()) {
                val payload = mutableMapOf<String, Any>(
                    "userId" to uid,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                user.email?.takeIf { it.isNotBlank() }?.let { payload["email"] = it.take(120) }
                privateRef.set(payload).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    suspend fun updateUserLanguage(language: AppLanguage): Result<Unit> {
        var path = "users"
        return try {
            val uid = requireUserId()
            val docRef = db.collection("users").document(uid)
            path = docRef.path
            if (docRef.get().await().exists()) {
                docRef.update(
                    mapOf(
                        "preferredLanguage" to language.code,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, path)
            Result.failure(e)
        }
    }

    suspend fun saveUserPrivateInfo(phone: String, homeAddress: String): Result<Unit> {
        var path = "users"
        return try {
            val uid = requireUserId()
            val cleanPhone = phone.trim().take(40)
            val cleanAddress = homeAddress.trim().take(200)
            val docRef = db.collection("users").document(uid).collection("private").document("info")
            path = docRef.path
            if (docRef.get().await().exists()) {
                docRef.update(
                    mapOf(
                        "phone" to cleanPhone,
                        "homeAddress" to cleanAddress,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            } else {
                val payload = mutableMapOf<String, Any>(
                    "userId" to uid,
                    "phone" to cleanPhone,
                    "homeAddress" to cleanAddress,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                auth.currentUser?.email?.takeIf { it.isNotBlank() }?.let {
                    payload["email"] = it.take(120)
                }
                docRef.set(payload).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    suspend fun toggleFavorite(provider: ServiceProvider, currentlyFavorite: Boolean): Result<Unit> {
        var path = "users"
        return try {
            val uid = requireUserId()
            require(provider.id.isNotBlank()) { "Provider ID cannot be blank" }
            val favRef = db.collection("users").document(uid)
                .collection("favorites").document(provider.id)
            path = favRef.path
            if (currentlyFavorite) {
                favRef.delete().await()
            } else {
                favRef.set(
                    mapOf(
                        "userId" to uid,
                        "providerId" to provider.id,
                        "providerName" to provider.businessName.take(80),
                        "tradeCategory" to provider.tradeCategory,
                        "city" to provider.city.take(80),
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    suspend fun createProvider(
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
    ): Result<String> {
        var path = "providers"
        return try {
            val uid = requireUserId()
            val cleanName = businessName.trim()
            val cleanBio = bio.trim()
            val cleanCity = city.trim()
            val cleanCountry = country.trim()
            val cleanCurrency = currency.trim().uppercase().ifBlank { "USD" }
            require(cleanName.length in 2..80) { "Business name must be 2-80 characters" }
            require(cleanBio.length in 5..500) { "Bio must be 5-500 characters" }
            require(cleanCity.length in 2..80) { "City must be 2-80 characters" }
            require(cleanCountry.length in 2..80) { "Country must be 2-80 characters" }
            require(hourlyRate in 1.0..10000.0) { "Hourly rate must be 1..10000" }
            require(yearsExperience in 0L..80L) { "Experience must be 0..80 years" }
            val docRef = db.collection("providers").document(uid)
            path = docRef.path
            val payload = mapOf(
                "ownerId" to uid,
                "businessName" to cleanName,
                "tradeCategory" to tradeCategory.key,
                "serviceKind" to serviceKind.key,
                "bio" to cleanBio,
                "city" to cleanCity,
                "country" to cleanCountry,
                "continent" to continent.key,
                "latitude" to (kotlin.math.round(latitude.coerceIn(-90.0, 90.0) * 100.0) / 100.0),
                "longitude" to (kotlin.math.round(longitude.coerceIn(-180.0, 180.0) * 100.0) / 100.0),
                "hourlyRate" to hourlyRate,
                "currency" to cleanCurrency.take(5),
                "languages" to languages.take(4).ifEmpty { listOf("en") },
                "rating" to 0.0,
                "reviewCount" to 0L,
                "yearsExperience" to yearsExperience,
                "verificationTier" to VerificationTier.UNVERIFIED.key,
                "availableToday" to true,
                "published" to true,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
            Result.success(docRef.id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun createBooking(
        provider: ServiceProvider,
        serviceTitle: String,
        scheduledDate: String,
        notes: String
    ): Result<String> {
        var path = "bookings"
        return try {
            val user = auth.currentUser ?: throw IllegalStateException("Not signed in")
            val uid = user.uid
            val cleanTitle = serviceTitle.trim()
            val cleanDate = scheduledDate.trim()
            val cleanNotes = notes.trim().take(400)
            require(provider.id.isNotBlank()) { "Provider ID is required" }
            require(cleanTitle.length in 2..120) { "Service title must be 2-120 characters" }
            require(cleanDate.matches(Regex("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$"))) {
                "Use date and time format YYYY-MM-DD HH:mm."
            }
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).apply { isLenient = false }
            val scheduledInstant = dateFormat.parse(cleanDate)
                ?: throw IllegalArgumentException("Invalid booking date and time.")
            require(scheduledInstant.after(Date())) { "Choose a future date and time." }
            val privateInfo = db.collection("users").document(uid)
                .collection("private").document("info").get().await()
            val contactPhone = privateInfo.getString("phone")?.trim()?.take(40).orEmpty()
            val serviceAddress = privateInfo.getString("homeAddress")?.trim()?.take(200).orEmpty()
            val docRef = db.collection("bookings").document()
            path = docRef.path
            val payload = mutableMapOf<String, Any>(
                "clientId" to uid,
                "clientName" to (user.displayName ?: "Member").trim().take(80).ifBlank { "Member" },
                "providerId" to provider.id,
                "providerName" to provider.businessName.take(80),
                "providerOwnerId" to provider.ownerId,
                "tradeCategory" to provider.tradeCategory,
                "serviceTitle" to cleanTitle,
                "scheduledDate" to cleanDate,
                "scheduledAt" to com.google.firebase.Timestamp(scheduledInstant),
                "city" to provider.city.take(80),
                // A transparent one-hour starting estimate; the final quote is agreed by both parties.
                "estimatedPrice" to provider.hourlyRate.coerceIn(0.0, 100000.0),
                "currency" to provider.currency.take(5),
                "status" to BookingStatus.PENDING.key,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            if (cleanNotes.isNotEmpty()) {
                payload["notes"] = cleanNotes
            }
            if (contactPhone.isNotBlank()) payload["clientPhone"] = contactPhone
            if (serviceAddress.isNotBlank()) payload["serviceAddress"] = serviceAddress
            docRef.set(payload).await()
            Result.success(docRef.id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun updateBookingStatus(bookingId: String, status: BookingStatus): Result<Unit> {
        var path = "bookings/$bookingId"
        return try {
            requireUserId()
            require(bookingId.isNotBlank()) { "Booking ID must not be blank" }
            val docRef = db.collection("bookings").document(bookingId)
            path = docRef.path
            docRef.update(
                mapOf(
                    "status" to status.key,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, path)
            Result.failure(e)
        }
    }

    suspend fun addReview(
        providerId: String,
        rating: Long,
        comment: String,
        language: AppLanguage
    ): Result<Unit> {
        var path = "providers/$providerId/reviews"
        return try {
            val user = auth.currentUser ?: throw IllegalStateException("Not signed in")
            val uid = user.uid
            val cleanComment = comment.trim()
            require(providerId.isNotBlank()) { "Provider ID is required" }
            require(rating in 1L..5L) { "Rating must be 1..5" }
            require(cleanComment.length in 2..400) { "Comment must be 2-400 characters" }
            val completedBooking = db.collection("bookings")
                .whereEqualTo("clientId", uid)
                .whereEqualTo("providerId", providerId)
                .whereEqualTo("status", BookingStatus.COMPLETED.key)
                .limit(1)
                .get()
                .await()
                .documents
                .firstOrNull()
                ?: throw IllegalStateException("Only customers with a completed booking can review this provider.")
            val docRef = db.collection("providers").document(providerId)
                .collection("reviews").document(uid)
            path = docRef.path
            docRef.set(
                mapOf(
                    "providerId" to providerId,
                    "bookingId" to completedBooking.id,
                    "authorId" to uid,
                    "authorName" to (user.displayName ?: "Member").trim().take(80).ifBlank { "Member" },
                    "rating" to rating,
                    "comment" to cleanComment,
                    "language" to language.code,
                    "published" to true,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

}
