package com.narde.gestionaleosteopatabetto.data.database.utils

import com.narde.gestionaleosteopatabetto.data.database.models.*
import com.narde.gestionaleosteopatabetto.data.model.Patient as UIPatient
import com.narde.gestionaleosteopatabetto.data.model.Visit as UIVisit
import io.realm.kotlin.ext.realmListOf
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Database utility operations implementation
 */
class DatabaseUtils : DatabaseUtilsInterface {
    
    /**
     * Create a sample patient for testing/development
     */
    override fun createSamplePatient(): Patient {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        
        return Patient().apply {
            idPaziente = "PAT001"
            datiPersonali = DatiPersonali().apply {
                nome = "Mario"
                cognome = "Rossi"
                dataNascita = "1985-06-15" // Database stores in ISO format
                sesso = "M"
                luogoNascita = "Roma"
                codiceFiscale = "RSSMRA85H15H501Z"
                telefonoPaziente = "+39 333 123 4567"
                emailPaziente = "mario.rossi@email.com"
            }
            indirizzo = Indirizzo().apply {
                via = "Via Roma 123"
                citta = "Roma"
                cap = "00100"
                provincia = "RM"
                nazione = "Italia"
                tipoIndirizzo = "Residenza"
            }
            privacy = Privacy().apply {
                consensoTrattamento = true
                consensoMarketing = false
                consensoTerzeparti = false
                dataConsenso = currentDate.toString()
            }
        }
    }
    
    /**
     * Generate a patient ID based on patient count
     */
    override fun generatePatientId(patientCount: Long): String {
        val nextId = patientCount + 1
        return "PAT${nextId.toString().padStart(3, '0')}"
    }
    
    /**
     * Create a new empty patient with given ID
     */
    override fun createNewPatient(patientId: String): Patient {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        
        return Patient().apply {
            this.idPaziente = patientId
            datiPersonali = DatiPersonali()
            indirizzo = Indirizzo()
            privacy = Privacy().apply {
                dataConsenso = currentDate.toString()
            }
            genitori = Genitori().apply {
                padre = Padre()
                madre = Madre()
            }
            medicocurante = MedicoCurante()
        }
    }
    
    /**
     * Convert database patient to UI patient
     */
    override fun toUIPatient(databasePatient: Patient): UIPatient {
        // Calculate age from birth date (database stores in ISO format)
        val age = databasePatient.datiPersonali?.dataNascita?.takeIf { it.isNotEmpty() }?.let { birthDateStr ->
            try {
                // Database stores dates in ISO format, so parse directly
                val birthDate = LocalDate.parse(birthDateStr)
                val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                
                var calculatedAge = currentDate.year - birthDate.year
                if (currentDate.monthNumber < birthDate.monthNumber || 
                    (currentDate.monthNumber == birthDate.monthNumber && currentDate.dayOfMonth < birthDate.dayOfMonth)) {
                    calculatedAge--
                }
                calculatedAge
            } catch (e: Exception) {
                0
            }
        } ?: 0
        
        // Convert parent information if available
        val parentInfo = databasePatient.genitori?.let { genitori ->
            val father = genitori.padre?.let { padre ->
                if (padre.nome.isNotEmpty() || padre.cognome.isNotEmpty()) {
                    com.narde.gestionaleosteopatabetto.data.model.Parent(
                        firstName = padre.nome,
                        lastName = padre.cognome,
                        phone = padre.telefono,
                        email = padre.email,
                        profession = padre.professione
                    )
                } else null
            }
            
            val mother = genitori.madre?.let { madre ->
                if (madre.nome.isNotEmpty() || madre.cognome.isNotEmpty()) {
                    com.narde.gestionaleosteopatabetto.data.model.Parent(
                        firstName = madre.nome,
                        lastName = madre.cognome,
                        phone = madre.telefono,
                        email = madre.email,
                        profession = madre.professione
                    )
                } else null
            }
            
            if (father != null || mother != null) {
                com.narde.gestionaleosteopatabetto.data.model.ParentInfo(father, mother)
            } else null
        }
        
        return UIPatient(
            id = databasePatient.idPaziente,
            name = "${databasePatient.datiPersonali?.nome ?: ""} ${databasePatient.datiPersonali?.cognome ?: ""}".trim(),
            phone = databasePatient.datiPersonali?.telefonoPaziente ?: "",
            email = databasePatient.datiPersonali?.emailPaziente ?: "",
            age = age,
            bmi = databasePatient.datiPersonali?.bmi,
            parentInfo = parentInfo
        )
    }
    
    /**
     * Convert database visit to UI visit
     */
    override fun toUIVisit(databaseVisit: Visita): UIVisit {
        return UIVisit(
            idVisita = databaseVisit.idVisita,
            idPaziente = databaseVisit.idPaziente,
            dataVisita = databaseVisit.dataVisita,
            osteopata = databaseVisit.osteopata,
            noteGenerali = databaseVisit.noteGenerali
        )
    }
    
    /**
     * Create clinical history data from JSON structure
     * This function creates a sample clinical history based on the provided JSON structure
     */
    override fun createSampleClinicalHistory(): StoriaClinica {
        return StoriaClinica().apply {
            // Chronic conditions
            patologieCroniche = PatologieCroniche().apply {
                allergieFarmaci = AllergieFarmaci().apply {
                    presente = true
                    listaAllergie = realmListOf("Penicillina", "Aspirina")
                }
                diabete = Diabete().apply {
                    presente = true
                    tipologia = "2"
                }
                ipertiroidismo = false
                cardiopatia = false
                ipertensioneArteriosa = true
                
                // Add other chronic conditions
                altrePatologie = realmListOf(
                    AltraPatologia().apply {
                        patologia = "Artrosi cervicale"
                        dataInsorgenza = "2020-03-15"
                        stato = "in_trattamento"
                    },
                    AltraPatologia().apply {
                        patologia = "Reflusso gastroesofageo"
                        dataInsorgenza = "2019-08-22"
                        stato = "attiva"
                    }
                )
            }
            
            // Lifestyle factors
            stileVita = StileVita().apply {
                tabagismo = Tabagismo().apply {
                    stato = "<10"
                    sigaretteGiorno = 5
                    anniFumo = 3
                }
                lavoro = "sedentario"
                professione = "Programmatore informatico"
                oreLavoroGiorno = 8
                attivitaSportiva = AttivitaSportiva().apply {
                    presente = true
                    sport = realmListOf("Nuoto", "Corsa")
                    frequenza = "settimanale"
                    intensita = "media"
                }
            }
            
            // Drug therapies
            terapieFarmacologiche = realmListOf(
                TerapiaFarmacologica().apply {
                    farmaco = "Metformina"
                    dosaggio = "500mg"
                    frequenza = "2 volte al giorno"
                    dataInizio = "2021-01-15"
                    dataFine = ""
                    indicazione = "Controllo glicemia"
                },
                TerapiaFarmacologica().apply {
                    farmaco = "Ramipril"
                    dosaggio = "5mg"
                    frequenza = "1 volta al giorno"
                    dataInizio = "2020-06-10"
                    dataFine = ""
                    indicazione = "Controllo pressione arteriosa"
                }
            )
            
            // Interventions and traumas
            interventiTrauma = realmListOf(
                InterventoTrauma().apply {
                    id = "INT001"
                    tipo = "intervento_chirurgico"
                    descrizione = "Appendicectomia laparoscopica"
                    data = "2015-04-12"
                    trattamento = "Laparoscopia"
                    esito = "guarigione_completa"
                },
                InterventoTrauma().apply {
                    id = "INT002"
                    tipo = "trauma"
                    descrizione = "Frattura polso destro"
                    data = "2018-09-03"
                    trattamento = "Trattamento conservativo"
                    esito = "guarigione_completa"
                }
            )
            
            // Diagnostic tests
            esamiStrumentali = realmListOf(
                EsameStrumentale().apply {
                    id = "EX001"
                    tipo = "esami_ematochimici"
                    distretto = "Sistema metabolico"
                    data = "2023-11-15"
                    risultato = "Glicemia a digiuno: 126 mg/dl (valore borderline)"
                    struttura = "Laboratorio Analisi Roma"
                },
                EsameStrumentale().apply {
                    id = "EX002"
                    tipo = "RX"
                    distretto = "Colonna cervicale"
                    data = "2023-10-08"
                    risultato = "Artrosi C5-C6, conferma diagnosi clinica"
                    struttura = "Ospedale San Giovanni Roma"
                },
                EsameStrumentale().apply {
                    id = "EX003"
                    tipo = "esami_ematochimici"
                    distretto = "Sistema ematologico"
                    data = "2023-11-15"
                    risultato = "Emocromo completo: tutti i valori nella norma"
                    struttura = "Laboratorio Analisi Roma"
                }
            )
            
            // Pediatric history
            anamnesiPediatrica = AnamnesiPediatrica().apply {
                gravidanza = Gravidanza().apply {
                    complicazioni = false
                    note = "Gravidanza fisiologica"
                }
                parto = Parto().apply {
                    tipo = "naturale"
                    complicazioni = false
                    pesoNascitaGrammi = 3200
                    punteggioApgar5min = 9
                    note = "Parto eutocico"
                }
                sviluppo = Sviluppo().apply {
                    primiPassiMesi = 12
                    primeParoleMesi = 11
                    problemiSviluppo = false
                    note = "Sviluppo psicomotorio nella norma"
                }
                
                // Note: problemiSignificativi field doesn't exist in the current model
                // This would need to be added to the AnamnesiPediatrica model if needed
                
                noteGenerali = "Sviluppo nella norma, nessuna particolare problematica. Bambino sano con crescita regolare."
            }
        }
    }
}

/**
 * Factory function to create DatabaseUtils instance
 */
fun createDatabaseUtils(): DatabaseUtilsInterface {
    return DatabaseUtils()
}