package com.example.ui.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.model.AppLanguage
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LegalInfoScreen
import com.example.ui.screens.ProcedureDetailScreen
import com.example.ui.screens.ProceduresScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.screens.TemplatesScreen
import com.example.viewmodel.MizanUiState
import com.example.viewmodel.MizanViewModel

object MizanRoutes {
    const val ROUTE_HOME = "home"
    const val ROUTE_TEMPLATES = "templates"
    const val ROUTE_PROCEDURES = "procedures"
    const val ROUTE_PROCEDURE_DETAIL = "procedure_detail/{procedureId}"
    const val ROUTE_SERVICES = "services"
    const val ROUTE_LEGAL = "legal_info"

    fun procedureDetail(procedureId: String) = "procedure_detail/$procedureId"
}

private data class BottomNavTab(
    val route: String,
    val labelAr: String,
    val labelFr: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

private val bottomTabs = listOf(
    BottomNavTab(
        route = MizanRoutes.ROUTE_HOME,
        labelAr = "الرئيسية",
        labelFr = "Accueil",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        testTag = "nav_home"
    ),
    BottomNavTab(
        route = MizanRoutes.ROUTE_TEMPLATES,
        labelAr = "النماذج",
        labelFr = "Modèles",
        selectedIcon = Icons.Filled.EditNote,
        unselectedIcon = Icons.Outlined.EditNote,
        testTag = "nav_templates"
    ),
    BottomNavTab(
        route = MizanRoutes.ROUTE_PROCEDURES,
        labelAr = "الوثائق",
        labelFr = "Dossiers",
        selectedIcon = Icons.Filled.Description,
        unselectedIcon = Icons.Outlined.Description,
        testTag = "nav_procedures"
    ),
    BottomNavTab(
        route = MizanRoutes.ROUTE_SERVICES,
        labelAr = "المدن والطوارئ",
        labelFr = "Villes & Urg.",
        selectedIcon = Icons.Filled.LocationCity,
        unselectedIcon = Icons.Outlined.LocationCity,
        testTag = "nav_services"
    ),
    BottomNavTab(
        route = MizanRoutes.ROUTE_LEGAL,
        labelAr = "الخصوصية والشروط",
        labelFr = "Infos légales",
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
        testTag = "nav_legal"
    )
)

@Composable
fun MizanAppShell(
    uiState: MizanUiState,
    viewModel: MizanViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isDetailRoute = currentDestination?.route == MizanRoutes.ROUTE_PROCEDURE_DETAIL
    val isAr = uiState.language == AppLanguage.AR

    fun navigateTopLevel(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavRail = maxWidth >= 680.dp

        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                if (!useNavRail && !isDetailRoute) {
                    NavigationBar(
                        windowInsets = WindowInsets.navigationBars,
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ) {
                        bottomTabs.forEach { tab ->
                            val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                            NavigationBarItem(
                                selected = selected,
                                onClick = { navigateTopLevel(tab.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = if (isAr) tab.labelAr else tab.labelFr
                                    )
                                },
                                label = {
                                    Text(
                                        text = if (isAr) tab.labelAr else tab.labelFr,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                modifier = Modifier.testTag(tab.testTag)
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
                if (useNavRail && !isDetailRoute) {
                    NavigationRail(
                        modifier = Modifier
                            .fillMaxHeight()
                            .statusBarsPadding(),
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        bottomTabs.forEach { tab ->
                            val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateTopLevel(tab.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = if (isAr) tab.labelAr else tab.labelFr
                                    )
                                },
                                label = {
                                    Text(
                                        text = if (isAr) tab.labelAr else tab.labelFr,
                                        maxLines = 1
                                    )
                                },
                                modifier = Modifier.testTag(tab.testTag)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = MizanRoutes.ROUTE_HOME,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .statusBarsPadding()
                ) {
                    composable(MizanRoutes.ROUTE_HOME) {
                        HomeScreen(
                            uiState = uiState,
                            onToggleLanguage = viewModel::toggleLanguage,
                            onSearchQueryChange = viewModel::updateHomeSearchQuery,
                            onOpenTemplates = {
                                navigateTopLevel(MizanRoutes.ROUTE_TEMPLATES)
                            },
                            onOpenProcedureDetail = { id ->
                                navController.navigate(MizanRoutes.procedureDetail(id))
                            },
                            onOpenAllProcedures = {
                                navigateTopLevel(MizanRoutes.ROUTE_PROCEDURES)
                            },
                            onOpenCitiesAndServices = {
                                navigateTopLevel(MizanRoutes.ROUTE_SERVICES)
                            }
                        )
                    }

                    composable(MizanRoutes.ROUTE_TEMPLATES) {
                        TemplatesScreen(
                            uiState = uiState,
                            viewModel = viewModel
                        )
                    }

                    composable(MizanRoutes.ROUTE_PROCEDURES) {
                        ProceduresScreen(
                            uiState = uiState,
                            onToggleLanguage = viewModel::toggleLanguage,
                            onSearchChange = viewModel::updateProcedureSearchQuery,
                            onSelectCategory = viewModel::selectProcedureCategory,
                            onOpenProcedureDetail = { id ->
                                navController.navigate(MizanRoutes.procedureDetail(id))
                            }
                        )
                    }

                    composable(
                        route = MizanRoutes.ROUTE_PROCEDURE_DETAIL,
                        arguments = listOf(navArgument("procedureId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val procedureId = backStackEntry.arguments?.getString("procedureId") ?: ""
                        val guide = viewModel.getProcedureById(procedureId)
                        if (guide != null) {
                            ProcedureDetailScreen(
                                guide = guide,
                                language = uiState.language,
                                checkedDocuments = uiState.checkedDocuments,
                                onToggleDocument = viewModel::toggleDocumentChecked,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }

                    composable(MizanRoutes.ROUTE_SERVICES) {
                        ServicesScreen(
                            uiState = uiState,
                            viewModel = viewModel
                        )
                    }

                    composable(MizanRoutes.ROUTE_LEGAL) {
                        LegalInfoScreen(language = uiState.language)
                    }
                }
            }
        }
    }
}
