package org.example.project

import oshi.SystemInfo

actual class BatteryManager {
    actual fun getBatteryLevel(): Int {
        val systemInfo = SystemInfo()
        val battery = systemInfo.hardware.powerSources.firstOrNull()

        return battery?.remainingCapacityPercent?.times(100)?.toInt() ?: -1
    }
}