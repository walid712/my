package com.example

import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import com.example.data.MoroccanCivicRepository
import com.example.data.UserPreferencesRepository
import com.example.viewmodel.MizanViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class MizanViewModelDateTest {
    @Test
    fun dateProviderUpdatesDynamicallyWhenDraftIsCleared() {
        var fakeDate = "10/01/2027"
        val vm = MizanViewModel(
            prefsRepo = UserPreferencesRepository(ApplicationProvider.getApplicationContext()),
            civicRepo = MoroccanCivicRepository(),
            dateProvider = { fakeDate }
        )
        assertTrue(vm.uiState.value.templateDraft.dateText == "10/01/2027")
        assertTrue(vm.uiState.value.renderedLetterText.contains("10/01/2027"))

        val collectorJob = CoroutineScope(Dispatchers.Unconfined).launch { vm.uiState.collect {} }
        try {
            fakeDate = "22/04/2027"
            vm.clearDraftFields()
            val deadline = System.currentTimeMillis() + 5000
            while (vm.uiState.value.templateDraft.dateText != "22/04/2027" &&
                System.currentTimeMillis() < deadline
            ) {
                Shadows.shadowOf(Looper.getMainLooper()).idle()
                Thread.sleep(20)
            }
            assertTrue(vm.uiState.value.templateDraft.dateText == "22/04/2027")
            assertTrue(vm.uiState.value.renderedLetterText.contains("22/04/2027"))
        } finally {
            collectorJob.cancel()
        }
    }
}
