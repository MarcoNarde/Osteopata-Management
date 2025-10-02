package com.narde.gestionaleosteopatabetto.data.database.repository

import com.narde.gestionaleosteopatabetto.data.database.models.Visita
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow

/**
 * Interface for Visit repository operations
 * Provides a clean API for visit data access and management
 */
interface VisitRepositoryInterface {
    
    /**
     * Get all visits as a Flow for reactive UI updates
     * Ordered by visit date (most recent first)
     */
    fun getAllVisitsFlow(): Flow<ResultsChange<Visita>>
    
    /**
     * Get all visits as a list (one-time query)
     */
    fun getAllVisits(): List<Visita>
    
    /**
     * Find visit by ID
     */
    fun getVisitById(visitId: String): Visita?
    
    /**
     * Get visits for a specific patient
     */
    fun getVisitsByPatientId(patientId: String): List<Visita>
    
    /**
     * Get visits by date range
     */
    fun getVisitsByDateRange(startDate: String, endDate: String): List<Visita>
    
    /**
     * Get visits by osteopath
     */
    fun getVisitsByOsteopath(osteopath: String): List<Visita>
    
    /**
     * Search visits by patient ID or visit ID
     */
    fun searchVisits(searchTerm: String): List<Visita>
    
    /**
     * Save or update a visit
     * If visit exists (same ID), it will be updated
     * If visit is new, it will be created
     */
    suspend fun saveVisit(visit: Visita)
    
    /**
     * Delete a visit by ID
     */
    suspend fun deleteVisit(visitId: String)
    
    /**
     * Get visits count
     */
    fun getVisitsCount(): Long
    
    /**
     * Get visits count for a specific patient
     */
    fun getVisitsCountByPatient(patientId: String): Long
    
    /**
     * Delete all visits from the database
     * Use with caution - this will permanently remove all visit data
     */
    suspend fun deleteAllVisits()
    
    /**
     * Delete all visits for a specific patient
     */
    suspend fun deleteVisitsByPatient(patientId: String)
}

