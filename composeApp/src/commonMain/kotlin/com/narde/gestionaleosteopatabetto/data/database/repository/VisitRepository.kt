package com.narde.gestionaleosteopatabetto.data.database.repository

import com.narde.gestionaleosteopatabetto.data.database.RealmConfig
import com.narde.gestionaleosteopatabetto.data.database.models.Visita
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow

/**
 * Repository for Visit database operations
 * Provides a clean API for visit data access and management
 */
class VisitRepository : VisitRepositoryInterface {
    
    private val realm = RealmConfig.realm
    
    /**
     * Get all visits as a Flow for reactive UI updates
     * Ordered by visit date (most recent first)
     */
    override fun getAllVisitsFlow(): Flow<ResultsChange<Visita>> {
        return realm.query<Visita>()
            .sort("dataVisita", Sort.DESCENDING)
            .asFlow()
    }
    
    /**
     * Get all visits as a list (one-time query)
     */
    override fun getAllVisits(): List<Visita> {
        return realm.query<Visita>()
            .sort("dataVisita", Sort.DESCENDING)
            .find()
    }
    
    /**
     * Find visit by ID
     */
    override fun getVisitById(visitId: String): Visita? {
        return realm.query<Visita>("idVisita == $0", visitId).first().find()
    }
    
    /**
     * Get visits for a specific patient
     */
    override fun getVisitsByPatientId(patientId: String): List<Visita> {
        return realm.query<Visita>("idPaziente == $0", patientId)
            .sort("dataVisita", Sort.DESCENDING)
            .find()
    }
    
    /**
     * Get visits by date range
     */
    override fun getVisitsByDateRange(startDate: String, endDate: String): List<Visita> {
        return realm.query<Visita>("dataVisita >= $0 AND dataVisita <= $1", startDate, endDate)
            .sort("dataVisita", Sort.DESCENDING)
            .find()
    }
    
    /**
     * Get visits by osteopath
     */
    override fun getVisitsByOsteopath(osteopath: String): List<Visita> {
        return realm.query<Visita>("osteopata == $0", osteopath)
            .sort("dataVisita", Sort.DESCENDING)
            .find()
    }
    
    /**
     * Search visits by patient ID or visit ID
     */
    override fun searchVisits(searchTerm: String): List<Visita> {
        return realm.query<Visita>("idPaziente CONTAINS[c] $0 OR idVisita CONTAINS[c] $0", searchTerm)
            .sort("dataVisita", Sort.DESCENDING)
            .find()
    }
    
    /**
     * Save or update a visit
     * If visit exists (same ID), it will be updated
     * If visit is new, it will be created
     */
    override suspend fun saveVisit(visit: Visita) {
        realm.write {
            copyToRealm(visit, updatePolicy = io.realm.kotlin.UpdatePolicy.ALL)
        }
    }
    
    /**
     * Delete a visit by ID
     */
    override suspend fun deleteVisit(visitId: String) {
        realm.write {
            val visit = query<Visita>("idVisita == $0", visitId).first().find()
            visit?.let { delete(it) }
        }
    }
    
    /**
     * Get visits count
     */
    override fun getVisitsCount(): Long {
        return realm.query<Visita>().count().find()
    }
    
    /**
     * Get visits count for a specific patient
     */
    override fun getVisitsCountByPatient(patientId: String): Long {
        return realm.query<Visita>("idPaziente == $0", patientId).count().find()
    }
    
    /**
     * Delete all visits from the database
     * Use with caution - this will permanently remove all visit data
     */
    override suspend fun deleteAllVisits() {
        realm.write {
            val allVisits = query<Visita>().find()
            delete(allVisits)
        }
    }
    
    /**
     * Delete all visits for a specific patient
     */
    override suspend fun deleteVisitsByPatient(patientId: String) {
        realm.write {
            val patientVisits = query<Visita>("idPaziente == $0", patientId).find()
            delete(patientVisits)
        }
    }
}

