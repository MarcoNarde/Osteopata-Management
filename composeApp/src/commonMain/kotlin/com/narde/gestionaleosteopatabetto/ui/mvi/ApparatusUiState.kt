package com.narde.gestionaleosteopatabetto.ui.mvi

/**
 * UI State classes for apparatus evaluation
 * Simplified versions of Realm models for UI state management
 * These are used in AddVisitState to hold apparatus evaluation data
 */

/**
 * Cranial apparatus UI state
 */
data class ApparatoCranioState(
    val problemiOlfatto: Boolean = false,
    val problemiVista: Boolean = false,
    val problemiUdito: Boolean = false,
    val disturbiOcclusali: Boolean = false,
    val malattieParodontali: Boolean = false,
    val linguaDolente: Boolean = false,
    val cefaleaPresente: Boolean = false,
    val cefaleaIntensitaVas: String = "",
    val cefaleaFrequenza: String = "",
    val cefaleaDurataOre: String = "",
    val cefaleaTipo: String = "",
    val cefaleaLocalizzazione: List<String> = emptyList(),
    val cefaleaFattoriScatenanti: List<String> = emptyList(),
    val cefaleaFattoriAllevianti: List<String> = emptyList(),
    val emicraniaPresente: Boolean = false,
    val emicraniaConAura: Boolean = false,
    val emicraniaFrequenza: String = "",
    val atmProblemiPresenti: Boolean = false,
    val atmClickArticolare: Boolean = false,
    val atmDoloreMasticazione: Boolean = false,
    val atmLimitazioneApertura: Boolean = false,
    val atmSerramentoDiurno: Boolean = false,
    val atmBruxismoNotturno: Boolean = false,
    val atmDeviazioneMandibolare: Boolean = false,
    val apparecchioOrtodonticoPortato: Boolean = false,
    val apparecchioOrtodonticoPeriodo: String = "",
    val apparecchioOrtodonticoEtaInizio: String = "",
    val apparecchioOrtodonticoEtaFine: String = "",
    val apparecchioOrtodonticoTipo: String = ""
) {
    val hasData: Boolean
        get() = problemiOlfatto || problemiVista || problemiUdito || disturbiOcclusali ||
                malattieParodontali || linguaDolente || cefaleaPresente || emicraniaPresente ||
                atmProblemiPresenti || apparecchioOrtodonticoPortato
}

/**
 * Respiratory apparatus UI state (simplified - key fields only)
 */
data class ApparatoRespiratorioState(
    val dispneaPresente: Boolean = false,
    val dispneaSottoSforzo: Boolean = false,
    val dispneaARiposo: Boolean = false,
    val dispneaNotturna: Boolean = false,
    val oppressioneToracica: Boolean = false,
    val tossePresente: Boolean = false,
    val tosseTipo: String = "",
    val tosseNotturna: Boolean = false,
    val tosseCronica: Boolean = false,
    val tosseConSangue: Boolean = false,
    val allergieRespiratoriePresente: Boolean = false,
    val allergieRespiratorieAllergeni: List<String> = emptyList(),
    val allergieRespiratorieStagionalita: String = "",
    val raucedine: Boolean = false,
    val congestioneNasalePresente: Boolean = false,
    val congestioneNasaleCronica: Boolean = false,
    val sinusitePresente: Boolean = false,
    val sinusiteLocalizzazione: String = "",
    val russarePresente: Boolean = false,
    val russareIntensita: String = "",
    val russareApneeNotturne: Boolean = false,
    val brucioreGola: Boolean = false,
    val raffreddoriFrequentiPresente: Boolean = false,
    val raffreddoriFrequentiFrequenzaAnno: String = ""
) {
    val hasData: Boolean
        get() = dispneaPresente || oppressioneToracica || tossePresente ||
                allergieRespiratoriePresente || raucedine || congestioneNasalePresente ||
                sinusitePresente || russarePresente || brucioreGola || raffreddoriFrequentiPresente
}

/**
 * Cardiovascular apparatus UI state (simplified)
 */
data class ApparatoCardiovascolareState(
    val doloreToracicoPresente: Boolean = false,
    val doloreToracicoSottoSforzo: Boolean = false,
    val doloreToracicoARiposo: Boolean = false,
    val doloreToracicoIrradiazione: String = "",
    val storiaCardiacaAttaccoCardiaco: Boolean = false,
    val storiaCardiacaSoffi: Boolean = false,
    val ipertensionePresente: Boolean = false,
    val ipertensioneInTerapia: Boolean = false,
    val ipertensioneValoriMedi: String = "",
    val edemiPresente: Boolean = false,
    val edemiLocalizzazione: String = "",
    val edemiBilaterale: Boolean = false,
    val veneVaricosePresente: Boolean = false,
    val veneVaricoseSintomatiche: Boolean = false,
    val aritmiePresente: Boolean = false,
    val aritmiePalpitazioni: Boolean = false,
    val formicoliiPosizionaliPresente: Boolean = false,
    val formicoliiPosizionaliMani: Boolean = false,
    val formicoliiPosizionaliPiedi: Boolean = false
) {
    val hasData: Boolean
        get() = doloreToracicoPresente || storiaCardiacaAttaccoCardiaco || storiaCardiacaSoffi ||
                ipertensionePresente || edemiPresente || veneVaricosePresente ||
                aritmiePresente || formicoliiPosizionaliPresente
}

/**
 * Gastrointestinal apparatus UI state (simplified)
 */
data class ApparatoGastrointestinaleState(
    val sintomiOraliHerpesFacciali: Boolean = false,
    val sintomiOraliDifficoltaMasticare: Boolean = false,
    val sintomiOraliDifficoltaDeglutire: Boolean = false,
    val sintomiOraliBoccaSecca: Boolean = false,
    val aciditaStomacoPresente: Boolean = false,
    val aciditaStomacoFrequenza: String = "",
    val aciditaStomacoOrario: String = "",
    val aciditaStomacoBrucioreRetrosternale: Boolean = false,
    val nauseaVomito: Boolean = false,
    val reflusso: Boolean = false,
    val colonIrritabile: Boolean = false,
    val alvoAlterno: Boolean = false,
    val emorroidiPresente: Boolean = false,
    val emorroidiSanguinanti: Boolean = false,
    val alvoFrequenza: String = "",
    val alvoRegolarita: String = "",
    val alvoConsistenza: String = "",
    val alvoColore: String = "",
    val problemiEpatici: Boolean = false,
    val ittero: Boolean = false
) {
    val hasData: Boolean
        get() = sintomiOraliHerpesFacciali || sintomiOraliDifficoltaMasticare ||
                sintomiOraliDifficoltaDeglutire || sintomiOraliBoccaSecca ||
                aciditaStomacoPresente || nauseaVomito || reflusso || colonIrritabile ||
                emorroidiPresente || alvoFrequenza.isNotBlank() || problemiEpatici || ittero
}

/**
 * Urinary apparatus UI state (simplified)
 */
data class ApparatoUrinarioState(
    val infezioniPresenti: Boolean = false,
    val infezioniRicorrenti: Boolean = false,
    val infezioniUltimaInfezione: String = "",
    val infezioniFrequenzaAnno: String = "",
    val malattieRenali: Boolean = false,
    val calcoli: Boolean = false,
    val storiaColiche: Boolean = false,
    val incontinenzaPresente: Boolean = false,
    val incontinenzaDaSforzo: Boolean = false,
    val incontinenzaDaUrgenza: Boolean = false,
    val incontinenzaNotturna: Boolean = false,
    val doloreBruciore: Boolean = false,
    val urgenza: Boolean = false,
    val nicturiaPresente: Boolean = false,
    val nicturiaNumeroRisvegli: String = ""
) {
    val hasData: Boolean
        get() = infezioniPresenti || malattieRenali || calcoli || storiaColiche ||
                incontinenzaPresente || doloreBruciore || urgenza || nicturiaPresente
}

/**
 * Reproductive apparatus UI state (simplified - gender-specific sections)
 */
data class ApparatoRiproduttivoState(
    // Male section
    val maschileDisfunzioniErettile: Boolean = false,
    val maschileEiaculazionePrecoce: Boolean = false,
    val maschileLibidoRidotta: Boolean = false,
    val maschileDoloreGenitale: Boolean = false,
    val maschileMasseTesticolari: Boolean = false,
    val maschileIpertrofiaProstaticaPresente: Boolean = false,
    val maschileVaricocelePresente: Boolean = false,
    val maschileVaricoceleLato: String = "",
    // Female section
    val femminileCistiSeno: Boolean = false,
    val femminileNoduliSenoPresente: Boolean = false,
    val femminileCistiOvarichePresente: Boolean = false,
    val femminileEndometriosiPresente: Boolean = false,
    val femminileFibromiPresente: Boolean = false,
    val femminilePerditeAnomale: Boolean = false,
    val femminileDispareuniaPresente: Boolean = false,
    val femminileMenopausaPresente: Boolean = false,
    val femminileMenopausaEtaInsorgenza: String = "",
    val femminileCicloRegolare: Boolean = true,
    val femminileCicloDurataGiorni: String = "",
    val femminileCicloIntervalloGiorni: String = "",
    val femminileCicloUltimaMestruazione: String = "",
    val femminileCrampi: Boolean = false,
    val femminileCrampiIntensitaVas: String = ""
) {
    val hasData: Boolean
        get() = maschileDisfunzioniErettile || maschileEiaculazionePrecoce ||
                maschileLibidoRidotta || maschileDoloreGenitale || maschileMasseTesticolari ||
                maschileIpertrofiaProstaticaPresente || maschileVaricocelePresente ||
                femminileCistiSeno || femminileNoduliSenoPresente || femminileCistiOvarichePresente ||
                femminileEndometriosiPresente || femminileFibromiPresente ||
                femminilePerditeAnomale || femminileDispareuniaPresente ||
                femminileMenopausaPresente || femminileCicloDurataGiorni.isNotBlank() ||
                femminileCrampi
}

/**
 * Psycho-neuro-endocrine apparatus UI state (simplified)
 */
data class ApparatoPsicoNeuroEndocrinoState(
    val ansiaPresente: Boolean = false,
    val ansiaLivello: String = "",
    val ansiaDurataMesi: String = "",
    val ansiaInterferisceVita: Boolean = false,
    val depressionePresente: Boolean = false,
    val depressioneInTrattamento: Boolean = false,
    val irritabilita: Boolean = false,
    val difficoltaConcentrazionePresente: Boolean = false,
    val difficoltaConcentrazioneIntensita: String = "",
    val problemiMemoria: Boolean = false,
    val sopraffatto: Boolean = false,
    val preoccupazioneCostantePresente: Boolean = false,
    val difficoltaRilassarsi: Boolean = false,
    val sonnoQualita: String = "",
    val sonnoOreNotte: String = "",
    val difficoltaAddormentarsiPresente: Boolean = false,
    val difficoltaAddormentarsiTempoMinuti: String = "",
    val risvegliNotturniPresente: Boolean = false,
    val risvegliNotturniNumero: String = "",
    val risveglioPrecoce: Boolean = false,
    val convulsioni: Boolean = false,
    val svenimenti: Boolean = false,
    val formicoliiPresente: Boolean = false,
    val formicoliiLocalizzazione: List<String> = emptyList(),
    val tremoriPresente: Boolean = false,
    val vertiginiPresente: Boolean = false,
    val vertiginiTipo: String = "",
    val difficoltaEquilibrio: Boolean = false
) {
    val hasData: Boolean
        get() = ansiaPresente || depressionePresente || irritabilita ||
                difficoltaConcentrazionePresente || problemiMemoria || sopraffatto ||
                sonnoQualita.isNotBlank() || difficoltaAddormentarsiPresente ||
                risvegliNotturniPresente || convulsioni || svenimenti ||
                formicoliiPresente || tremoriPresente || vertiginiPresente || difficoltaEquilibrio
}

/**
 * Nails and skin apparatus UI state (simplified)
 */
data class ApparatoUnghieCuteState(
    val unghieProblemi: Boolean = false,
    val unghieDettagli: List<String> = emptyList(),
    val eczemiPresente: Boolean = false,
    val eczemiCronico: Boolean = false,
    val psoriasiPresente: Boolean = false,
    val psoriasiInTrattamento: Boolean = false,
    val dermatitePresente: Boolean = false,
    val dermatiteTipo: String = "",
    val verruche: Boolean = false,
    val vitiligine: Boolean = false,
    val cicatrici: List<String> = emptyList(), // Simplified: just descriptions
    val cheloidiTendenza: Boolean = false,
    val tatuaggiPresente: Boolean = false,
    val tatuaggiNumero: String = "",
    val piercingPresente: Boolean = false,
    val piercingNumero: String = "",
    val neiControllati: Boolean = false,
    val neiUltimoControllo: String = "",
    val neiAtipici: Boolean = false
) {
    val hasData: Boolean
        get() = unghieProblemi || eczemiPresente || psoriasiPresente ||
                dermatitePresente || verruche || vitiligine || cicatrici.isNotEmpty() ||
                cheloidiTendenza || tatuaggiPresente || piercingPresente ||
                neiControllati || neiAtipici
}

/**
 * Metabolism apparatus UI state (simplified)
 */
data class ApparatoMetabolismoState(
    val stanchezzaPersistentePresente: Boolean = false,
    val stanchezzaPersistenteIntensita: String = "",
    val stanchezzaPersistenteDurataMesi: String = "",
    val sonnolenzaDiurnaPresente: Boolean = false,
    val maniPiediFreddiPresente: Boolean = false,
    val maniPiediFreddiCostante: Boolean = false,
    val sudorazioneNotturna: Boolean = false,
    val sudorazioneEccessiva: Boolean = false,
    val intolleranzaCaldo: Boolean = false,
    val intolleranzaFreddo: Boolean = false,
    val pesoVariazioneRecente: String = "",
    val pesoKgVariati: String = "",
    val pesoPeriodoMesi: String = "",
    val pesoIntenzionale: Boolean = false,
    val doloriDiffusi: Boolean = false,
    val febbreRicorrente: Boolean = false,
    val vampatePresente: Boolean = false,
    val appetitoStato: String = "",
    val seteEccessiva: Boolean = false,
    val polifagia: Boolean = false
) {
    val hasData: Boolean
        get() = stanchezzaPersistentePresente || sonnolenzaDiurnaPresente ||
                maniPiediFreddiPresente || sudorazioneNotturna || sudorazioneEccessiva ||
                pesoVariazioneRecente.isNotBlank() || doloriDiffusi ||
                febbreRicorrente || vampatePresente || appetitoStato.isNotBlank() ||
                seteEccessiva || polifagia
}

/**
 * Lymph nodes apparatus UI state (simplified)
 */
data class ApparatoLinfonodiState(
    val anomaliePresente: Boolean = false,
    val anomalieLocalizzazione: String = "",
    val anomalieDimensione: String = "",
    val anomalieMobilita: Boolean = false,
    val anomalieDolenti: Boolean = false,
    val anomalieConsistenza: String = "",
    val note: String = ""
) {
    val hasData: Boolean
        get() = anomaliePresente || note.isNotBlank()
}

/**
 * Musculoskeletal apparatus UI state (simplified - key fields)
 */
data class ApparatoMuscoloScheletricoState(
    val dolorePresente: Boolean = false,
    val doloreSedi: List<String> = emptyList(), // Simplified: list of zone descriptions
    val doloreVas: String = "",
    val doloreTipo: String = "",
    val doloreDurata: String = "",
    val rigiditaPresente: Boolean = false,
    val rigiditaSedi: List<String> = emptyList(),
    val rigiditaOrario: String = "",
    val rigiditaDurataMinuti: String = "",
    val stanchezzaMuscolare: Boolean = false,
    val affaticamentoFacile: Boolean = false,
    val deformita: Boolean = false,
    val rossore: Boolean = false,
    val gonfiorePresente: Boolean = false,
    val spasmiPresente: Boolean = false,
    val crampiPresente: Boolean = false,
    val crampiNotturni: Boolean = false,
    val contratturePresente: Boolean = false,
    val triggerPoints: Boolean = false,
    val traumiPregressi: List<String> = emptyList(), // Simplified descriptions
    val limitazioniPresente: Boolean = false,
    val limitazioniAttivitaLimitate: List<String> = emptyList(),
    val posturaAlterazioni: List<String> = emptyList(),
    val scoliosiPresente: Boolean = false,
    val scoliosiTipo: String = "",
    val entesitiTendinitiPresente: Boolean = false
) {
    val hasData: Boolean
        get() = dolorePresente || rigiditaPresente || stanchezzaMuscolare ||
                affaticamentoFacile || deformita || rossore || gonfiorePresente ||
                spasmiPresente || crampiPresente || contratturePresente ||
                triggerPoints || traumiPregressi.isNotEmpty() ||
                limitazioniPresente || posturaAlterazioni.isNotEmpty() ||
                scoliosiPresente || entesitiTendinitiPresente
}

/**
 * Nervous apparatus UI state (simplified)
 */
data class ApparatoNervosoState(
    val epilessia: Boolean = false,
    val ictusPresente: Boolean = false,
    val ictusData: String = "",
    val ictusTipo: String = "",
    val tia: Boolean = false,
    val sclerosiMultipla: Boolean = false,
    val parkinson: Boolean = false,
    val problemiVegetativiPresenti: Boolean = false,
    val ipotensioneOrtostatica: Boolean = false,
    val sudorazioneAnomala: Boolean = false,
    val neuropatiePresente: Boolean = false,
    val neuropatieTipo: String = "",
    val parestesiePresente: Boolean = false,
    val parestesieLocalizzazione: List<String> = emptyList(),
    val parestesieTipo: String = "",
    val parestesiePosizionale: Boolean = false,
    val ipoestesiePresente: Boolean = false,
    val deficitMotoriPresente: Boolean = false,
    val deficitMotoriGrado: String = "",
    val radicolopatiePresente: Boolean = false,
    val radicolopatieLivello: String = "",
    val radicolopatieLato: String = "",
    val tunnelCarpalePresente: Boolean = false,
    val tunnelCarpaleLato: String = "",
    val tunnelCarpaleGravita: String = "",
    val riflessiStato: String = "",
    val sensibilitaTattile: String = "",
    val coordinazioneStato: String = "",
    val note: String = ""
) {
    val hasData: Boolean
        get() = epilessia || ictusPresente || tia || sclerosiMultipla || parkinson ||
                problemiVegetativiPresenti || neuropatiePresente || parestesiePresente ||
                ipoestesiePresente || deficitMotoriPresente || radicolopatiePresente ||
                tunnelCarpalePresente || riflessiStato.isNotBlank() ||
                sensibilitaTattile.isNotBlank() || coordinazioneStato.isNotBlank() || note.isNotBlank()
}


