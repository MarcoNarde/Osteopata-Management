package com.narde.gestionaleosteopatabetto.ui.components.apparati

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

/**
 * Metadata for apparatus systems
 * Provides Italian names, icons, and descriptions for each apparatus
 */
object ApparatusMetadata {
    
    /**
     * Apparatus information data class
     */
    data class ApparatusInfo(
        val key: String,
        val italianName: String,
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val description: String
    )
    
    /**
     * All apparatus systems metadata
     */
    val allApparatus = listOf(
        ApparatusInfo(
            key = "cranio",
            italianName = "Cranio",
            icon = Icons.Default.Psychology,
            description = "Valutazione dell'apparato craniale: olfatto, vista, udito, cefalee, ATM, apparecchi ortodontici"
        ),
        ApparatusInfo(
            key = "respiratorio",
            italianName = "Respiratorio",
            icon = Icons.Default.Air,
            description = "Valutazione dell'apparato respiratorio: dispnea, tosse, allergie, sinusite, russamento"
        ),
        ApparatusInfo(
            key = "cardiovascolare",
            italianName = "Cardiovascolare",
            icon = Icons.Default.Favorite,
            description = "Valutazione dell'apparato cardiovascolare: dolore toracico, ipertensione, edemi, aritmie"
        ),
        ApparatusInfo(
            key = "gastrointestinale",
            italianName = "Gastrointestinale",
            icon = Icons.Default.Restaurant,
            description = "Valutazione dell'apparato gastrointestinale: sintomi orali, digestivi, intestinali, alvo"
        ),
        ApparatusInfo(
            key = "urinario",
            italianName = "Urinario",
            icon = Icons.Default.WaterDrop,
            description = "Valutazione dell'apparato urinario: infezioni, patologie renali, sintomi urinari"
        ),
        ApparatusInfo(
            key = "riproduttivo",
            italianName = "Riproduttivo",
            icon = Icons.Default.Person,
            description = "Valutazione dell'apparato riproduttivo: sintomi e patologie specifiche per genere"
        ),
        ApparatusInfo(
            key = "psicoNeuroEndocrino",
            italianName = "Psico-Neuro-Endocrino",
            icon = Icons.Default.Mood,
            description = "Valutazione dell'apparato psico-neuro-endocrino: umore, sonno, sintomi neurologici"
        ),
        ApparatusInfo(
            key = "unghieCute",
            italianName = "Unghie e Cute",
            icon = Icons.Default.Spa,
            description = "Valutazione di unghie e cute: patologie cutanee, lesioni, modifiche corporee, controlli"
        ),
        ApparatusInfo(
            key = "metabolismo",
            italianName = "Metabolismo",
            icon = Icons.Default.LocalFireDepartment,
            description = "Valutazione del metabolismo: energia, termoregolazione, peso, sintomi sistemici"
        ),
        ApparatusInfo(
            key = "linfonodi",
            italianName = "Linfonodi",
            icon = Icons.Default.BubbleChart,
            description = "Valutazione dei linfonodi: anomalie e caratteristiche"
        ),
        ApparatusInfo(
            key = "muscoloScheletrico",
            italianName = "Muscolo-Scheletrico",
            icon = Icons.Default.Accessibility,
            description = "Valutazione dell'apparato muscolo-scheletrico: dolore, rigidità, limitazioni, postura"
        ),
        ApparatusInfo(
            key = "nervoso",
            italianName = "Nervoso",
            icon = Icons.Default.Memory,
            description = "Valutazione dell'apparato nervoso: sistema centrale, vegetativo, periferico"
        )
    )
    
    /**
     * Get apparatus info by key
     */
    fun getApparatusInfo(key: String): ApparatusInfo? {
        return allApparatus.find { it.key == key }
    }
    
    /**
     * Get Italian name by key
     */
    fun getItalianName(key: String): String {
        return getApparatusInfo(key)?.italianName ?: key
    }
    
    /**
     * Get icon by key
     */
    fun getIcon(key: String): androidx.compose.ui.graphics.vector.ImageVector {
        return getApparatusInfo(key)?.icon ?: Icons.Default.Info
    }
    
    /**
     * Get description by key
     */
    fun getDescription(key: String): String {
        return getApparatusInfo(key)?.description ?: ""
    }
}


