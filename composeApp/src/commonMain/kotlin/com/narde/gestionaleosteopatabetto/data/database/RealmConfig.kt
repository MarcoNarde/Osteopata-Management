package com.narde.gestionaleosteopatabetto.data.database

import com.narde.gestionaleosteopatabetto.data.database.models.*
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

/**
 * Realm database configuration and initialization
 * Provides centralized access to the Realm database instance
 */
object RealmConfig {
    
    /**
     * Realm database instance
     * Initialized with patient management schema
     */
    val realm: Realm by lazy {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                Patient::class,
                DatiPersonali::class,
                Indirizzo::class,
                Privacy::class,
                Genitori::class,
                Padre::class,
                Madre::class,
                MedicoCurante::class,
                // Clinical history models
                StoriaClinica::class,
                PatologieCroniche::class,
                AllergieFarmaci::class,
                Diabete::class,
                AltraPatologia::class,
                StileVita::class,
                Tabagismo::class,
                AttivitaSportiva::class,
                TerapiaFarmacologica::class,
                InterventoTrauma::class,
                EsameStrumentale::class,
                AnamnesiPediatrica::class,
                Gravidanza::class,
                Parto::class,
                Sviluppo::class,
                ProblemaSignificativo::class,
                // Visit models
                Visita::class,
                DatiVisitaCorrente::class,
                MotivoConsulto::class,
                MotivoPrincipale::class,
                MotivoSecondario::class
            )
        )
        .name("gestionale_osteopata.realm") // Database file name
        .schemaVersion(4) // Incremented schema version to handle visit models
        .deleteRealmIfMigrationNeeded() // Delete old database if migration is required
        .build()
        
        println("RealmConfig: Initializing Realm database with schema version 4")
        Realm.open(config)
    }
    
    /**
     * Close the Realm database
     * Call this when the app is shutting down
     */
    fun close() {
        if (!realm.isClosed()) {
            realm.close()
        }
    }
}