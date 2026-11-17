package com.example.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.AppLanguage
import com.example.data.model.BookingStatus
import com.example.data.model.Continent
import com.example.data.model.FavoriteProvider
import com.example.data.model.ProviderReview
import com.example.data.model.ServiceBooking
import com.example.data.model.ServiceKind
import com.example.data.model.ServiceProvider
import com.example.data.model.TradeCategory
import com.example.data.model.UiState
import com.example.data.model.UserPrivateInfo
import com.example.data.model.UserSummary
import com.example.data.repository.MarketplaceRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TAG = "MarketplaceVM"
private const val STOP_TIMEOUT_MS = 5000L

data class MarketplaceFilters(
    val query: String = "",
    val selectedContinent: Continent? = null,
    val selectedCategory: TradeCategory? = null,
    val selectedServiceKind: ServiceKind? = null
)

class MarketplaceViewModel(
    private val repository: MarketplaceRepository,
    val currentUserId: String
) : ViewModel() {

    private val _filters = MutableStateFlow(MarketplaceFilters())
    val filters: StateFlow<MarketplaceFilters> = _filters.asStateFlow()

    private val _selectedProviderId = MutableStateFlow<String?>(null)
    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    private val _userCoordinates = MutableStateFlow<Pair<Double, Double>?>(null)
    val userCoordinates: StateFlow<Pair<Double, Double>?> = _userCoordinates.asStateFlow()

    val providersState: StateFlow<UiState<List<ServiceProvider>>> =
        repository.observeProviders()
            .asUiStateFlow("Failed to load providers")

    val filteredProvidersState: StateFlow<UiState<List<ServiceProvider>>> =
        combine(providersState, _filters) { state, filter ->
            when (state) {
                is UiState.Loading -> UiState.Loading
                is UiState.Error -> state
                is UiState.Success -> {
                    val q = filter.query.trim().lowercase()
                    val filtered = state.data.filter { provider ->
                        val matchesContinent = filter.selectedContinent == null ||
                            provider.continent == filter.selectedContinent.key
                        val matchesCategory = filter.selectedCategory == null ||
                            provider.tradeCategory == filter.selectedCategory.key
                        val matchesKind = filter.selectedServiceKind == null ||
                            provider.serviceKind == filter.selectedServiceKind.key
                        val matchesQuery = q.isEmpty() ||
                            provider.businessName.lowercase().contains(q) ||
                            provider.city.lowercase().contains(q) ||
                            provider.country.lowercase().contains(q) ||
                            provider.bio.lowercase().contains(q) ||
                            provider.tradeCategory.lowercase().contains(q)
                        matchesContinent && matchesCategory && matchesKind && matchesQuery
                    }
                    UiState.Success(filtered)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = UiState.Loading
        )

    val myBookingsState: StateFlow<UiState<List<ServiceBooking>>> =
        repository.observeMyBookings(currentUserId)
            .asUiStateFlow("Failed to load bookings")

    val providerJobsState: StateFlow<UiState<List<ServiceBooking>>> =
        repository.observeProviderJobs(currentUserId)
            .asUiStateFlow("Failed to load provider jobs")

    val favoritesState: StateFlow<UiState<List<FavoriteProvider>>> =
        repository.observeFavorites(currentUserId)
            .asUiStateFlow("Failed to load favorites")

    val userSummary: StateFlow<UserSummary?> =
        repository.observeUserSummary(currentUserId)
            .catch { error ->
                val firestoreError = unwrapFirestoreException(error)
                if (firestoreError == null && error is CancellationException) throw error
                Log.w(TAG, "Error observing user summary", firestoreError ?: error)
                emit(null)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), null)

    val userPrivateInfo: StateFlow<UserPrivateInfo?> =
        repository.observeUserPrivateInfo(currentUserId)
            .catch { error ->
                val firestoreError = unwrapFirestoreException(error)
                if (firestoreError == null && error is CancellationException) throw error
                Log.w(TAG, "Error observing user private info", firestoreError ?: error)
                emit(null)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedProviderReviews: StateFlow<UiState<List<ProviderReview>>> =
        _selectedProviderId.flatMapLatest { id ->
            if (id.isNullOrBlank()) {
                flowOf<UiState<List<ProviderReview>>>(UiState.Success(emptyList()))
            } else {
                repository.observeProviderReviews(id)
                    .asUiStateFlow("Failed to load reviews")
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = UiState.Success(emptyList())
        )

    fun initializeSession(language: AppLanguage) {
        viewModelScope.launch {
            repository.ensureUserInitialized(language)
        }
    }

    fun syncLanguageToProfile(language: AppLanguage) {
        viewModelScope.launch {
            repository.updateUserLanguage(language)
        }
    }

    fun selectProviderForReviews(providerId: String) {
        _selectedProviderId.value = providerId
    }

    fun updateSearchQuery(query: String) {
        _filters.value = _filters.value.copy(query = query)
    }

    fun selectContinent(continent: Continent?) {
        _filters.value = _filters.value.copy(selectedContinent = continent)
    }

    fun selectCategory(category: TradeCategory?) {
        _filters.value = _filters.value.copy(selectedCategory = category)
    }

    fun selectServiceKind(kind: ServiceKind?) {
        _filters.value = _filters.value.copy(selectedServiceKind = kind)
    }

    fun updateUserCoordinates(lat: Double, lng: Double) {
        _userCoordinates.value = lat to lng
    }

    fun clearActionMessage() {
        _actionMessage.value = null
    }

    fun toggleFavorite(provider: ServiceProvider, currentlyFavorite: Boolean) {
        viewModelScope.launch {
            val result = repository.toggleFavorite(provider, currentlyFavorite)
            result.onFailure { _actionMessage.value = it.localizedMessage }
        }
    }

    fun publishProvider(
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
        yearsExperience: Long,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isSubmitting.value = true
            val result = repository.createProvider(
                businessName = businessName,
                tradeCategory = tradeCategory,
                serviceKind = serviceKind,
                bio = bio,
                city = city,
                country = country,
                continent = continent,
                latitude = latitude,
                longitude = longitude,
                hourlyRate = hourlyRate,
                currency = currency,
                languages = languages,
                yearsExperience = yearsExperience
            )
            _isSubmitting.value = false
            result.onSuccess {
                _actionMessage.value = "Listing published globally!"
                onSuccess()
            }.onFailure {
                _actionMessage.value = it.localizedMessage ?: "Failed to publish listing"
            }
        }
    }

    fun createBooking(
        provider: ServiceProvider,
        serviceTitle: String,
        scheduledDate: String,
        notes: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isSubmitting.value = true
            val result = repository.createBooking(provider, serviceTitle, scheduledDate, notes)
            _isSubmitting.value = false
            result.onSuccess {
                _actionMessage.value = "Booking request sent to ${provider.businessName}"
                onSuccess()
            }.onFailure {
                _actionMessage.value = it.localizedMessage ?: "Failed to submit booking"
            }
        }
    }

    fun updateBookingStatus(bookingId: String, status: BookingStatus) {
        viewModelScope.launch {
            val result = repository.updateBookingStatus(bookingId, status)
            result.onFailure {
                _actionMessage.value = it.localizedMessage ?: "Failed to update booking"
            }
        }
    }

    fun submitReview(
        providerId: String,
        rating: Long,
        comment: String,
        language: AppLanguage,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isSubmitting.value = true
            val result = repository.addReview(providerId, rating, comment, language)
            _isSubmitting.value = false
            result.onSuccess {
                _actionMessage.value = "Review posted"
                onSuccess()
            }.onFailure {
                _actionMessage.value = it.localizedMessage ?: "Failed to post review"
            }
        }
    }

    fun savePrivateContactInfo(phone: String, address: String) {
        viewModelScope.launch {
            _isSubmitting.value = true
            val result = repository.saveUserPrivateInfo(phone, address)
            _isSubmitting.value = false
            result.onSuccess {
                _actionMessage.value = "Private contact details saved securely"
            }.onFailure {
                _actionMessage.value = it.localizedMessage ?: "Failed to save contact details"
            }
        }
    }

    private fun <T> Flow<T>.asUiStateFlow(fallbackError: String): StateFlow<UiState<T>> =
        this.map<T, UiState<T>> { UiState.Success(it) }
            .catch { error ->
                val firestoreError = unwrapFirestoreException(error)
                if (firestoreError == null && error is CancellationException) throw error
                val rootError = firestoreError ?: error
                Log.w(TAG, fallbackError, rootError)
                emit(UiState.Error(rootError.message ?: fallbackError))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
                initialValue = UiState.Loading
            )

    private fun unwrapFirestoreException(error: Throwable): FirebaseFirestoreException? =
        generateSequence(error) { it.cause }
            .filterIsInstance<FirebaseFirestoreException>()
            .firstOrNull()
}
