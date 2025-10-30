package org.example.project.presentation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import org.example.project.util.BatteryManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        var listener: (String) -> Unit = {}
        setContent {
            val pathPicker = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.CreateDocument(
                    "image/*"
                ),
            ) { uri ->
                uri?.let {
                    listener(it.toString())
                }
            }
            val notificationPermission = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {

            }
            LaunchedEffect(notificationPermission) {
                notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            App(
                darkColorScheme = dynamicDarkColorScheme(this),
                lightColorScheme = dynamicLightColorScheme(this),
                batteryManager = remember { BatteryManager(applicationContext) },
                pickFile = { mimeType, onPick ->
                    listener = onPick
                    pathPicker.launch("image.${mimeType.substringAfterLast("/")}")
                }
            )
        }
    }
}