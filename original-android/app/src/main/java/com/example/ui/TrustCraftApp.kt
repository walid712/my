package com.example.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.R
import com.example.core.AppLanguage
import com.example.core.StringsCatalog
import com.example.core.authStateFlow
import com.example.data.model.UiState
import com.example.data.preferences.LanguagePreferences
import com.example.data.repository.MarketplaceRepository
import com.example.ui.screens.AiConciergeScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.BookingsScreen
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.MapDiscoveryScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProviderDetailScreen
import com.example.ui.theme.TrustCraftTheme
import com.example.ui.viewmodel.AiAssistantViewModel
import com.example.ui.viewmodel.MarketplaceViewModel
import com.example.ui.viewmodel.TtsUiState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

const val ROUTE_DISCOVER = "discover"
const val ROUTE_MAP = "map"
const val ROUTE_CONCIERGE = "concierge"
const val ROUTE_BOOKINGS = "bookings"
const val ROUTE_PROFILE = "profile"
const val ROUTE_PROVIDER_DETAIL = "provider_detail/{providerId}"

private data class NavDestinationItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun TrustCraftApp(auth: FirebaseAuth = Firebase.auth) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val languagePrefs = remember(context) { LanguagePreferences(context.applicationContext) }
    val selectedLanguage by languagePrefs.selectedLanguage.collectAsStateWithLifecycle(initialValue = AppLanguage.EN)
    val strings = remember(selectedLanguage) { StringsCatalog.get(selectedLanguage) }

    val currentUser by auth.authStateFlow().collectAsStateWithLifecycle(initialValue = auth.currentUser)

    CompositionLocalProvider(LocalLayoutDirection provides selectedLanguage.layoutDirection) {
        TrustCraftTheme(isArabic = selectedLanguage == AppLanguage.AR) {
            val user = currentUser
            if (user == null) {
                AuthScreen(
                    selectedLanguage = selectedLanguage,
                    strings = strings,
                    onSelectLanguage = { lang ->
                        scope.launch { languagePrefs.setLanguage(lang) }
                    },
                    onAuthSuccess = {}
                )
            } else {
                key(user.uid) {
                    AuthenticatedMarketplaceShell(
                        currentUserId = user.uid,
                        userDisplayName = user.displayName ?: "Member",
                        userEmail = user.email,
                        selectedLanguage = selectedLanguage,
                        onSelectLanguage = { lang ->
                            scope.launch { languagePrefs.setLanguage(lang) }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthenticatedMarketplaceShell(
    currentUserId: String,
    userDisplayName: String,
    userEmail: String?,
    selectedLanguage: AppLanguage,
    onSelectLanguage: (AppLanguage) -> Unit
) {
    val marketplaceViewModel: MarketplaceViewModel = viewModel(
        key = currentUserId,
        factory = viewModelFactory {
            initializer {
                val app = checkNotNull(this[APPLICATION_KEY]) {
                    "APPLICATION_KEY missing from CreationExtras"
                }
                val databaseId = app.getString(R.string.firestore_database_id)
                val db = FirebaseFirestore.getInstance(databaseId)
                MarketplaceViewModel(MarketplaceRepository(db), currentUserId)
            }
        }
    )

    val aiViewModel: AiAssistantViewModel = viewModel(key = "ai_$currentUserId")
    val strings = remember(selectedLanguage) { StringsCatalog.get(selectedLanguage) }
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val providersState by marketplaceViewModel.providersState.collectAsStateWithLifecycle()
    val filteredProvidersState by marketplaceViewModel.filteredProvidersState.collectAsStateWithLifecycle()
    val filters by marketplaceViewModel.filters.collectAsStateWithLifecycle()
    val myBookingsState by marketplaceViewModel.myBookingsState.collectAsStateWithLifecycle()
    val providerJobsState by marketplaceViewModel.providerJobsState.collectAsStateWithLifecycle()
    val favoritesState by marketplaceViewModel.favoritesState.collectAsStateWithLifecycle()
    val userSummary by marketplaceViewModel.userSummary.collectAsStateWithLifecycle()
    val userPrivateInfo by marketplaceViewModel.userPrivateInfo.collectAsStateWithLifecycle()
    val reviewsState by marketplaceViewModel.selectedProviderReviews.collectAsStateWithLifecycle()
    val isSubmitting by marketplaceViewModel.isSubmitting.collectAsStateWithLifecycle()
    val actionMessage by marketplaceViewModel.actionMessage.collectAsStateWithLifecycle()
    val userCoordinates by marketplaceViewModel.userCoordinates.collectAsStateWithLifecycle()

    val conciergeState by aiViewModel.conciergeState.collectAsStateWithLifecycle()
    val mapsInsightsState by aiViewModel.mapsInsightsState.collectAsStateWithLifecycle()
    val ttsState by aiViewModel.ttsState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(currentUserId) {
        marketplaceViewModel.initializeSession(selectedLanguage)
    }

    LaunchedEffect(actionMessage) {
        actionMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            marketplaceViewModel.clearActionMessage()
        }
    }

    LaunchedEffect(ttsState) {
        val err = ttsState as? TtsUiState.Error
        if (err != null) {
            snackbarHostState.showSnackbar(err.message)
            aiViewModel.clearTtsError()
        }
    }

    val favoriteIds = remember(favoritesState) {
        (favoritesState as? UiState.Success)?.data?.map { it.providerId }?.toSet() ?: emptySet()
    }

    val navItems = remember(strings) {
        listOf(
            NavDestinationItem(ROUTE_DISCOVER, strings.navDiscover, Icons.Filled.Explore, Icons.Outlined.Explore),
            NavDestinationItem(ROUTE_MAP, strings.navMap, Icons.Filled.Map, Icons.Outlined.Map),
            NavDestinationItem(ROUTE_CONCIERGE, strings.navConcierge, Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
            NavDestinationItem(ROUTE_BOOKINGS, strings.navBookings, Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth),
            NavDestinationItem(ROUTE_PROFILE, strings.navProfile, Icons.Filled.Person, Icons.Outlined.Person)
        )
    }

    val showTopLevelNav = currentRoute != ROUTE_PROVIDER_DETAIL

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useRail = maxWidth >= 720.dp

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (showTopLevelNav && !useRail) {
                    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                        navItems.forEach { item ->
                            val selected = currentRoute == item.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    if (!selected) {
                                        navController.navigate(item.route) {
                                            popUpTo(ROUTE_DISCOVER) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label
                                    )
                                },
                                label = {
                                    Text(item.label, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (showTopLevelNav && useRail) {
                    NavigationRail(containerColor = MaterialTheme.colorScheme.surface) {
                        navItems.forEach { item ->
                            val selected = currentRoute == item.route
                            NavigationRailItem(
                                selected = selected,
                                onClick = {
                                    if (!selected) {
                                        navController.navigate(item.route) {
                                            popUpTo(ROUTE_DISCOVER) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label
                                    )
                                },
                                label = { Text(item.label, maxLines = 1) }
                            )
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = ROUTE_DISCOVER
                    ) {
                        composable(ROUTE_DISCOVER) {
                            DiscoverScreen(
                                providersState = filteredProvidersState,
                                filters = filters,
                                favoriteIds = favoriteIds,
                                ttsState = ttsState,
                                strings = strings,
                                language = selectedLanguage,
                                isSubmitting = isSubmitting,
                                userCoordinates = userCoordinates,
                                onSelectLanguage = { lang ->
                                    onSelectLanguage(lang)
                                    marketplaceViewModel.syncLanguageToProfile(lang)
                                },
                                onSearchQueryChange = marketplaceViewModel::updateSearchQuery,
                                onSelectContinent = marketplaceViewModel::selectContinent,
                                onSelectCategory = marketplaceViewModel::selectCategory,
                                onSelectServiceKind = marketplaceViewModel::selectServiceKind,
                                onProviderClick = { provider ->
                                    navController.navigate("provider_detail/${provider.id}")
                                },
                                onToggleFavorite = marketplaceViewModel::toggleFavorite,
                                onSpeakBio = { provider ->
                                    val spoken = "${provider.businessName}. ${provider.city}, ${provider.country}. ${provider.bio}"
                                    aiViewModel.speakText("provider_${provider.id}", spoken, selectedLanguage)
                                },
                                onCreateBooking = marketplaceViewModel::createBooking,
                                onPublishProvider = marketplaceViewModel::publishProvider
                            )
                        }

                        composable(ROUTE_MAP) {
                            MapDiscoveryScreen(
                                providersState = filteredProvidersState,
                                selectedContinent = filters.selectedContinent,
                                mapsInsightsState = mapsInsightsState,
                                strings = strings,
                                language = selectedLanguage,
                                onSelectContinent = marketplaceViewModel::selectContinent,
                                onUpdateCoordinates = marketplaceViewModel::updateUserCoordinates,
                                onProviderClick = { provider ->
                                    navController.navigate("provider_detail/${provider.id}")
                                },
                                onRequestMapsInsights = { lat, lng, city, tradeLabel ->
                                    aiViewModel.fetchNearbyMapsInsights(
                                        latitude = lat,
                                        longitude = lng,
                                        city = city,
                                        tradeLabel = tradeLabel,
                                        language = selectedLanguage
                                    )
                                }
                            )
                        }

                        composable(ROUTE_CONCIERGE) {
                            AiConciergeScreen(
                                conciergeState = conciergeState,
                                ttsState = ttsState,
                                strings = strings,
                                language = selectedLanguage,
                                onAskQuery = { query ->
                                    aiViewModel.askSearchGroundedConcierge(
                                        query = query,
                                        language = selectedLanguage,
                                        cityContext = userSummary?.preferredCity
                                    )
                                },
                                onSpeakText = { tag, text ->
                                    aiViewModel.speakText(tag, text, selectedLanguage)
                                }
                            )
                        }

                        composable(ROUTE_BOOKINGS) {
                            BookingsScreen(
                                myBookingsState = myBookingsState,
                                providerJobsState = providerJobsState,
                                ttsState = ttsState,
                                strings = strings,
                                language = selectedLanguage,
                                onUpdateStatus = marketplaceViewModel::updateBookingStatus,
                                onSpeakSummary = { tag, text ->
                                    aiViewModel.speakText(tag, text, selectedLanguage)
                                }
                            )
                        }

                        composable(ROUTE_PROFILE) {
                            ProfileScreen(
                                userDisplayName = userDisplayName,
                                userEmail = userEmail,
                                userSummary = userSummary,
                                userPrivateInfo = userPrivateInfo,
                                favoritesState = favoritesState,
                                selectedLanguage = selectedLanguage,
                                strings = strings,
                                isSubmitting = isSubmitting,
                                onSelectLanguage = { lang ->
                                    onSelectLanguage(lang)
                                    marketplaceViewModel.syncLanguageToProfile(lang)
                                },
                                onSavePrivateContact = marketplaceViewModel::savePrivateContactInfo,
                                onOpenFavoriteProvider = { providerId ->
                                    navController.navigate("provider_detail/$providerId")
                                },
                                onSignOutComplete = {
                                    aiViewModel.stopAudio()
                                }
                            )
                        }

                        composable(
                            route = ROUTE_PROVIDER_DETAIL,
                            arguments = listOf(navArgument("providerId") { type = NavType.StringType })
                        ) { entry ->
                            val providerId = entry.arguments?.getString("providerId").orEmpty()
                            val allProviders = (providersState as? UiState.Success)?.data.orEmpty()
                            val provider = allProviders.firstOrNull { it.id == providerId }
                            if (provider != null) {
                                val isFav = favoriteIds.contains(provider.id)
                                ProviderDetailScreen(
                                    provider = provider,
                                    reviewsState = reviewsState,
                                    isFavorite = isFav,
                                    ttsState = ttsState,
                                    strings = strings,
                                    language = selectedLanguage,
                                    isSubmitting = isSubmitting,
                                    onSelectForReviews = marketplaceViewModel::selectProviderForReviews,
                                    onBack = { navController.popBackStack() },
                                    onToggleFavorite = { marketplaceViewModel.toggleFavorite(provider, isFav) },
                                    onSpeakBio = {
                                        val spoken = "${provider.businessName}. ${provider.city}, ${provider.country}. ${provider.bio}"
                                        aiViewModel.speakText("provider_${provider.id}", spoken, selectedLanguage)
                                    },
                                    onCreateBooking = marketplaceViewModel::createBooking,
                                    onSubmitReview = marketplaceViewModel::submitReview
                                )
                            } else {
                                LaunchedEffect(Unit) { navController.popBackStack() }
                            }
                        }
                    }
                }
            }
        }
    }
}
