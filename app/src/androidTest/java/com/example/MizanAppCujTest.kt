package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextReplacement
import com.example.viewmodel.currentFormattedDate
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class MizanAppCujTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun cuj_language_toggle_and_templates_builder() {
        composeRule.waitForIdle()

        // Home screen visible
        composeRule.onNodeWithTag("home_card_templates").performScrollTo().assertIsDisplayed()

        // Toggle language AR -> FR and back
        composeRule.onNodeWithTag("language_toggle_button").performClick()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithText("Mizan Maroc").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Mizan Maroc").assertIsDisplayed()

        composeRule.onNodeWithTag("language_toggle_button").performClick()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithText("ميزان المغرب").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("ميزان المغرب").assertIsDisplayed()

        // Navigate to Templates and test filling + dynamic date + copying a letter
        composeRule.onNodeWithTag("nav_templates").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("templates_lazy_list")
            .performScrollToNode(hasTestTag("tmpl_input_name"))
        composeRule.onNodeWithTag("tmpl_input_name").performTextReplacement("يوسف العلمي")
        composeRule.onNodeWithTag("tmpl_input_city").performTextReplacement("مراكش")
        composeRule.waitForIdle()

        val expectedToday = currentFormattedDate()
        composeRule.onNodeWithTag("templates_lazy_list")
            .performScrollToNode(hasTestTag("tmpl_preview_text"))
        composeRule.onNode(hasTestTag("tmpl_preview_text") and hasText(expectedToday, substring = true))
            .assertIsDisplayed()

        composeRule.onNodeWithTag("templates_lazy_list")
            .performScrollToNode(hasTestTag("tmpl_copy_button"))
        composeRule.onNodeWithTag("tmpl_copy_button").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("تم النسخ!").assertIsDisplayed()

        // Clear fields and verify today's dynamic date remains in preview
        composeRule.onNodeWithTag("templates_lazy_list")
            .performScrollToNode(hasTestTag("tmpl_clear_fields"))
        composeRule.onNodeWithTag("tmpl_clear_fields").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("templates_lazy_list")
            .performScrollToNode(hasTestTag("tmpl_preview_text"))
        composeRule.onNode(hasTestTag("tmpl_preview_text") and hasText(expectedToday, substring = true))
            .assertIsDisplayed()
    }

    @Test
    fun cuj_procedures_cities_lookup_and_official_sources() {
        composeRule.waitForIdle()

        // Navigate to Procedures
        composeRule.onNodeWithTag("nav_procedures").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("procedure_card_cnie").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("procedure_detail_source").assertIsDisplayed()
        composeRule.onNodeWithTag("procedure_detail_verified_date").assertIsDisplayed()
        composeRule.onNodeWithTag("doc_checkbox_cnie_1").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("procedure_detail_back").performClick()
        composeRule.waitForIdle()

        // Navigate to Cities & Services
        composeRule.onNodeWithTag("nav_services").performClick()
        composeRule.waitForIdle()

        // Search car matricule 26 -> Marrakech
        composeRule.onNodeWithTag("city_search_input").performTextReplacement("26")
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("city_item_marrakech_menara").assertIsDisplayed()
        composeRule.onNodeWithTag("city_search_input").performTextReplacement("")
        composeRule.waitForIdle()

        // Scroll to the confirmed Police emergency contact, Last Updated, Official Sources & Disclaimer
        composeRule.onNodeWithTag("services_lazy_list")
            .performScrollToNode(hasTestTag("emergency_item_police"))
        composeRule.onNodeWithTag("emergency_item_police").assertIsDisplayed()

        composeRule.onNodeWithTag("services_lazy_list")
            .performScrollToNode(hasTestTag("last_updated_card"))
        composeRule.onNodeWithTag("last_updated_card").assertIsDisplayed()

        composeRule.onNodeWithTag("services_lazy_list")
            .performScrollToNode(hasTestTag("official_source_idarati"))
        composeRule.onNodeWithTag("official_source_idarati").assertIsDisplayed()

        composeRule.onNodeWithTag("services_lazy_list")
            .performScrollToNode(hasTestTag("disclaimer_card"))
        composeRule.onNodeWithTag("disclaimer_card").assertIsDisplayed()
    }
    @Test
    fun legalPrivacyAndTermsPage_isAccessibleInArabicAndFrench() {
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("nav_legal").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("الخصوصية والشروط").assertIsDisplayed()
        composeRule.onNodeWithTag("legal_privacy_title").assertIsDisplayed()
        composeRule.onNodeWithTag("legal_terms_title").performScrollTo().assertIsDisplayed()

        composeRule.onNodeWithTag("nav_home").performClick()
        composeRule.onNodeWithTag("language_toggle_button").performClick()
        composeRule.onNodeWithTag("nav_legal").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Confidentialité et conditions").assertIsDisplayed()
        composeRule.onNodeWithTag("legal_privacy_title").assertIsDisplayed()
        composeRule.onNodeWithTag("legal_terms_title").performScrollTo().assertIsDisplayed()
    }

}
