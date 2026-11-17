package com.example

import com.example.data.MoroccanCivicRepository
import com.example.model.AppLanguage
import com.example.viewmodel.MizanUiState
import com.example.viewmodel.TemplateDraftState
import com.example.viewmodel.currentFormattedDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MoroccanDirectoryAndTemplatesTest {

    private val repo = MoroccanCivicRepository()

    @Test
    fun all_templates_have_bilingual_content_and_dynamic_today_date() {
        assertTrue(repo.letterTemplates.size >= 8)
        repo.letterTemplates.forEach { tmpl ->
            assertTrue(tmpl.title.ar.isNotBlank())
            assertTrue(tmpl.title.fr.isNotBlank())
            assertTrue(tmpl.bodyTemplateAr.contains("{FULL_NAME}"))
            assertTrue(tmpl.bodyTemplateFr.contains("{FULL_NAME}"))
            assertTrue(tmpl.bodyTemplateAr.contains("{DATE}"))
            assertTrue(tmpl.bodyTemplateFr.contains("{DATE}"))
        }

        val expectedToday = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(Date())
        assertEquals(expectedToday, currentFormattedDate())

        val defaultDraft = TemplateDraftState()
        assertTrue(defaultDraft.dateText.matches(Regex("^\\d{2}/\\d{2}/\\d{4}$")))
        assertEquals(expectedToday, defaultDraft.dateText)

        // Verify custom dynamic date renders cleanly instead of any fixed date
        val dynamicState = MizanUiState(
            language = AppLanguage.AR,
            templateDraft = TemplateDraftState(
                selectedTemplateId = "demande_emploi",
                dateText = "05/03/2028"
            ),
            letterTemplates = repo.letterTemplates
        )
        assertTrue(dynamicState.renderedLetterText.contains("05/03/2028"))
    }

    @Test
    fun all_51_moroccan_cities_have_verified_plate_and_postal_codes() {
        assertEquals(51, repo.moroccanCities.size)
        assertEquals(51, repo.moroccanCities.map { it.id }.distinct().size)

        val expectedCities = mapOf(
            "rabat" to ("1" to "10000"),
            "sale" to ("2" to "11000"),
            "temara" to ("4" to "12000"),
            "khemisset" to ("5" to "15000"),
            "casa_anfa" to ("6" to "20000"),
            "casa_ain_sebaa" to ("7" to "20250"),
            "casa_hay_hassani" to ("8" to "20230"),
            "casa_ben_msick" to ("9" to "20700"),
            "casa_moulay_rachid" to ("10" to "20702"),
            "casa_bernoussi" to ("13" to "20600"),
            "mohammedia" to ("14" to "28800"),
            "fes_jdid" to ("15" to "30000"),
            "fes_medina" to ("16" to "30110"),
            "sefrou" to ("18" to "31000"),
            "meknes" to ("20 / 21" to "50000"),
            "ifrane" to ("23" to "53000"),
            "khenifra" to ("24" to "54000"),
            "errachidia" to ("25" to "52000"),
            "marrakech_menara" to ("26" to "40000"),
            "marrakech_medina" to ("27" to "40034"),
            "kelaa" to ("31" to "43000"),
            "essaouira" to ("32" to "44000"),
            "agadir" to ("33" to "80000"),
            "inezgane" to ("34" to "80100 / 86350"),
            "taroudant" to ("36" to "83000"),
            "tiznit" to ("37" to "85000"),
            "ouarzazate" to ("38" to "45000"),
            "zagora" to ("39" to "47900"),
            "tanger" to ("40" to "90000"),
            "larache" to ("42" to "92000"),
            "chefchaouen" to ("43" to "91000"),
            "tetouan" to ("44" to "93000"),
            "hoceima" to ("45" to "32000"),
            "taza" to ("46" to "35000"),
            "oujda" to ("48" to "60000"),
            "berkane" to ("49" to "63300"),
            "nador" to ("50" to "62000"),
            "safi" to ("54" to "46000"),
            "el_jadida" to ("55" to "24000"),
            "settat" to ("56" to "26000"),
            "khouribga" to ("57" to "25000"),
            "kenitra" to ("59" to "14000"),
            "beni_mellal" to ("61" to "23000"),
            "guelmim" to ("64" to "81000"),
            "laayoune" to ("68" to "70000"),
            "dakhla" to ("70" to "73000"),
            "mdiq_fnideq" to ("75" to "93200 / 93100"),
            "berrechid" to ("81" to "26100"),
            "benguerir" to ("83" to "43150"),
            "tinghir" to ("86" to "45800"),
            "sidi_ifni" to ("87" to "85200")
        )
        assertEquals(51, expectedCities.size)

        val actualCities = repo.moroccanCities.associate { it.id to (it.plateCode to it.postalCode) }
        assertEquals(expectedCities, actualCities)

        repo.moroccanCities.forEach { city ->
            assertTrue("Blank Arabic name for ${city.id}", city.name.ar.isNotBlank())
            assertTrue("Blank French name for ${city.id}", city.name.fr.isNotBlank())
            assertTrue("Blank Arabic region for ${city.id}", city.region.ar.isNotBlank())
            assertTrue("Blank French region for ${city.id}", city.region.fr.isNotBlank())
            assertTrue(
                "Invalid postalCode '${city.postalCode}' for ${city.id}",
                city.postalCode.matches(Regex("^\\d{5}(\\s*/\\s*\\d{5})?$"))
            )
            city.plateCode.split("/").map { it.trim().toInt() }.forEach { plateNum ->
                assertTrue("Plate number $plateNum out of range for ${city.id}", plateNum in 1..87)
            }
        }
    }

    @Test
    fun procedures_emergency_and_tips_have_verified_facts_legal_refs_and_official_sources() {
        assertTrue(repo.procedures.size >= 8)
        assertTrue(repo.officialSources.size >= 10)
        assertFalse(repo.lastUpdated.ar.isBlank())
        assertFalse(repo.lastUpdated.fr.isBlank())

        // Every procedure must include specific official source URL, legal reference, and verification date
        repo.procedures.forEach { proc ->
            assertTrue("Missing AR sourceName for ${proc.id}", proc.sourceName.ar.isNotBlank())
            assertTrue("Missing FR sourceName for ${proc.id}", proc.sourceName.fr.isNotBlank())
            assertTrue("Source URL must be https for ${proc.id}", proc.sourceUrl.startsWith("https://"))
            assertTrue("Missing AR legalReference for ${proc.id}", proc.legalReference.ar.isNotBlank())
            assertTrue("Missing FR legalReference for ${proc.id}", proc.legalReference.fr.isNotBlank())
            assertTrue("Missing AR verifiedAt for ${proc.id}", proc.verifiedAt.ar.isNotBlank())
            assertTrue("Missing FR verifiedAt for ${proc.id}", proc.verifiedAt.fr.isNotBlank())
        }
        repo.emergencyServices.forEach { service ->
            assertTrue(service.sourceName.ar.isNotBlank())
            assertTrue(service.sourceName.fr.isNotBlank())
            assertTrue(service.sourceUrl.startsWith("https://"))
            assertTrue(service.verifiedAt.ar.isNotBlank())
            assertTrue(service.verifiedAt.fr.isNotBlank())
        }
        repo.civicRightsTips.forEach { tip ->
            assertTrue(tip.sourceName.ar.isNotBlank())
            assertTrue(tip.sourceName.fr.isNotBlank())
            assertTrue(tip.sourceUrl.startsWith("https://"))
            assertTrue(tip.verifiedAt.ar.isNotBlank())
            assertTrue(tip.verifiedAt.fr.isNotBlank())
        }

        // Verify passport minor exoneration clarification + specific deep URL
        val passport = repo.procedures.first { it.id == "passport" }
        assertEquals("https://www.passeport.ma/Web/", passport.sourceUrl)
        assertTrue(passport.feeLabel.ar.contains("500 درهم"))
        assertTrue(passport.feeLabel.ar.contains("دون 18 سنة"))
        assertTrue(passport.feeLabel.fr.contains("< 18 ans"))
        assertTrue(passport.documents.any { it.label.fr.contains("1 photo") })
        assertTrue(passport.documents.any { it.label.ar.contains("الإعفاء") })

        // Verify specific legislation URLs
        val carteGrise = repo.procedures.first { it.id == "carte_grise" }
        assertEquals("https://www.mutationvehicule.ma", carteGrise.sourceUrl)

        val mariage = repo.procedures.first { it.id == "mariage" }
        assertEquals("https://adala.justice.gov.ma/", mariage.sourceUrl)
        assertTrue(mariage.documents.any { it.label.ar.contains("نسخة كاملة") })

        val residence = repo.procedures.first { it.id == "residence_naissance" }
        assertEquals("https://www.watiqa.ma/", residence.sourceUrl)

        val legalTip = repo.civicRightsTips.first { it.id == "tip_legalisation" }
        assertEquals("https://www.sgg.gov.ma/BO/", legalTip.sourceUrl)

        assertEquals(setOf("police", "gendarmerie", "protection_civile", "mobile_police"),
            repo.emergencyServices.map { it.id }.toSet())
        assertEquals(setOf("19", "177", "15", "112"),
            repo.emergencyServices.map { it.number }.toSet())

        val sos112 = repo.emergencyServices.first { it.id == "sos_112" }
        assertFalse(sos112.description.ar.contains("SIM"))
        assertFalse(sos112.description.fr.contains("SIM"))
    }
}
