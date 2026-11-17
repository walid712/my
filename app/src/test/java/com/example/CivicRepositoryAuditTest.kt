package com.example

import com.example.data.MoroccanCivicRepository
import com.example.model.AppLanguage
import com.example.model.feeForDisplay
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CivicRepositoryAuditTest {
    private val repository = MoroccanCivicRepository()

    @Test
    fun emergencyNumbersAreLimitedToDocumentedServices() {
        assertEquals(setOf("19", "177", "15", "112"), repository.emergencyServices.map { it.number }.toSet())
    }

    @Test
    fun emergencyNumbers19_177_15AreListedByAnrt() {
        val confirmed = repository.emergencyServices.filter { it.number in setOf("19", "177", "15") }
        assertEquals(3, confirmed.size)
        assertTrue(confirmed.all { it.sourceUrl == "https://www.anrt.ma/infos-pratiques/que-sont-les-numeros-speciaux" })
    }

    @Test
    fun emergencyNumberEvidenceHasIndependentReviewDate() {
        assertTrue(repository.emergencyServices.all { it.lastIndependentlyVerified == "2026-11-17" })
        val mobile = repository.emergencyServices.single { it.number == "112" }
        assertTrue(mobile.sourceUrl.contains("Juin%2B22-2026"))
        assertTrue(mobile.verifiedAt.ar.contains("لا نعمم"))
    }

    @Test
    fun officialSourceUrlsUseHttps() {
        assertTrue(repository.officialSources.all { it.url.startsWith("https://") })
    }

    @Test
    fun everyProcedureHasAnOfficialSourceAndNoInventedVerificationDate() {
        assertTrue(repository.procedures.isNotEmpty())
        assertTrue(repository.procedures.all { it.sourceUrl.startsWith("https://") })
        // A null date is explicit: a source URL alone is not proof that the details were reviewed.
        assertTrue(repository.procedures.all { it.lastIndependentlyVerified == null })
    }

    @Test
    fun procedureIdsAreUniqueAndRequiredFieldsArePresent() {
        assertEquals(repository.procedures.size, repository.procedures.map { it.id }.toSet().size)
        assertTrue(repository.procedures.all { it.title.ar.isNotBlank() && it.title.fr.isNotBlank() })
        assertTrue(repository.procedures.all { it.sourceName.ar.isNotBlank() && it.sourceName.fr.isNotBlank() })
    }

    @Test
    fun unreviewedFeesAreNotPresentedAsConfirmedFacts() {
        assertTrue(repository.procedures.all { it.feeForDisplay(AppLanguage.AR).contains("غير متحقق") })
        assertTrue(repository.procedures.all { it.feeForDisplay(AppLanguage.FR).contains("Non vérifié") })
    }

    @Test
    fun officialSourcesHaveExplicitPendingReviewDates() {
        assertTrue(repository.officialSources.isNotEmpty())
        assertTrue(repository.officialSources.all { it.lastIndependentlyVerified == null })
    }
}
