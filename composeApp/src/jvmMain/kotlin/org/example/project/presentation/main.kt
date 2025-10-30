package org.example.project.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.project.di.initKoin
import org.example.project.util.BatteryManager

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "MyFirstKmpProject",
        ) {
            val isDark = isSystemInDarkTheme()
            App(
                isDark = isDark,
                batteryManager = remember { BatteryManager() }
            )
        }
    }
}