package org.example.project.presentation

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import org.example.project.di.initKoin
import org.example.project.util.BatteryManager

fun MainViewController() = ComposeUIViewController(configure = {
    initKoin()
}) {
    App(
        batteryManager = remember {
            BatteryManager()
        }
    )
}