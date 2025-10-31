package org.example.project.presentation

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material.icons.outlined.FullscreenExit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.example.project.di.initKoin
import org.example.project.util.BatteryManager
import java.awt.FileDialog
import java.awt.Frame

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
                null as Frame?,
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
                },
                topBar = {
                    WindowDraggableArea(
                        Modifier.pointerInput(Unit) {
                            detectTapGestures(onDoubleTap = {
                                val goingToMaximize = state.placement != WindowPlacement.Maximized
                                state.placement = if (goingToMaximize) WindowPlacement.Maximized else WindowPlacement.Floating
                                window.extendedState = if (goingToMaximize) Frame.MAXIMIZED_BOTH else Frame.NORMAL
                            })
                        }
                    ) {
                        it()
                    }
                }
            ) {
                IconButton({
                    state.isMinimized = true
                    window.extendedState = Frame.ICONIFIED
                }) {
                    Icon(Icons.Default.Minimize, null)
                }
                IconButton({
                    val goingToMaximize = state.placement != WindowPlacement.Maximized
                    state.placement = if (goingToMaximize) WindowPlacement.Maximized else WindowPlacement.Floating
                    window.extendedState = if (goingToMaximize) Frame.MAXIMIZED_BOTH else Frame.NORMAL
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