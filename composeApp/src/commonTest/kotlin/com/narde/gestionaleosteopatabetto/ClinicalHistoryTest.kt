package com.narde.gestionaleosteopatabetto

import com.narde.gestionaleosteopatabetto.data.database.utils.createDatabaseUtils
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Tests for clinical history functionality
 * Verifies that clinical history data can be created and contains expected information
 */
class ClinicalHistoryTest {

    @Test
    fun testCreateSampleClinicalHistory() {
        val databaseUtils = createDatabaseUtils()
        val clinicalHistory = databaseUtils.createSampleClinicalHistory()
        
        // Verify that clinical history was created
        assertNotNull(clinicalHistory, "Clinical history should not be null")
        
        // Test chronic conditions
        assertNotNull(clinicalHistory.patologieCroniche, "Chronic conditions should not be null")
        assertEquals(true, clinicalHistory.patologieCroniche?.allergieFarmaci?.presente, "Drug allergies should be present")
        assertEquals(3, clinicalHistory.patologieCroniche?.allergieFarmaci?.listaAllergie?.size, "Should have 3 drug allergies")
        assertTrue(clinicalHistory.patologieCroniche?.allergieFarmaci?.listaAllergie?.contains("Penicillina") == true, "Should contain Penicillina allergy")
        
        // Test diabetes information
        assertEquals(true, clinicalHistory.patologieCroniche?.diabete?.presente, "Diabetes should be present")
        assertEquals("2", clinicalHistory.patologieCroniche?.diabete?.tipologia, "Diabetes type should be 2")
        
        // Test lifestyle factors
        assertNotNull(clinicalHistory.stileVita, "Lifestyle factors should not be null")
        assertEquals("sedentario", clinicalHistory.stileVita?.lavoro, "Work type should be sedentary")
        assertEquals("Programmatore informatico", clinicalHistory.stileVita?.professione, "Profession should be programmer")
        
        // Test smoking information
        assertNotNull(clinicalHistory.stileVita?.tabagismo, "Smoking information should not be null")
        assertEquals("<10", clinicalHistory.stileVita?.tabagismo?.stato, "Smoking status should be <10")
        assertEquals(5, clinicalHistory.stileVita?.tabagismo?.sigaretteGiorno, "Should smoke 5 cigarettes per day")
        
        // Test physical activity
        assertNotNull(clinicalHistory.stileVita?.attivitaSportiva, "Physical activity should not be null")
        assertEquals("amatoriale", clinicalHistory.stileVita?.attivitaSportiva?.livello, "Activity level should be amateur")
        assertEquals(1, clinicalHistory.stileVita?.attivitaSportiva?.sport?.size, "Should have 1 sport")
        assertTrue(clinicalHistory.stileVita?.attivitaSportiva?.sport?.contains("Calcetto") == true, "Should include Calcetto")
        
        // Test pharmacological therapies
        assertEquals(1, clinicalHistory.terapieFarmacologiche.size, "Should have 1 pharmacological therapy")
        assertEquals("Ramipril", clinicalHistory.terapieFarmacologiche[0].farmaco, "Medication should be Ramipril")
        assertEquals("5mg", clinicalHistory.terapieFarmacologiche[0].dosaggio, "Dosage should be 5mg")
        
        // Test past interventions
        assertEquals(1, clinicalHistory.interventiTraumi.size, "Should have 1 past intervention")
        assertEquals("INT_001", clinicalHistory.interventiTraumi[0].id, "Intervention ID should be INT_001")
        assertEquals("trauma", clinicalHistory.interventiTraumi[0].tipo, "Intervention type should be trauma")
        
        // Test diagnostic tests
        assertEquals(1, clinicalHistory.esamiStrumentali.size, "Should have 1 diagnostic test")
        assertEquals("ESAME_001", clinicalHistory.esamiStrumentali[0].id, "Test ID should be ESAME_001")
        assertEquals("RMN", clinicalHistory.esamiStrumentali[0].tipo, "Test type should be RMN")
        
        // Test pediatric history
        assertNotNull(clinicalHistory.anamnesiPediatrica, "Pediatric history should not be null")
        assertEquals("naturale", clinicalHistory.anamnesiPediatrica?.parto?.tipo, "Birth type should be natural")
        assertEquals(3200, clinicalHistory.anamnesiPediatrica?.parto?.pesoNascitaGrammi, "Birth weight should be 3200g")
        assertEquals(9, clinicalHistory.anamnesiPediatrica?.parto?.punteggioApgar5min, "APGAR score should be 9")
        
        println("✅ All clinical history tests passed successfully")
    }
} 