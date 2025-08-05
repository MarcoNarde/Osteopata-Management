package com.narde.gestionaleosteopatabetto

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform