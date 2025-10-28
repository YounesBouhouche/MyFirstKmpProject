package org.example.project.networking

import org.example.project.BuildConfig

actual object AppSecrets {
    actual val apiKey: String = BuildConfig.apiKey
}