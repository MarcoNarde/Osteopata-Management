package com.narde.gestionaleosteopatabetto

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Gestionale Osteopata Betto",
        state = WindowState(
            width = 1400.dp,
            height = 900.dp
        ),
        resizable = true
    ) {
        App()
    }
}