package com.narde.gestionaleosteopatabetto.data.database

import com.narde.gestionaleosteopatabetto.data.database.models.*
import com.narde.gestionaleosteopatabetto.data.database.models.apparati.*
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
                MotivoSecondario::class,
                // Apparatus evaluation pattern models
                SintomoBase::class,
                SintomoIntensita::class,
                SintomoFrequenza::class,
                SintomoCompleto::class,
                CaratteristicheSintomo::class,
                Localizzazione::class,
                StoriaPatologia::class,
                // Apparatus evaluation models - Cranio
                ValutazioneApparati::class,
                ApparatoCranio::class,
                Cefalea::class,
                CaratteristicheCefalea::class,
                Emicrania::class,
                ProblemiATM::class,
                SintomiATM::class,
                ApparecchioOrtodontico::class,
                // Apparatus evaluation models - Respiratorio
                ApparatoRespiratorio::class,
                Dispnea::class,
                Tosse::class,
                AllergieRespiratorie::class,
                CongestioneNasale::class,
                Sinusite::class,
                Russare::class,
                RaffreddoriFrequenti::class,
                // Apparatus evaluation models - Cardiovascolare
                ApparatoCardiovascolare::class,
                DoloreToracico::class,
                StoriaCardiaca::class,
                InterventoCardiaco::class,
                Ipertensione::class,
                Edemi::class,
                VeneVaricose::class,
                Aritmie::class,
                FormicoliiPosizionali::class,
                // Apparatus evaluation models - Gastrointestinale
                ApparatoGastrointestinale::class,
                SintomiOrali::class,
                SintomiDigestivi::class,
                AciditaStomaco::class,
                SintomiIntestinali::class,
                Emorroidi::class,
                Alvo::class,
                AltriSintomiGI::class,
                // Apparatus evaluation models - Urinario
                ApparatoUrinario::class,
                InfezioniUrinarie::class,
                PatologieRenali::class,
                SintomiUrinari::class,
                Incontinenza::class,
                Nicturia::class,
                // Apparatus evaluation models - Riproduttivo
                ApparatoRiproduttivo::class,
                RiproduttivoMaschile::class,
                DisfunzioniSessualiMaschili::class,
                SintomiGenitaliMaschili::class,
                PatologieMaschili::class,
                IpertrofiaProstatica::class,
                Varicocele::class,
                InfertilitaMaschile::class,
                RiproduttivoFemminile::class,
                PatologieFemminili::class,
                NoduliSeno::class,
                CistiOvariche::class,
                Endometriosi::class,
                Fibromi::class,
                InfertilitaFemminile::class,
                SintomiGenitaliFemminili::class,
                Dispareunia::class,
                Menopausa::class,
                CicloMestruale::class,
                SintomiMestruali::class,
                // Apparatus evaluation models - PsicoNeuroEndocrino
                ApparatoPsicoNeuroEndocrino::class,
                AspettiPsicologici::class,
                Umore::class,
                Ansia::class,
                Depressione::class,
                Cognitivo::class,
                DifficoltaConcentrazione::class,
                Stress::class,
                PreoccupazioneCostante::class,
                Sonno::class,
                ProblemiSonno::class,
                DifficoltaAddormentarsi::class,
                RisvegliNotturni::class,
                DisturbiSonno::class,
                Neurologico::class,
                SintomiGravi::class,
                AlterazioniSensoriali::class,
                FormicoliiNeurologici::class,
                Intorpidimento::class,
                Tremori::class,
                EquilibrioCoordinazione::class,
                Vertigini::class,
                AltriSintomiNeurologici::class,
                Fobie::class,
                Allucinazioni::class,
                // Apparatus evaluation models - UnghieCute
                ApparatoUnghieCute::class,
                Unghie::class,
                PatologieCutanee::class,
                Eczemi::class,
                Psoriasi::class,
                Dermatite::class,
                Lesioni::class,
                Cicatrice::class,
                ModificheCorporee::class,
                Tatuaggi::class,
                Piercing::class,
                ControlliDermatologici::class,
                // Apparatus evaluation models - Metabolismo
                ApparatoMetabolismo::class,
                Energia::class,
                StanchezzaPersistente::class,
                SonnolenzaDiurna::class,
                Termoregolazione::class,
                ManiPiediFreddi::class,
                Peso::class,
                SintomiSistemici::class,
                Vampate::class,
                Appetito::class,
                // Apparatus evaluation models - Linfonodi
                ApparatoLinfonodi::class,
                AnomalieLinfonodi::class,
                DettagliLinfonodi::class,
                // Apparatus evaluation models - MuscoloScheletrico
                ApparatoMuscoloScheletrico::class,
                DoloreMuscoloScheletrico::class,
                LocalizzazioneDolore::class,
                Rigidita::class,
                Debolezza::class,
                AlterazioniArticolari::class,
                GonfioreArticolare::class,
                ProblemiMuscolari::class,
                Spasmi::class,
                Crampi::class,
                Contratture::class,
                TickMuscolari::class,
                TraumaPregresso::class,
                LimitazioniFunzionali::class,
                Postura::class,
                Asimmetrie::class,
                Scoliosi::class,
                EntesitiTendiniti::class,
                // Apparatus evaluation models - Nervoso
                ApparatoNervoso::class,
                SistemaNervosoCentrale::class,
                PatologieSNC::class,
                Ictus::class,
                SistemaNervosoVegetativo::class,
                SintomiVegetativi::class,
                SistemaNervosoPeriferico::class,
                Neuropatie::class,
                Parestesie::class,
                Ipoestesie::class,
                DeficitMotori::class,
                Radicolopatie::class,
                TunnelCarpale::class,
                ValutazioneNeurologica::class,
                Riflessi::class,
                Sensibilita::class,
                Coordinazione::class
            )
        )
        .name("gestionale_osteopata.realm") // Database file name
        .schemaVersion(5) // Incremented schema version to handle apparatus evaluation models
        .deleteRealmIfMigrationNeeded() // Delete old database if migration is required
        .build()
        
        println("RealmConfig: Initializing Realm database with schema version 5")
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