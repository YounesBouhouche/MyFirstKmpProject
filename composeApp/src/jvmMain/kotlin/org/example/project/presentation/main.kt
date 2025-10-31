package org.example.project.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material.icons.outlined.FullscreenExit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.example.project.di.initKoin
import org.example.project.util.BatteryManager
import java.awt.FileDialog

fun main() {
    initKoin()
    application {
        val state = rememberWindowState()
        Window(
            onCloseRequest = ::exitApplication,
            title = "MyFirstKmpProject",
            undecorated = true,
            state = state
        ) {
            val fileDialog = FileDialog(
                null as java.awt.Frame?,
                "Select File to Save",
                FileDialog.SAVE
            )
            App(
                batteryManager = remember { BatteryManager() },
                pickFile = { _, onSave ->
                    fileDialog.isVisible = true
                    fileDialog.filenameFilter = null
                    fileDialog.files.firstOrNull()?.path?.let {
                        onSave(it)
                    }
                }
            ) {
                IconButton({
                    state.isMinimized = true
                }) {
                    Icon(Icons.Default.Minimize, null)
                }
                IconButton({
                    state.placement =
                        if (state.placement == WindowPlacement.Maximized)
                            WindowPlacement.Floating
                        else
                            WindowPlacement.Maximized
                }) {
                    Icon(
                        if (state.placement == WindowPlacement.Maximized)
                            Icons.Outlined.FullscreenExit
                        else
                            Icons.Outlined.Fullscreen,
                        null
                    )
                }
                IconButton({
                    window.dispose()
                    exitApplication()
                }) {
                    Icon(Icons.Default.Close, null)
                }
            }
        }
    }
}