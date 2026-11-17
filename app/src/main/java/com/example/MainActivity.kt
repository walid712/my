package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.MoroccanCivicRepository
import com.example.data.UserPreferencesRepository
import com.example.model.AppLanguage
import com.example.model.ThemePreference
import com.example.ui.navigation.MizanAppShell
import com.example.ui.theme.MizanMarocTheme
import com.example.viewmodel.MizanViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefsRepo = UserPreferencesRepository(applicationContext)
        val civicRepo = MoroccanCivicRepository()
        val factory = MizanViewModel.Factory(prefsRepo, civicRepo)

        setContent {
            val viewModel: MizanViewModel = viewModel(factory = factory)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            val useDarkTheme = when (uiState.themePreference) {
                ThemePreference.LIGHT -> false
                ThemePreference.DARK -> true
                ThemePreference.SYSTEM -> isSystemInDarkTheme()
            }

            val layoutDirection = if (uiState.language == AppLanguage.AR) {
                LayoutDirection.Rtl
            } else {
                LayoutDirection.Ltr
            }

            MizanMarocTheme(darkTheme = useDarkTheme) {
                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    MizanAppShell(uiState = uiState, viewModel = viewModel)
                }
            }
        }
    }
}
